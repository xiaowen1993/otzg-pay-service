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
 * 交押金
 */
public class DepositSubmit {

    //刷脸收押金
    public final static Map<String, String> receiveDepositData(String subMchId,
                                                               String body,
                                                               String outTradeNo,
                                                               Double totalFee,
                                                               String faceCode,
                                                               String spbillCreateIp) {
        Map<String, String> paramMap = new HashMap<>();
        //是否押金人脸支付，Y-是,N-普通人脸支付
        paramMap.put("deposit", "Y");
        //子商户号
        paramMap.put("sub_mch_id", subMchId);
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //描述
        paramMap.put("body", body);
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", outTradeNo);
        //金额必须为整数  单位为转换为分
        paramMap.put("total_fee", "" + Math.round(totalFee * 100));
        //交易类型
        paramMap.put("fee_type", "CNY");
        //调用微信支付API的机器IP
        paramMap.put("spbill_create_ip", spbillCreateIp);
        //面纹id
        paramMap.put("face_code", faceCode);

        return paramMap;
    }

    /**
     * 刷脸交押金
     */
    public final static Map<String, String> postDepositPay(Map<String, String> map) throws Exception {
        WXPayConfig wxPayConfig = new WxPayConfigImpl();

//        wxPayConfig.setUseSandBox(true);
//        WXPayConstants.setUseSandBox(true);

        wxPayConfig.setUrl(WXPayConstants.getDepositFacePayUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        //内置60秒的轮询
        Map<String, String> result = wxPayRequest.requestTimes(map);
        System.out.println("调用结果=" + result);
        //调用结果={transaction_id=4164583679620190411182047672979, nonce_str=LbQjExfMQquvNd7A7hapTT4SzjesdobU, bank_type=CMC, openid=085e9858e9914da9a0da5522a, sign=64DA7555B343847E35A316B3E1E10322, err_code=SUCCESS, err_code_des=ok, return_msg=OK, fee_type=CNY, mch_id=1519839251, cash_fee=1, device_info=001, out_trade_no=134520174432770725, cash_fee_type=CNY, total_fee=1, appid=wx4dc8cb7f0eb12288, trade_type=MICROPAY, result_code=SUCCESS, time_end=20190411182047, attach=测试刷卡付, is_subscribe=Y, return_code=SUCCESS}
        if (result.get("return_code") != null
                && result.get("return_code").equals("SUCCESS")
                && result.get("result_code") != null
                && result.get("result_code").equals("SUCCESS")
        ) {
            JSONObject jo = new JSONObject();
            jo.put("transaction_id", result.get("transaction_id"));
            jo.put("openid", result.get("openid"));
            return JsonUtil.get(true, result.get("result_code"), result.get("return_msg"), jo);
        } else {
            return JsonUtil.get(false, result.get("result_code"), result.get("err_code_des"));
        }
    }




    String subMchId = "1525006091";
    //    String subMchId = "1521225291";
    String subAppId = "wxd8de5d37b6976b55"; //


    String openid = "olFJwwK3yub_nNNXXOhkTf07nYC0";
//    String openid = "olFJwwNOyw5v5OpMq-2Ex959r5is"; //wzg


    //商户号下的公众号appid
    String gzhAppId = "wxd8de5d37b6976b55";

    //押金
    @Test
    public void receiveDepositTest() throws Exception {
        String faceCode = "b713b5d2-666c-48b6-8c37-f15acf5a7069";

        Map<String, String> payData = receiveDepositData(subMchId,
                "测试订单1", "190108165433002", new Double(1.01), faceCode, WxpayUtil.localIp());

        System.out.println("payData=>" + payData);
        Map<String, String> result = postDepositPay(payData);
        System.out.println("result=" + result.toString());
        //2019-11-16 17:33
        //{return_msg=沙箱支付金额不正确，请确认验收case, return_code=FAIL}
    }

}
