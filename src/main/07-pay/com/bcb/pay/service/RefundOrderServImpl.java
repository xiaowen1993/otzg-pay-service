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
import com.bcb.util.DateUtil;
import com.bcb.util.FuncUtil;
import com.bcb.wxpay.util.service.WxRefundUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.krb5.internal.ktab.KeyTab;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

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
     * @param payOrder
     * @param refundOrderDto
     * @return
     */
    @Override
    @Transactional
    public int createRefundOrderByUnit(Long payAccountId,PayOrder payOrder, RefundOrderDto refundOrderDto) {

        //获取支付渠道商户账户
        PayChannelAccount payChannelAccount = payChannelAccountServ.findByAccountAndPayChannel(payAccountId,payOrder.getPayChannel());
        //如果支付渠道账户不存在或者支付账户余额小于退款金额返回失败
        if(null == payChannelAccount
                || FuncUtil.getBigDecimalScale(payChannelAccount.getBalance()).compareTo(FuncUtil.getBigDecimalScale(new BigDecimal(refundOrderDto.getAmount())))<0){
            return 1;
        }

        //如果冻结账户不成功
        if(!payChannelAccountServ.freezeBalance(payChannelAccount,new BigDecimal(refundOrderDto.getAmount()))){
            return 2;
        }

        //创建退款业务单
        RefundOrder refundOrder = createRefundOrder(payOrder.getPayChannel(),payOrder.getPayChannelAccount(),refundOrderDto);


        //=======================================提交支付渠道退款===================================================
        //如果是微信支付
        if (payOrder.getPayChannel().equals(PayChannelType.wxpay.name())) {
            //提交支付渠道退款
            Map map = new WxRefundUtil().refund(payChannelAccount.getPayChannelAccount(),payOrder.getPayOrderNo(),refundOrder.getPayOrderNo(),payOrder.getAmount().doubleValue(),refundOrderDto);
            //如果没有返回成功
            if(map.get("success").equals(false)){
                PT("退款业务执行失败");
                payChannelAccountServ.unFreezeBalance(payChannelAccount,new BigDecimal(refundOrderDto.getAmount()),true);
                return 3;
            }

            PT("退款业务执行成功");
            //{transaction_id=4200000434201911276476814411, nonce_str=QzsYpbhkDxagqn1G,
            // out_refund_no=2019112717363163181867378635, sign=98ED8407FC1B9FA8E479705A7C51A21066A7714836F09F5DBD4DA28E7CD5E983,
            // return_msg=OK, mch_id=1513549201, sub_mch_id=1525006091, refund_id=50300302252019112713407326039, cash_fee=1,
            // out_trade_no=2019112717261637271867378635, coupon_refund_fee=0, refund_channel=,
            // appid=wxa574b9142c67f42e, refund_fee=1, total_fee=1, result_code=SUCCESS, coupon_refund_count=0, cash_refund_fee=1, return_code=SUCCESS}
            Map result = ((Map)map.get("data"));
            String payChannelNo = result.get("refund_id").toString();
            //更新退款单
            finishRefundOrder(refundOrder,payChannelNo);

            //更新账户余额
            payChannelAccountServ.substract(payChannelAccount,refundOrderDto.getUnitId(),payOrder.getPayChannel(),refundOrder.getPayOrderNo(), refundOrderDto.getSubject(), payChannelNo, new BigDecimal(refundOrderDto.getAmount()));

            PT("更新退款单成功");
            return 0;
        }

        return 4;
    }


    //创建退款业务单,并返回退款单号
    RefundOrder createRefundOrder(String payChannel,String payChannelAccount,RefundOrderDto refundOrderDto){
        RefundOrder refundOrder = new RefundOrder();

        refundOrder.setId(getId());
        refundOrder.setUnitId(refundOrderDto.getUnitId());
        refundOrder.setMemberId(refundOrderDto.getMemberId());

        //支付系统退款业务单号
        refundOrder.setPayOrderNo(getRefundOrderNo(refundOrderDto.getUnitId()));
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

    void finishRefundOrder(RefundOrder refundOrder,String payChannelNo){
        refundOrder.setUpdateTime(DateUtil.now());
        refundOrder.setStatus(1);
        //保存退款单
        refundOrderDao.save(refundOrder);

        //增加一个退款记录单
        RefundOrderLog refundOrderLog = new RefundOrderLog();
        refundOrderLog.setId(getId());

        refundOrderLog.setPayOrderNo(refundOrder.getPayOrderNo());
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
        Map result = new WxRefundUtil().postRefundQuery(refundOrder.getPayChannelAccount(),refundOrder.getPayOrderNo());
        P("微信退款查询结果=>"+result);
        if(!result.get("success").equals(true)){
            return 1;
        }

        //{"msg":"退款成功","code":"SUCCESS","data":"4200000435201911279540935041","success":true}
        //{"msg":"调用失败","code":"FAIL","success":false}

        //更新退款单
        finishRefundOrder(refundOrder,result.get("data").toString());

        return 0;
    }
}

