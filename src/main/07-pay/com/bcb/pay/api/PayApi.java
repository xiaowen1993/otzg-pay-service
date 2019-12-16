package com.bcb.pay.api;

import com.bcb.alipay.util.AlipayUtil;
import com.bcb.base.Finder;
import com.bcb.pay.dto.PayOrderDto;
import com.bcb.pay.entity.PayAccount;
import com.bcb.pay.entity.PayChannelAccount;
import com.bcb.pay.entity.PayOrder;
import com.bcb.pay.service.PayAccountServ;
import com.bcb.pay.service.PayChannelAccountServ;
import com.bcb.pay.service.PayOrderServ;
import com.bcb.pay.util.PayOrderDtoCheckUtil;
import com.bcb.util.CheckUtil;
import com.bcb.util.RespTips;
import com.bcb.util.UrlUtil;
import com.bcb.wxpay.util.sdk.WXPayUtil;
import com.bcb.wxpay.util.service.WXPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
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

    /**
     * 支付宝回调
     * 回调频率 25小时8次回调 0m, 4m,10m,10m,1h,2h,6h,15h
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/pay/alipay/notify")
    public final void alipayPayNotify() {
        HttpServletRequest request = getRequest();
        P("支付宝回调");
        if (!new AlipayUtil().notifyCheck(request)) {//验证成功
            PT("支付宝收款回调校验失败");
            return;
        }

        Map<String, Object> params = UrlUtil.getRequestMap(request);
        P("支付宝回调参数" + params.toString());
        //{gmt_create=2019-12-11 15:45:25, charset=UTF-8, seller_email=litang@bocaibao.com.cn, subject=测试收款,
        // sign=F8h8vLkeuCirrkwFzI8pzSZmNPRcmN7xno9w3caehLpX+mf3EX06D3l6LGeDuY2+EuUmk4ErJ5Ak3E/evrupLTXnuqDMMMcT5Hy3etHDed9zILCSNxjcgy0pHs0mC2Tvo+OGPDCUknnQdWTbTlh8pebsAj+soRIy3jXd0duH9VUuDvSAZgKIW/xBRJl9u0T1Kz2G6xNFS2B047LuXV80lr3qR/twGhYPReff8phC7XitQy2DkdaCWLanFgMG1dPPeCgcyeKjaRV2Y0b5fASfM9v5RivdN43YqN6P7BKWsPb/OpnBWCXzJYgip6k6fzMXhvQb5oVrSgbBHQbYCZxQnA==,
        // buyer_id=2088002123336273, invoice_amount=0.01,
        // notify_id=2019121100222154529036275727265986,
        // fund_bill_list=[{"amount":"0.01","fundChannel":"PCREDIT"}],
        // notify_type=trade_status_sync, trade_status=TRADE_SUCCESS,
        // receipt_amount=0.01, buyer_pay_amount=0.01,
        // app_id=2019011162891191, sign_type=RSA2,
        // seller_id=2088431307185963, gmt_payment=2019-12-11 15:45:29, notify_time=2019-12-11 15:45:30,
        // version=1.0, out_trade_no=20191211154137867745463060,
        // total_amount=0.01, trade_no=2019121122001436275744646696,
        // auth_app_id=2019012863166278, buyer_logon_id=375***@qq.com,
        // point_amount=0.00}

        String outTradeNo = params.get("out_trade_no").toString();
        String tradeNo = params.get("trade_no").toString();
        //支付宝用户号
        String buyerId = params.get("buyer_id").toString();
        //支付宝授权商户的 sellerId
        String sellerId = params.get("seller_id").toString();
        if (payOrderServ.handleNotify(outTradeNo, tradeNo, params.get("trade_status").toString(), buyerId, sellerId)) {
            P("支付宝回调成功");
            sendHtml("success");
        }

    }

    /**
     * 微信回调地址
     * 回调频率：（15/15/30/180/1800/1800/1800/1800/3600，单位：秒）
     * @throws Exception
     */
    @RequestMapping(value = "/pay/wx/notify")
    public final void wxPayNotify() {
        PT("微信支付回调");
        Map<String, String> params = getMapFromRequest(getRequest());
        PT("微信支付回调参数=>" + params.toString());

        if (!WXPayUtil.isSignatureValid(params, WXPayConfig.getKey())) {
            PT("微信支付回调校验失败");
            return;
        }

        PT("微信支付回调校验成功");

        String resultCode = params.get("result_code");
        //支付渠道单号
        String transactionId = params.get("transaction_id");
        //支付单号
        String payOrderNo = params.get("out_trade_no");
        //付款账号
        String openId = params.get("openid");
        //收款账号(商户号)
        String mchId = params.get("sub_mch_id");
        if (payOrderServ.handleNotify(payOrderNo, transactionId, resultCode, openId, mchId)) {
            sendHtml("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
        }
    }

    //收款业务查询
    @RequestMapping(value = "/pay/query")
    public void payOrderQuery(String unitId, String orderNo) {
        if (CheckUtil.isEmpty(unitId)
                || CheckUtil.isEmpty(orderNo)) {
            sendParamError();
            return;
        }

        //查询收款订单
        PayOrder payOrder = payOrderServ.findByUnitAndOrderNo(unitId, orderNo);
        if (CheckUtil.isEmpty(payOrder)) {
            PT("没有对应的支付订单");
            sendJson(false, RespTips.DATA_NULL.code, "没有预生成的订单");
            return;
        }

        if (payOrder.getStatus().equals(1)) {
            PT("订单支付已成功");
            sendJson(true, RespTips.SUCCESS_CODE.code, "订单支付已成功");
            return;
        }

        //如果没有收款成功，调支付渠道接口查询
        if (payOrder.getStatus() == 0) {
            sendJson(payOrderServ.queryByPayChannel(payOrder));
            return;
        } else {
            PT("订单支付失败");
            sendJson(false, RespTips.PAYORDER_ERROR.code, RespTips.PAYORDER_ERROR.tips);
        }
    }

    //收款业务
    @RequestMapping(value = "/pay/receive")
    public void payReceive(PayOrderDto payOrderDto) {
//    public void payReceive(@RequestBody PayOrderDto payOrderDto) {
        if (CheckUtil.isEmpty(payOrderDto)) {
            sendParamError();
            return;
        }

        //策略模式校验参数
        PayOrderDtoCheckUtil payOrderDtoCheck = new PayOrderDtoCheckUtil(payOrderDto);
        payOrderDto = payOrderDtoCheck.get();
        if (null == payOrderDto) {
            sendJson(payOrderDtoCheck.getMsg());
            return;
        }

        //判断业务单号是否已经生成
        if (payOrderServ.checkByOrderNo(payOrderDto.getOrderNo())) {
            sendJson(false, RespTips.PAYORDER_FOUND.code, RespTips.PAYORDER_FOUND.tips);
            return;
        }

        //判断基本账户是否已创建
        PayAccount payAccount = payAccountServ.findByUnitId(payOrderDto.getUnitId());
        if (null == payAccount
                || !payAccount.isUseable()) {
            sendJson(false, RespTips.PAYACCOUNT_IS_UNAVAILABLE.code, RespTips.PAYACCOUNT_IS_UNAVAILABLE.tips);
            return;
        }

        //获取支付渠道商户号
        PayChannelAccount payChannelAccount = payChannelAccountServ.findByAccountAndPayChannel(payAccount.getId(), payOrderDto.getPayChannel());
        if (null == payChannelAccount
                || null == payChannelAccount.getPayChannelAccount()) {
            sendJson(false, RespTips.PAYCHANNEL_SET_ERROR.code, RespTips.PAYCHANNEL_SET_ERROR.tips);
            return;
        }

        //获取支付结果
        Map result = payOrderServ.createPayOrderByUnit(payChannelAccount.getPayChannelAccount(), payOrderDto);

        //如果返回失败
        if (result.get("success").equals(false)) {
            sendFail(result.get("msg").toString());
            return;
        }

        //如果成功
        sendSuccess(result.get("data"));

    }

    @RequestMapping(value = "/payOrder/find")
    public void payOrderFind(String unitId, String payChannel, Finder finder) {
        if (CheckUtil.isEmpty(unitId)) {
            sendParamError();
            return;
        }
        //如果成功
        sendJson(payOrderServ.findPayOrderByUnit(finder,unitId,payChannel));

    }

}

