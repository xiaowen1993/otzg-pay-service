//package com.bcb.pay.api;
//
//import com.bcb.admin.controller.AbstractController;
//import com.bcb.base.Finder;
//import com.bcb.pay.entity.Account;
//import com.bcb.pay.entity.PayOrders;
//import com.bcb.pay.service.AccountServ;
//import com.bcb.pay.service.PayOrdersServ;
//import com.bcb.util.CheckUtil;
//import com.bcb.util.RespTips;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class PayOrderAdminApi extends AbstractController {
//
//    @Autowired
//    private PayOrdersServ payOrdersServ;
//    @Autowired
//    private AccountServ accountServ;
//
//    /**
//     * 订单分页
//     * @param finder
//     */
//    @RequestMapping(value = "/admin/payOrder/find")
//    public void payOrderFind(Finder finder) {
//        sendJson(payOrdersServ.findByPayOrders(finder));
//    }
//
//    /**
//     * 同意提现
//     * @param token
//     * @param orderNo
//     */
//    @RequestMapping(value = "/admin/payOrder/pay")
//    public void payOrderPay(String token,String orderNo) {
//        //获取提现订单
//        PayOrders po = payOrdersServ.findByOrderNo(orderNo);
//        if(CheckUtil.isEmpty(po)){
//            sendJson(false, RespTips.DATA_NULL.code, "没有找到对应的订单");
//            return;
//        }
//
//        if(po.getStatus().equals(1)){
//            sendJson(false, RespTips.DATA_NULL.code, "已支付成功");
//            return;
//        }
//
//        //根据付款账户获取账户
//        Account ac = accountServ.findById(po.getPayerAccountId());
//        //如果支付渠道未设置返回
//        if (CheckUtil.isEmpty(ac.getPayChannel())) {
//            sendJson(false, RespTips.ACCOUNT_PAYCHANNEL_ERROR.code, RespTips.ACCOUNT_PAYCHANNEL_ERROR.tips);
//            return;
//        }
//
//        if (CheckUtil.isEmpty(ac.getPayChannelId())
//                && CheckUtil.isEmpty(ac.getPayChannelAccount())) {
//            sendJson(false, RespTips.ACCOUNT_PAYCHANNEL_ERROR.code, "未绑定账户");
//            return;
//        }
//
//        //发起支付业务
//        sendJson(payOrdersServ.payCash(po,ac,getUser(token).getId()));
//    }
//
//    /**
//     * 不同意提现
//     * @param token
//     * @param orderNo
//     */
//    @RequestMapping(value = "/admin/payOrder/pay/fail")
//    public void payOrderPayFail(String token,String orderNo,String approvalComment) {
//        if(CheckUtil.isEmpty(orderNo)
//                ||CheckUtil.isEmpty(approvalComment)){
//            sendParamError();
//            return;
//        }
//
//        //获取提现订单
//        PayOrders po = payOrdersServ.findByOrderNo(orderNo);
//        if(CheckUtil.isEmpty(po)){
//            sendJson(false, RespTips.DATA_NULL.code, "没有找到对应的订单");
//            return;
//        }
//
//        if(po.getStatus().equals(1)){
//            sendJson(false, RespTips.DATA_NULL.code, "已支付成功");
//            return;
//        }
//
//        //审核失败
//        int r= payOrdersServ.payCashFail(po,getUser(token).getId(),approvalComment);
//        if(r==0){
//            sendSuccess();
//        }else{
//            sendFail();
//        }
//    }
//}
