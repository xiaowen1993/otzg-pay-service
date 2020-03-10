package com.bcb.wxpay.util.service;

import com.bcb.wxpay.data.WxRefundPayData;
import com.bcb.wxpay.data.WxRefundPayQueryData;
import com.bcb.wxpay.util.sdk.WXPayConstants;
import com.bcb.wxpay.util.sdk.WXPayRequest;
import com.bcb.wxpay.util.sdk.WXPayUtil;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 退款
 */
public class RefundSubmit {

    /**
     * 请求退款的数据
     * <p>
     * <xml>
     * <appid>wx2421b1c4370ec43b</appid>
     * <mch_id>10000100</mch_id>
     * <nonce_str>6cefdb308e1e2e8aabd48cf79e546a02</nonce_str>
     * <out_refund_no>1415701182</out_refund_no>
     * <out_trade_no>1415757673</out_trade_no>
     * <refund_fee>1</refund_fee>
     * <total_fee>1</total_fee>
     * <transaction_id>4006252001201705123297353072</transaction_id>
     * <sign>FE56DD4AA85C0EECA82C35595A69E153</sign>
     * </xml>
     *
     * @author G/2016年10月19日 上午11:08:56
     */
    public final static Map<String, String> refundData(WxRefundPayData wxRefundPayDto) {
        Map<String, String> paramMap = new HashMap<>();
        //子商户号
        paramMap.put("sub_mch_id", wxRefundPayDto.getSubMchId());
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //transaction_id是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
        paramMap.put("transaction_id", wxRefundPayDto.getTransactionId());
        //根据API给的签名规则进行签名
        paramMap.put("out_trade_no", wxRefundPayDto.getOutTradeNo());
        //商户系统内部的退款单号
        paramMap.put("out_refund_no", wxRefundPayDto.getOutRefundNo());
        //金额必须为整数  单位为转换为分
        //订单金额
        paramMap.put("total_fee", "" + Math.round(wxRefundPayDto.getTotalFee() * 100));
        //退款金额
        paramMap.put("refund_fee", "" + Math.round(wxRefundPayDto.getRefundFee() * 100));
        //余额退款
        paramMap.put("refund_account", "REFUND_SOURCE_RECHARGE_FUNDS");
        return paramMap;
    }

    /**
     * 提交退款
     *
     * @return {transaction_id=4200000232201901097507856496, nonce_str=3PFz75JiEy4q7Ajw, out_refund_no=1901081654330013, sign=CDA7C77CD91BF60252234C2E54D0C097C3996EDA8080E09B2CE1BE8F52DEE448, return_msg=OK, mch_id=1519839251, refund_id=50000009452019010907917586560, cash_fee=1, out_trade_no=1901081654330013, coupon_refund_fee=0, refund_channel=, appid=wx4dc8cb7f0eb12288, refund_fee=1, total_fee=1, result_code=SUCCESS, coupon_refund_count=0, cash_refund_fee=1, return_code=SUCCESS}
     * @author G/2016年10月27日 上午9:04:42
     */
    public final static JSONObject postRefundPay(Map<String, String> map) throws Exception {
        WXPayConfig wxPayConfig = new WXPayConfig(SignType.HMACSHA256.name(), true);
        wxPayConfig.setUrl(WXPayConstants.getPayRefundUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        Map<String, Object> result = wxPayRequest.requestCert(map);
        System.out.println("调用结果=" + result.toString());

        if (result.get("return_code") != null
                && result.get("return_code").equals("SUCCESS")
                && result.get("result_code") != null
                && result.get("result_code").equals("SUCCESS")
        ) {
            System.out.println("调用成功=" + result.toString());
            return null;
//            return FastJsonUtil.get(true, result.get("result_code"), "调用成功", result);
        } else {
            System.out.println("调用失败");
            return null;
//            return FastJsonUtil.get(false, result.get("result_code"), "调用失败");
        }
    }


    /**
     * 退款结果查询
     * <xml>
     * <appid>wx2421b1c4370ec43b</appid>
     * <mch_id>10000100</mch_id>
     * <nonce_str>0b9f35f484df17a732e537c37708d1d0</nonce_str>
     * <out_refund_no></out_refund_no>
     * <out_trade_no>1415757673</out_trade_no>
     * <refund_id></refund_id>
     * <transaction_id></transaction_id>
     * <sign>66FFB727015F450D167EF38CCC549521</sign>
     * </xml>
     * @return
     */
    public final static Map<String, String> refundQueryData(WxRefundPayQueryData wxRefundPayQueryDto) {
        Map<String, String> paramMap = new HashMap<>();
        //子商户号
        paramMap.put("sub_mch_id", wxRefundPayQueryDto.getSubMchId());
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //transaction_id是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
        //根据API给的签名规则进行签名
        paramMap.put("out_trade_no", wxRefundPayQueryDto.getOutTradeNo());
        return paramMap;
    }

    /**
     * 提交退款结果查询
     *
     * @return {transaction_id=4200000232201901097507856496, nonce_str=PM8FDErs4JbioIFY, out_refund_no_0=1901081654330013, refund_status_0=SUCCESS, sign=C2557FD486231E91B17EB63E749247662239B39D010DD12B900BED7846C774AF, refund_fee_0=1, refund_recv_accout_0=支付用户的零钱, return_msg=OK, mch_id=1519839251, refund_success_time_0=2019-01-09 12:02:30, cash_fee=1, refund_id_0=50000009452019010907917586560, out_trade_no=1901081654330013, appid=wx4dc8cb7f0eb12288, refund_fee=1, total_fee=1, result_code=SUCCESS, refund_account_0=REFUND_SOURCE_UNSETTLED_FUNDS, refund_count=1, return_code=SUCCESS, refund_channel_0=ORIGINAL}
     * @author G/2016年10月27日 上午9:04:42
     */
    public final static JSONObject postRefundQuery(Map<String, String> map) throws Exception {
        WXPayConfig wxPayConfig = new WXPayConfig();
        wxPayConfig.setUrl(WXPayConstants.getPayRefundQueryUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        Map<String, Object> result = wxPayRequest.request(map);
        System.out.println("调用结果=" + result.toString());

        if (result.get("return_code") != null
                && result.get("return_code").equals("SUCCESS")
                && result.get("result_code") != null
                && result.get("result_code").equals("SUCCESS")
        ) {
            System.out.println("调用成功=" + result.toString());
            return null;
//            return FastJsonUtil.get(true, result.get("result_code"), "调用成功", result.get("transaction_id"));
        } else {
            System.out.println("调用失败");
            return null;
//            return FastJsonUtil.get(false, result.get("result_code"), "调用失败");
        }
    }








    //测试退款查询
    @Test
    public void testRefundQuery() throws Exception {
        String subMchId="1525006091";
        String outTradeNo="ff10001000310062234640847716";
        WxRefundPayQueryData wxRefundPayQueryDto = new WxRefundPayQueryData(subMchId, outTradeNo);
        Map<String, String> data = refundQueryData(wxRefundPayQueryDto);
        postRefundQuery(data);

    }

    //测试退款
    @Test
    public void testRefund() throws Exception {
        String subMchId="1525006091";
        String outTradeNo="ff10001000310062234640847716";
        String outRefundNo="fd0656c2172e480091bfb128d4e45a8d";

        Double totalFee=new Double(1);
        Double refundFee=new Double(1);
        WxRefundPayData wxRefundPayDto = new WxRefundPayData(subMchId,"",outTradeNo,outRefundNo,totalFee,refundFee);
        Map<String, String> data = refundData(wxRefundPayDto);
        postRefundPay(data);
    }
}
