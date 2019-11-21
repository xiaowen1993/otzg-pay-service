package com.bcb.wxpay.util.service;

import com.bcb.util.JsonUtil;
import com.bcb.wxpay.util.WxpayUtil;
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
     * @param transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
     * @param outTradeNo    商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no
     * @param outRefundNo   商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
     * @param totalFee      订单总金额，单位为分
     * @param refundFee     退款总金额，单位为分
     *                      仅针对老资金流商户使用
     *                      REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款）
     *                      REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款
     * @author G/2016年10月19日 上午11:08:56
     */
    public final static Map<String, String> refundData(String sub_mch_id,
                                                       String transactionID,
                                                       String outTradeNo,
                                                       String outRefundNo,
                                                       Double totalFee,
                                                       Double refundFee) {
        Map<String, String> paramMap = new HashMap<>();
        //子商户号
        paramMap.put("sub_mch_id", sub_mch_id);
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //transaction_id是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
        paramMap.put("transaction_id", transactionID);
        //根据API给的签名规则进行签名
        paramMap.put("out_trade_no", outTradeNo);
        //商户系统内部的退款单号
        paramMap.put("out_refund_no", outRefundNo);
        //金额必须为整数  单位为转换为分
        //订单金额
        paramMap.put("total_fee", "" + Math.round(totalFee * 100));
        //退款金额
        paramMap.put("refund_fee", "" + Math.round(refundFee * 100));
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
        String certRootPath = "D:/workspace/bcb-pay/target/artifacts/bcb_pay_war_exploded";
        WXPayConfig wxPayConfig = new WxPayConfigImpl(SignType.HMACSHA256.name(), certRootPath);
        wxPayConfig.setUrl(WXPayConstants.getPayRefundUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        Map<String, String> result = wxPayRequest.requestCert(map);
        System.out.println("调用成功=" + result.toString());

        if (result.get("return_code") != null
                && result.get("return_code").equals("SUCCESS")
                && result.get("result_code") != null
                && result.get("result_code").equals("SUCCESS")
        ) {
            System.out.println("调用成功=" + result.toString());
            return JsonUtil.get(true, result.get("result_code"), "调用成功", result);
        } else {
            System.out.println("调用失败");
            return JsonUtil.get(false, result.get("result_code"), "调用失败");
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
     *
     * @param outTradeNo
     * @return
     */
    public final static Map<String, String> refundQueryData(String outTradeNo) {
        Map<String, String> paramMap = new HashMap<>();
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //transaction_id是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
        //根据API给的签名规则进行签名
        paramMap.put("out_trade_no", outTradeNo);
        return paramMap;
    }

    /**
     * 提交退款结果查询
     *
     * @return {transaction_id=4200000232201901097507856496, nonce_str=PM8FDErs4JbioIFY, out_refund_no_0=1901081654330013, refund_status_0=SUCCESS, sign=C2557FD486231E91B17EB63E749247662239B39D010DD12B900BED7846C774AF, refund_fee_0=1, refund_recv_accout_0=支付用户的零钱, return_msg=OK, mch_id=1519839251, refund_success_time_0=2019-01-09 12:02:30, cash_fee=1, refund_id_0=50000009452019010907917586560, out_trade_no=1901081654330013, appid=wx4dc8cb7f0eb12288, refund_fee=1, total_fee=1, result_code=SUCCESS, refund_account_0=REFUND_SOURCE_UNSETTLED_FUNDS, refund_count=1, return_code=SUCCESS, refund_channel_0=ORIGINAL}
     * @author G/2016年10月27日 上午9:04:42
     */
    public final static JSONObject postRefundQuery(Map<String, String> map) throws Exception {
        WXPayConfig wxPayConfig = new WxPayConfigImpl();
        wxPayConfig.setUrl(WXPayConstants.getPayRefundQueryUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        Map<String, String> result = wxPayRequest.request(map);
        if (result.get("result_code").equals("SUCCESS")) {
            System.out.println("调用成功=" + result.toString());
            return JsonUtil.get(true, result.get("result_code"), "调用成功", result.get("transaction_id"));
        } else {
            System.out.println("调用失败");
            return JsonUtil.get(false, result.get("result_code"), "调用失败");
        }
    }




    String subMchId = "1525006091";
    //    String subMchId = "1521225291";
    String subAppId = "wxd8de5d37b6976b55"; //


    String openid = "olFJwwK3yub_nNNXXOhkTf07nYC0";
//    String openid = "olFJwwNOyw5v5OpMq-2Ex959r5is"; //wzg


    //商户号下的公众号appid
    String gzhAppId = "wxd8de5d37b6976b55";


    /**
     * 退款
     *
     * @throws Exception
     */
    @Test
    public void refundTest() throws Exception {
        String outRefundNo = "BCB234273947298374237492";
        Map<String, String> payData = refundData(subMchId, "", "1415757673002", outRefundNo, new Double(0.01), new Double(0.01));
        Map<String, String> result = postRefundPay(payData);
        System.out.println("result=" + result);
        //{return_msg=证书已过期, return_code=FAIL}

    }
}
