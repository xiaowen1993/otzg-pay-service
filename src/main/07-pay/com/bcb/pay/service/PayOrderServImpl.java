package com.bcb.pay.service;

import com.bcb.base.AbstractServ;
import com.bcb.base.Finder;
import com.bcb.pay.dao.*;
import com.bcb.pay.dto.PayOrderDto;
import com.bcb.pay.entity.PayAccount;
import com.bcb.pay.entity.PayChannelAccount;
import com.bcb.pay.entity.PayOrder;
import com.bcb.pay.entity.PayOrderLog;
import com.bcb.pay.util.PayChannelType;
import com.bcb.util.CheckUtil;
import com.bcb.util.DateUtil;
import com.bcb.util.FuncUtil;
import com.bcb.util.LockUtil;
import com.bcb.wxpay.util.service.WxReceiveUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author G./2018/7/3 10:38
 */
@Service
public class PayOrderServImpl extends AbstractServ implements PayOrderServ {

    @Autowired
    PayOrderDao payOrderDao;
    @Autowired
    PayOrderLogDao payOrderLogDao;
    @Autowired
    PayChannelAccountServ payChannelAccountServ;

    @Override
    @Transactional
    public Map createPayOrderByUnit(String payChannelAccount, PayOrderDto payOrderDto) {
        String payOrderNo = getPayOrderNo(payOrderDto.getUnitId());
        PayOrder payOrder = new PayOrder(payOrderNo, payChannelAccount, payOrderDto);
        payOrder.setId(getId());
        //状态未支付
        payOrder.setStatus(0);
        payOrderDao.save(payOrder);

        //如果是微信支付
        if (payOrderDto.getPayChannel().equals(PayChannelType.wxpay.name())) {
            return new WxReceiveUtil().pay(payChannelAccount,payOrderNo, payOrderDto);
        }

        return null;
    }

    //自动生成订单号
    String getPayOrderNo(String unitId) {
        return DateUtil.yearMonthDayTimeShort() + FuncUtil.getRandInt(0001, 9999) + Math.abs(unitId.hashCode());
    }

    @Override
    public PayOrder findByUnitAndType(String unitId, String payType) {
        return null;
    }

    @Override
    public PayOrder findWaitByUnitAndType(String unitId, String payType) {
        return null;
    }


    @Override
    public PayOrder findByPayOrderNo(String unitId, String payOrderNo) {
        Optional<PayOrder> op = payOrderDao.findByPayOrderNo(payOrderNo);
        if(!op.isPresent()){
            return null;
        }

        PayOrder payOrder = op.get();
        //如果没有收款成功，调支付渠道接口查询
        if(payOrder.getStatus()==0){
            new Thread(()-> queryByPayChannel(payOrder)).start();
        }

        return payOrder;
    }

    //微信查询支付结果并更新数据
    Map queryByPayChannel(PayOrder payOrder){
        Map map = new HashMap();
        if (payOrder.getPayChannel().equals(PayChannelType.wxpay.name())) {
            map = new WxReceiveUtil().query(payOrder.getPayChannelAccount(),payOrder.getPayOrderNo());
            P("支付渠道查账结果=>"+map.toString());
            if(null==map
                    || map.get("success").equals(false)
                    || null == map.get("data")){
                return map;
            }
            //支付成功
            Map result = (Map)map.get("data");
            //{transaction_id=4200000423201911268573008667, nonce_str=Htuf1711NiY17XXd, trade_state=SUCCESS, bank_type=OTHERS,
            // openid=olFJwwNOyw5v5OpMq-2Ex959r5is, sign=130164F6749E46A9F85C5C12C426388E, return_msg=OK, fee_type=CNY,
            // mch_id=1513549201, sub_mch_id=1525006091, cash_fee=1, out_trade_no=2019112617331281031867378635,
            // cash_fee_type=CNY, appid=wxa574b9142c67f42e, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE,
            // result_code=SUCCESS, attach=, time_end=20191126173621, is_subscribe=Y, return_code=SUCCESS}
            //执行收款操作
            saveSuccess(payOrder,result.get("transaction_id").toString(),result.get("openid").toString(),result.get("sub_mch_id").toString());

        }
        return map;
    }


    @Override
    public boolean checkByOrderNo(String unitId,String orderNo) {
        return payOrderDao.findByUnitIdAndOrderNo(unitId,orderNo).isPresent();
    }

    @Override
    public PayOrder getSuccessByOrderNo(String orderNo) {
        Optional<PayOrder> op = payOrderDao.findByOrderNo(orderNo);
        if(op.isPresent() && op.get().getStatus()==1){
            return op.get();
        }
        return null;
    }

    @Override
    public JSONObject findPayOrderByUnit(Finder f, String unitId) {
        return null;
    }

    @Override
    @Transactional
    public boolean handleNotify(String payOrderNo, String payChannelNo, String resultCode, String payerId, String payeeId) {
        Optional<PayOrder> op = payOrderDao.findByPayOrderNo(payOrderNo);
        //如果没有订单信息返回错误
        if (!op.isPresent()) {
            return false;
        }

        PayOrder payOrder = op.get();

        //如果已经完成返回成功
        if(payOrder.getStatus()==1){
            return true;
        }

        //执行收款操作
        saveSuccess(payOrder,payChannelNo,payerId,payeeId);

        //返回支付成功
        return true;
    }


    //收款成功操作
    void saveSuccess(PayOrder payOrder,String payChannelNo,String payerId,String payeeId){
        //保存交易日志
        savePayOrderLog(payOrder,payOrder.getPayOrderNo(),payChannelNo,payerId,payeeId);

        //更新支付渠道账户及账户记录
        payChannelAccountServ.addPayAccount(payOrder.getUnitId(),payOrder.getPayOrderNo(),payOrder.getSubject(),payOrder.getPayChannel(),payChannelNo,payOrder.getAmount());

        //通知子系统收款已成功
        if(!CheckUtil.isEmpty(payOrder.getPayNotify())){
            new Thread(()->sendSubNotify(payOrder.getOrderNo(),payOrder.getPayNotify())).start();
        }
    }


    //更新支付单及支付单记录
    void savePayOrderLog(PayOrder payOrder,String payOrderNo,String payChannelNo,String payerId,String payeeId) {
        //保存支付
        payOrder.setStatus(1);
        payOrder.setUpdateTime(DateUtil.now());
        payOrder.setMemberId(payerId);
        payOrderDao.save(payOrder);
        PT("保存收款单成功 payOrderNo="+payOrderNo);

        PayOrderLog payOrderLog = new PayOrderLog();
        payOrderLog.setId(getId());
        payOrderLog.setPayChannelNo(payChannelNo);
        payOrderLog.setUnitId(payOrder.getUnitId());
        payOrderLog.setAmount(payOrder.getAmount());
        payOrderLog.setCreateTime(DateUtil.now());
        payOrderLog.setPayOrderNo(payOrder.getPayOrderNo());
        //子系统业务单号
        payOrderLog.setOrderNo(payOrder.getOrderNo());
        payOrderLog.setPayChannel(payOrder.getPayChannel());

        //支付渠道付款人账号
        payOrderLog.setPayerChannelAccount(payerId);
        //支付渠道收款人账号
        payOrderLog.setPayeeChannelAccount(payeeId);
        payOrderLog.setStatus(1);
        payOrderLogDao.save(payOrderLog);

        PT("保存收款单记录成功");
    }

    /**
     * 通知子系统收款成功
     * @param outTraderNo
     * @param notifyUrl
     */
    void sendSubNotify(String outTraderNo,String notifyUrl){
        P("发送收款成功=>"+notifyUrl);
    }


//
//    /**
//     * 系统自动读取数据库，获取订单时间为5分钟或十分钟，
//     * 由此产生的取消订单误差(倒计时到了没有取消订单)
//     * 解决方案读取数据库间隔内的订单不抛弃，而加入倒计时向量
//     * 此方法将接近完成的订单加入一个临时向量
//     *
//     * @author:G/2017年10月31日
//     * @param
//     * @return
//     */
//    static TreeMap<Long, PayOrders> shortTimeOrders = new TreeMap<>();
//
//    void addToShortTimeVector(PayOrders o) {
//        P("加入倒计时的订单id=" + o.getId());
//        shortTimeOrders.put(o.getId(), o);
//    }
//
//    /**
//     * 从倒计时订单向量删除订单
//     * 比如倒计时间隔,订单进行到下一步时,应该从倒计时向量删除
//     */
//    void deleteShortTimeVector(Long oid) {
//        P("从倒计时删除的订单id=" + oid);
//        shortTimeOrders.remove(oid);
//    }
//
//    public static String getTimeStamp() {
//        return String.valueOf(System.currentTimeMillis() / 1000);
//    }


    /**
     * 自动查询申请的微信退款订单(微信退款没有自动通知，平台内需要定时查询)
     * 1、退款提交到微信后，SuccessNum="0"。
     * 2、系统定时查找TradeRefund中，支付渠道为payChannels="wxpay" 并且 SuccessNum="0"(退款成功未确认)的 notifyid(refund_id) OutTradeNo(平台单号)
     * 3、根据refund_id 和 OutTradeNo 到微信财付通查询退款是否成功
     * 4、如果成功则更新TradeRefund.SuccessNum="1";
     * 5、修改订单状态
     *
     * @author G/2018/06/30
     */
    void doAutoRefundQuery() {

    }

//
//
//    Page findAll(Finder f, Long memberId) {
//        //统计数量
//        String countHql = "select count(distinct po) from PayOrders po";
//
//        //数据集合
//        String listHql = "select distinct po from PayOrders po ";
//
//        //查询条件
//        String hql = " where 1=1 ";
//        if (!CheckUtil.isEmpty(memberId)) {
//            hql = " where po.memberId = " + memberId;
//        }
//
//        if (!CheckUtil.isEmpty(f)) {
//            if (!CheckUtil.isEmpty(f.getStatus())) {
//                if (f.getStatus().equals(4)) {                //表示审核过的(等于-1:失败,2:通过并支付的)
//                    hql += " and (po.status != 0 )";
//                } else {
//                    hql += " and (po.status = " + f.getStatus() + ")";
//                }
//            }
//            if (!CheckUtil.isEmpty(f.getScop())) {
//                hql += " and (po.payType = " + f.getScop() + ")";
//            }
//
//            if (!CheckUtil.isEmpty(f.getStartTime())) {
//                hql += " and (po.updateTime >= '" + f.getStartTime() + "') ";
//            }
//
//            if (!CheckUtil.isEmpty(f.getEndTime())) {
//                hql += " and (po.updateTime <= '" + f.getEndTime() + "') ";
//            }
//
//            if (!CheckUtil.isEmpty(f.getKeyword())) {    //推广员、区域
//                hql += " and (" +
//                        "locate('" + f.getKeyword() + "',po.ordersNo)>0 " +
//                        "or locate('" + f.getKeyword() + "',po.details)>0 " +
//                        "or locate('" + f.getKeyword() + "',po.subject)>0 " +
//                        ") ";
//            }
//        }
//
//
//        //统计总数量
//        Long totalCount = payOrdersDao.getCountByHql(countHql + hql);
//
//        hql += " order by po.updateTime desc";
//
//        //获取集合
//        List<PayOrders> items = payOrdersDao.findListByHql(listHql + hql, f.getPageSize(), f.getStartIndex());
//
//        //返回分页数据
//        return new Page(items, totalCount, f.getPageSize(), f.getStartIndex());
//
//    }


}