//package com.bcb.pay.api;
//
//import com.bcb.log.util.LogUtil;
//import com.bcb.member.api.AbstractController;
////import com.bcb.pay.alipay.AlipaySubmit;
//import com.bcb.pay.entity.PayOrders;
//import com.bcb.pay.service.AccountServ;
//import com.bcb.pay.service.PayOrdersServ;
//import com.bcb.util.CheckUtil;
//import com.bcb.util.RespTips;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.Map;
//
//import static com.bcb.wxpay.util.HtmlUtil.getMapFromRequest;
//
///**
// * @author G./2018/7/3 12:08
// */
//@RestController
//public class PayController extends AbstractController {
//
//    @Autowired
//    private PayOrdersServ payOrdersServ;
//    @Autowired
//    private AccountServ accountServ;
//
//    /**
//     * 支付宝回调
//     *
//     * @throws UnsupportedEncodingException
//     */
//    @RequestMapping(value = "/pay/alipay/notify")
//    public final void alipayPayNotify() {
//        HttpServletRequest request = getRequest();
//        LogUtil.saveTradeLog(LogUtil.getFileSavePath(), "支付宝回调");
////        if (AlipaySubmit.notifyCheck(request)) {//验证成功
////            Map<String, Object> params = UrlUtil.getRequestMap(request);
////            LogUtil.saveTradeLog(LogUtil.getFileSavePath(), "支付宝回调参数" + params.toString());
////            String orderNo = params.get("out_trade_no").toString();
////            String tradeNo = params.get("trade_no").toString();
////            //支付宝用户号
////            String buyerId = params.get("buyer_id").toString();
////            //支付宝appid
////            String appId = params.get("app_id").toString();
////            if (payOrdersServ.handleNotify(orderNo,tradeNo, params.get("trade_status").toString(), buyerId, appId)) {
////                LogUtil.saveTradeLog(LogUtil.getFileSavePath(), "支付宝回调成功");
////                sendBack("success");
////            }
////        }
//    }
//
//
//    /**
//     * 微信回调地址
//     *
//     * @throws Exception
//     */
//    @RequestMapping(value = "/pay/wx/notify")
//    public final void wxPayNotify() {
//        HttpServletRequest request = getRequest();
//        LogUtil.saveTradeLog(LogUtil.getFileSavePath(), "微信支付回调");
//        Map<String, String> params = getMapFromRequest(request);
//        LogUtil.saveTradeLog(LogUtil.getFileSavePath(), "微信支付回调参数" + params.toString());
//        String result_code = params.get("result_code");
//        String orderNo = params.get("out_trade_no");
//        //付款账号
//        String openId = params.get("openid");
//        //收款账号(商户号)
//        String mchId = params.get("mch_id");
//        if (payOrdersServ.handleNotify(orderNo,"" ,result_code, openId, mchId)) {
//            String xml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>"
//                    + "</xml>";
//            LogUtil.saveTradeLog(LogUtil.getFileSavePath(), "微信支付回调业务完成");
//            sendBack(xml);
//        }
//    }
//
//    private void sendBack(String msg) {
//        getResponse().setContentType("text/plain;charset=UTF-8");
//        try {
//            getResponse().getWriter().write(msg);
//        } catch (final IOException ex) {
//            LogUtil.print("回复支付宝失败" + ex);
//        }
//    }
//
//
//    @RequestMapping(value = "/pay/query")
//    public void payOrderQuery(String token, String ordersNo) {
//        if (CheckUtil.isEmpty(ordersNo)) {
//            sendParamError();
//            return;
//        }
//
//        //查询收款订单
//        PayOrders payOrders = payOrdersServ.findByOrderNo(ordersNo);
//        if (CheckUtil.isEmpty(payOrders)) {
//            P("没有对应的支付订单");
//            sendJson(false, RespTips.DATA_NULL.code, "没有预生成的订单");
//            return;
//        }
//
//        if (payOrders.getStatus().equals(1)) {
//            P("订单支付已成功");
//            sendJson(true, RespTips.SUCCESS_CODE.code, "订单支付已成功");
//            return;
//        } else if (payOrders.getStatus().equals(0)) {
//            P("订单支付未成功");
//            sendJson(false, RespTips.PAY_LOCK_ERROR.code, RespTips.PAY_LOCK_ERROR.tips);
//        } else {
//            P("订单支付失败");
//            sendJson(false, RespTips.PAY_ERROR.code, RespTips.PAY_ERROR.tips);
//        }
//    }
//
//
//    @RequestMapping(value = "/test/lock")
//    public final void testLock(Integer key){
//        sendSuccess(accountServ.testWatch(key));
//    }
//}
//
