package com.bcb.pay.service;

import com.bcb.base.AbstractServ;
import com.bcb.pay.dao.RefundOrderDao;
import com.bcb.pay.dao.RefundOrderLogDao;
import com.bcb.pay.dto.RefundOrderDto;
import com.bcb.pay.entity.PayChannelAccount;
import com.bcb.pay.entity.PayOrder;
import com.bcb.pay.entity.RefundOrder;
import com.bcb.pay.entity.RefundOrderLog;
import com.bcb.pay.util.PayChannelType;
import com.bcb.pay.util.PayRefund;
import com.bcb.pay.util.PayRefundUtil;
import com.bcb.util.DateUtil;
import com.bcb.util.FuncUtil;
import com.bcb.util.RespTips;
import com.bcb.wxpay.util.service.WxRefundUtil;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.krb5.internal.ktab.KeyTab;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Author G.
 * @Date 2019/11/27 0027 上午 11:31
 */
@Service
public class RefundOrderServImpl extends AbstractServ implements RefundOrderServ {
    @Autowired
    RefundOrderDao refundOrderDao;

    @Autowired
    PayChannelAccountServ payChannelAccountServ;
    @Autowired
    RefundOrderLogDao refundOrderLogDao;

    @Override
    public RefundOrder findByOrderNo(String refundOrderNo) {
        return refundOrderDao.findByRefundOrderNo(refundOrderNo).orElse(null);
    }

    @Override
    public boolean checkByRefundOrderNo(String refundOrderNo) {
        return refundOrderDao.findByRefundOrderNo(refundOrderNo).isPresent();
    }

    /**
     * 执行退款业务
     * 1.收款单支付渠道，收款单号，收款金额
     * 2.冻结收款渠道账户金额
     * 3.执行退款业务
     *
     * @param payOrder
     * @param refundOrderDto
     * @return
     */
    @Override
    @Transactional
    public int refundByUnit(Long payAccountId, PayOrder payOrder, RefundOrderDto refundOrderDto) {
        //获取支付渠道商户账户
        PayChannelAccount payChannelAccount = payChannelAccountServ.findByAccountAndPayChannel(payAccountId, payOrder.getPayChannel());
        //如果支付渠道账户不存在或者支付账户余额小于退款金额返回失败
        if (null == payChannelAccount
                || FuncUtil.getBigDecimalScale(payChannelAccount.getBalance()).compareTo(FuncUtil.getBigDecimalScale(new BigDecimal(refundOrderDto.getAmount()))) < 0) {
            return 2;
        }

        //对支付渠道账户加锁
        RLock lock = redisson.getLock(payChannelAccount.toString());
        try {
            boolean l = lock.tryLock(30, TimeUnit.SECONDS);
            if (!l) {
                P("redisson lock false");
                return 5;
            }

            //判断该退款单号是否已经生成
            if (checkByRefundOrderNo(refundOrderDto.getRefundOrderNo())) {
                return 1;
            }

            //如果冻结账户不成功
            PT("冻结支付渠道账户退款余额");
            if (!payChannelAccountServ.freezeBalance(payChannelAccount, new BigDecimal(refundOrderDto.getAmount()))) {
                return 3;
            }
            PT("冻结支付渠道账户退款余额成功。开始执行退款业务=> refundOrderDto.orderNo="+refundOrderDto.getOrderNo());

            RefundOrder refundOrder = createRefundOrder(payOrder.getPayChannel(), payOrder.getPayChannelAccount(), payOrder.getPayOrderNo(),refundOrderDto);
            PT("创建退款业务单成功=> refundOrder.getRefundOrderNo="+refundOrder.getRefundOrderNo());

            //=======================================提交支付渠道退款===================================================
            //策略模式获取退款渠道
            PayRefund payRefund = new PayRefundUtil(payOrder.getPayChannel(),refundOrderDto);
            //提交支付渠道退款
            Map map = payRefund.refund(payChannelAccount.getPayChannelAccount(), payOrder.getPayOrderNo(),refundOrder.getPayRefundOrderNo());
            //如果没有返回成功
            if (map.get("success").equals(false)) {
                PT("退款业务执行失败,账户冻结余额解冻");
                payChannelAccountServ.unFreezeBalance(payChannelAccount, new BigDecimal(refundOrderDto.getAmount()), true);
                return 4;
            }

            PT("执行退款业务成功");
            Map result = ((Map) map.get("data"));
            String payChannelNo = result.get("payChannelNo").toString();
            //更新退款单
            finishRefundOrder(refundOrder, payChannelNo);

            //更新账户余额
            payChannelAccountServ.substract(payChannelAccount, refundOrderDto.getUnitId(), payOrder.getPayChannel(), refundOrder.getPayRefundOrderNo(), refundOrderDto.getSubject(), payChannelNo, new BigDecimal(refundOrderDto.getAmount()));

            PT("更新退款单成功");
            return 0;

        } catch (Exception e) {
            rollBack();
            P("error");
            return 6;
        } finally {
            lock.unlock();
            P("redisson lock unlock");
        }
    }


    //创建退款业务单,并返回退款单号
    RefundOrder createRefundOrder(String payChannel, String payChannelAccount,String payOrderNo, RefundOrderDto refundOrderDto) {
        RefundOrder refundOrder = new RefundOrder();

        refundOrder.setId(getId());
        refundOrder.setUnitId(refundOrderDto.getUnitId());
        refundOrder.setMemberId(refundOrderDto.getMemberId());
        //收款时候的业务单号
        refundOrder.setPayOrderNo(payOrderNo);
        //支付系统退款业务单号
        refundOrder.setPayRefundOrderNo(getRefundOrderNo(refundOrderDto.getUnitId()));
        //子系统收款时候的业务单号
        refundOrder.setOrderNo(refundOrderDto.getOrderNo());
        //子系统退款业务单号
        refundOrder.setRefundOrderNo(refundOrderDto.getRefundOrderNo());


        refundOrder.setSubject(refundOrderDto.getSubject());
        refundOrder.setAmount(new BigDecimal(refundOrderDto.getAmount()));

        refundOrder.setPayChannel(payChannel);
        refundOrder.setPayChannelAccount(payChannelAccount);

        refundOrder.setCreateTime(DateUtil.now());
        refundOrder.setUpdateTime(DateUtil.now());
        refundOrder.setStatus(0);

        //保存退款单
        refundOrderDao.save(refundOrder);

        //返回退款单号
        return refundOrder;
    }

    //自动生成退款业务单号
    String getRefundOrderNo(String unitId) {
        return DateUtil.yearMonthDayTimeShort() + FuncUtil.getRandInt(10001, 19999) + Math.abs(unitId.hashCode());
    }

    void finishRefundOrder(RefundOrder refundOrder, String payChannelNo) {
        refundOrder.setUpdateTime(DateUtil.now());
        refundOrder.setStatus(1);
        //保存退款单
        refundOrderDao.save(refundOrder);

        //增加一个退款记录单
        RefundOrderLog refundOrderLog = new RefundOrderLog();
        refundOrderLog.setId(getId());

        refundOrderLog.setPayRefundOrderNo(refundOrder.getPayRefundOrderNo());
        refundOrderLog.setOrderNo(refundOrder.getOrderNo());
        refundOrderLog.setRefundOrderNo(refundOrder.getRefundOrderNo());
        refundOrderLog.setAmount(refundOrder.getAmount());
        refundOrderLog.setCreateTime(DateUtil.now());

        refundOrderLog.setPayChannel(refundOrder.getPayChannel());
        //支付渠道返回的退款单号
        refundOrderLog.setPayChannelNo(payChannelNo);
        refundOrderLog.setUnitId(refundOrder.getUnitId());
        refundOrderLog.setStatus(1);
        refundOrderLogDao.save(refundOrderLog);
    }


    @Override
    @Transactional
    public int queryRefundOrderByUnit(RefundOrder refundOrder) {
        PayRefund payRefund = new PayRefundUtil(refundOrder.getPayChannel());
        Map result = payRefund.query(refundOrder.getPayChannelAccount(), refundOrder.getPayOrderNo(),refundOrder.getPayRefundOrderNo());
        P("微信退款查询结果=>" + result);
        if (!result.get("success").equals(true)) {
            return 1;
        }

        //{"msg":"退款成功","code":"SUCCESS","data":"4200000435201911279540935041","success":true}
        //{"msg":"调用失败","code":"FAIL","success":false}

        //更新退款单
        finishRefundOrder(refundOrder, result.get("data").toString());

        return 0;
    }
}

