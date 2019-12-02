package com.bcb.pay.api;

import com.bcb.pay.dto.PayOrderDto;
import com.bcb.pay.dto.RefundOrderDto;
import com.bcb.pay.entity.PayAccount;
import com.bcb.pay.entity.PayChannelAccount;
import com.bcb.pay.entity.PayOrder;
import com.bcb.pay.service.PayAccountServ;
import com.bcb.pay.service.PayChannelAccountServ;
import com.bcb.pay.service.PayOrderServ;
import com.bcb.pay.service.RefundOrderServ;
import com.bcb.pay.util.PayOrderDtoCheck;
import com.bcb.pay.util.PayOrderDtoWxpayCheck;
import com.bcb.util.CheckUtil;
import com.bcb.util.LockUtil;
import com.bcb.util.RespTips;
import com.bcb.wxpay.util.sdk.WXPayUtil;
import com.bcb.wxpay.util.service.WXPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Map;

import static com.bcb.wxpay.util.HtmlUtil.getMapFromRequest;

/**
 * @author G./2018/7/3 12:08
 */
@RestController
public class PayApi extends AbstractController {

    @Autowired
    private PayOrderServ payOrderServ;
    @Autowired
    private PayAccountServ payAccountServ;
    @Autowired
    private PayChannelAccountServ payChannelAccountServ;
    @Autowired
    LockUtil lockUtil;


    /**
     * 支付宝回调
     *
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/pay/alipay/notify")
    public final void alipayPayNotify() {
        HttpServletRequest request = getRequest();
        P( "支付宝回调");
//        if (AlipaySubmit.notifyCheck(request)) {//验证成功
//            Map<String, Object> params = UrlUtil.getRequestMap(request);
//            LogUtil.saveTradeLog( "支付宝回调参数" + params.toString());
//            String orderNo = params.get("out_trade_no").toString();
//            String tradeNo = params.get("trade_no").toString();
//            //支付宝用户号
//            String buyerId = params.get("buyer_id").toString();
//            //支付宝appid
//            String appId = params.get("app_id").toString();
//            if (payOrdersServ.handleNotify(orderNo,tradeNo, params.get("trade_status").toString(), buyerId, appId)) {
//                LogUtil.saveTradeLog( "支付宝回调成功");
//                sendBack("success");
//            }
//        }
    }


    /**
     * 微信回调地址
     *
     * @throws Exception
     */
    @RequestMapping(value = "/pay/wx/notify")
    public final void wxPayNotify() {
        PT( "微信支付回调");
        Map<String, String> params = getMapFromRequest(getRequest());
        PT( "微信支付回调参数=>" + params.toString());

        if(!WXPayUtil.isSignatureValid(params, WXPayConfig.getKey())){
            PT( "微信支付回调校验失败");
            return;
        }

        PT( "微信支付回调校验成功");

        String resultCode = params.get("result_code");
        //支付渠道单号
        String transactionId = params.get("transaction_id");
        //支付单号
        String payOrderNo = params.get("out_trade_no");
        //付款账号
        String openId = params.get("openid");
        //收款账号(商户号)
        String mchId = params.get("sub_mch_id");
        if (payOrderServ.handleNotify(payOrderNo,transactionId ,resultCode, openId, mchId)) {
            sendBack(true);
        }
    }

    void sendBack(boolean flag) {
        getResponse().setContentType("text/plain;charset=UTF-8");
        try {
            if(flag){
                String xml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>"
                        + "</xml>";
                getResponse().getWriter().write(xml);
                PT( "微信支付回调业务完成");
            }
        } catch (final IOException ex) {
            PT("回复微信失败" + ex);
        }
    }


    @RequestMapping(value = "/pay/query")
    public void payOrderQuery(String unitId, String payOrderNo) {
        if (CheckUtil.isEmpty(unitId)
                ||CheckUtil.isEmpty(payOrderNo)) {
            sendParamError();
            return;
        }

        //查询收款订单
        PayOrder payOrder = payOrderServ.findByPayOrderNo(unitId,payOrderNo);
        if (CheckUtil.isEmpty(payOrder)) {
            PT("没有对应的支付订单");
            sendJson(false, RespTips.DATA_NULL.code, "没有预生成的订单");
            return;
        }

        if (payOrder.getStatus().equals(1)) {
            PT("订单支付已成功");
            sendJson(true, RespTips.SUCCESS_CODE.code, "订单支付已成功");
            return;
        } else if (payOrder.getStatus().equals(0)) {
            PT("订单支付未成功");
            sendJson(false, RespTips.PAYORDER_LOCK_ERROR.code, RespTips.PAYORDER_LOCK_ERROR.tips);
        } else {
            PT("订单支付失败");
            sendJson(false, RespTips.PAYORDER_ERROR.code, RespTips.PAYORDER_ERROR.tips);
        }
    }


    @RequestMapping(value = "/pay/receive")
    public void payReceive(PayOrderDto payOrderDto) {
        if (CheckUtil.isEmpty(payOrderDto)) {
            sendParamError();
            return;
        }

        //校验入参
        PayOrderDtoCheck payOrderDtoCheck = new PayOrderDtoWxpayCheck(payOrderDto);
        payOrderDto = payOrderDtoCheck.get();
        if(null == payOrderDto){
            sendJson(payOrderDtoCheck.getMsg());
            return;
        }

        //判断业务单号是否已经生成
        if(payOrderServ.checkByOrderNo(payOrderDto.getUnitId(),payOrderDto.getOrderNo())){
            sendJson(false,RespTips.PAYORDER_FOUND.code,RespTips.PAYORDER_FOUND.tips);
            return;
        }

        PayAccount payAccount = payAccountServ.findByUnitId(payOrderDto.getUnitId());
        if(null==payAccount
                || !payAccount.isUseable()){
            sendJson(false,RespTips.PAYACCOUNT_IS_UNAVAILABLE.code,RespTips.PAYACCOUNT_IS_UNAVAILABLE.tips);
            return;
        }

        //获取支付渠道商户号
        PayChannelAccount payChannelAccount = payChannelAccountServ.findByAccountAndPayChannel(payAccount.getId(),payOrderDto.getPayChannel());
        if(null == payChannelAccount){
            sendJson(false,RespTips.PAYCHANNEL_SET_ERROR.code,RespTips.PAYCHANNEL_SET_ERROR.tips);
            return;
        }

        //获取支付结果
        Map result = payOrderServ.createPayOrderByUnit(payChannelAccount.getPayChannelAccount(), payOrderDto);
        //如果没有对应的渠道
        if(null == result){
            sendFail();
            return;
        }
        //如果返回失败
        if(result.get("success").equals(false)){
            sendFail();
            return;
        }

        //如果成功
        sendSuccess(result.get("data"));

    }

}

