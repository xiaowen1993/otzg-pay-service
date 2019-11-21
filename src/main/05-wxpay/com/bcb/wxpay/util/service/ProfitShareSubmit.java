package com.bcb.wxpay.util.service;

import com.bcb.util.JsonUtil;
import com.bcb.wxpay.util.sdk.WXPayConstants;
import com.bcb.wxpay.util.sdk.WXPayRequest;
import com.bcb.wxpay.util.sdk.WXPayUtil;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 分账相关
 */
public class ProfitShareSubmit {

    /**
     * 删除分账接收方
     *
     * <xml>
     * <mch_id>10000100</mch_id>
     * <sub_mch_id>1415701182</sub_mch_id>
     * <appid>wx2421b1c4370ec43b</appid>
     * <sub_appid>wx2203b1494370e08cm</sub_appid>
     * <nonce_str>6cefdb308e1e2e8aabd48cf79e546a02</nonce_str>
     * <sign>ABC6DD4AA85C0EECA82C35595A69EFGH</sign>
     * <sign_type>HMAC-SHA256</sign_type>
     * <receiver>
     * {
     * "type": "MERCHANT_ID",
     * "account": "190001001",
     * "name": "示例商户全称"
     * }
     * </receiver>
     * </xml>
     *
     * @param subMchId
     * @param receiver
     * @return
     */
    public final static Map<String, String> deleteProfitsharingReceiverData(String subMchId,
                                                                            JSONObject receiver) {
        Map<String, String> paramMap = new HashMap<>();
        //子商户号
        paramMap.put("sub_mch_id", subMchId);
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //分账者
        paramMap.put("receiver", receiver.toString());

        return paramMap;
    }

    public final static Map<String, String> deleteProfitsharingReceiver(Map<String, String> map) throws Exception {
        WXPayConfig wxPayConfig = new WxPayConfigImpl(SignType.HMACSHA256.name());
        wxPayConfig.setUrl(WXPayConstants.getProfitSharingAddReceiverUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        //内置60秒的轮询
        Map<String, String> result = wxPayRequest.request(map);
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




    /**
     * 添加分账接收方
     *
     * <xml>
     * <mch_id>10000100</mch_id>
     * <sub_mch_id>1415701182</sub_mch_id>
     * <appid>wx2421b1c4370ec43b</appid>
     * <sub_appid>wx2203b1494370e08cm</sub_appid>
     * <nonce_str>6cefdb308e1e2e8aabd48cf79e546a02</nonce_str>
     * <sign>ABC6DD4AA85C0EECA82C35595A69EFGH</sign>
     * <sign_type>HMAC-SHA256</sign_type>
     * <receiver>
     * {
     * "type": "MERCHANT_ID",
     * "account": "190001001",
     * "name": "示例商户全称",
     * "relation_type": "STORE_OWNER"
     * }
     * </receiver>
     * </xml>
     */
    public final static Map<String, String> addProfitsharingReceiverData(String subMchId,
                                                                         JSONObject receiver) {
        Map<String, String> paramMap = new HashMap<>();
        //子商户号
        paramMap.put("sub_mch_id", subMchId);
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //分账者
        paramMap.put("receiver", receiver.toString());

        return paramMap;
    }

    /**
     * 服务商分账
     * <p>
     * 服务商按指令分账主要是用于服务商帮助特约商户完成订单收单成功以后的资金分配。
     *
     * @param map
     * @return
     * @throws Exception
     */
    public final static Map<String, String> addProfitsharingReceiver(Map<String, String> map) throws Exception {
        WXPayConfig wxPayConfig = new WxPayConfigImpl(SignType.HMACSHA256.name());
        wxPayConfig.setUrl(WXPayConstants.getProfitSharingAddReceiverUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        //内置60秒的轮询
        Map<String, String> result = wxPayRequest.request(map);
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

    @Test
    public void addReceiverTest() throws Exception {
        JSONObject jsonObject = new JSONObject();
        //MERCHANT_ID：商户ID PERSONAL_WECHATID：个人微信号PERSONAL_OPENID：个人openid（由父商户APPID转换得到）PERSONAL_SUB_OPENID: 个人sub_openid（由子商户APPID转换得到）
        jsonObject.put("type", "PERSONAL_WECHATID");
        jsonObject.put("account", openid);
        jsonObject.put("name", "示例商户全称");
        jsonObject.put("relation_type", "STORE_OWNER");
        Map<String, String> payData = addProfitsharingReceiverData(subMchId, jsonObject);
        Map<String, String> result = addProfitsharingReceiver(payData);
        System.out.println("result=" + result);

        //2019-11-21 09:15
        //{nonce_str=b625b1aeaa2e7545, receiver={"type":"PERSONAL_WECHATID","account":"olFJwwK3yub_nNNXXOhkTf07nYC0","relation_type":"STORE_OWNER"}, appid=wxa574b9142c67f42e, sign=72C79EAB30C832FDDB57FD2F0A64382AA12877B3CF104AEB986F247F9FE9343D, err_code=NOAUTH, result_code=FAIL, err_code_des=无分账权限, mch_id=1513549201, sub_mch_id=1525006091, return_code=SUCCESS}
        //result={"success":false,"code":"FAIL","msg":"无分账权限"}

    }

}
