/**
 * 功能：
 * 1、微信财付通收款-》获取支付二维码 或者获取预付款id
 * 2、退款操作并获取返回值
 * <p>
 * //把参数转换成XML数据格式
 * String data = WxpaySubmit.ReceiveData("测试商品","16101815455603649","1");
 * //获取二维码链接
 * String codeUrl = getCodeUrl(data);
 * System.out.println("codeUrl="+codeUrl);
 * //获取预付款id
 * String prepayId = getPrepayId(data);
 * System.out.println("prepayId="+prepayId);
 * <p>
 * String data=WxpaySubmit.RefundData("","16101815455603649","124444446","1","1");
 * String result=WxpaySubmit.doRefund(data,path);
 * System.out.println("codeUrl="+result);
 */
package com.bcb.wxpay.util.service;


import com.bcb.util.CheckUtil;
import com.bcb.util.FuncUtil;
import com.bcb.util.JsonUtil;
import com.bcb.wxpay.util.WxpayUtil;
import com.bcb.wxpay.util.company.WxpayConfig;
import com.bcb.wxpay.util.sdk.WXPayConstants;
import com.bcb.wxpay.util.sdk.WXPayRequest;
import com.bcb.wxpay.util.sdk.WXPayUtil;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 适用于境内服务商调用接口
 *
 * @author G/2019年11月16日
 */
public class ReceiveSubmit {

    /**
     * 刷脸付数据
     * 扫码付款数据
     * <xml>
     * <appid>wx2421b1c4370ec43b</appid>
     * <attach>订单额外描述</attach>
     * <auth_code>120269300684844649</auth_code>
     * <body>付款码支付测试</body>
     * <device_info>1000</device_info>
     * <goods_tag></goods_tag>
     * <mch_id>10000100</mch_id>
     * <sub_mch_id>10000101</sub_mch_id>
     * <nonce_str>8aaee146b1dee7cec9100add9b96cbe2</nonce_str>
     * <out_trade_no>1415757673</out_trade_no>
     * <spbill_create_ip>14.17.22.52</spbill_create_ip>
     * <time_expire></time_expire>
     * <total_fee>1</total_fee>
     * <sign>C29DB7DB1FD4136B84AE35604756362C</sign>
     * </xml>
     *
     * @return
     */
    public final static Map<String, String> microPayData(String subMchId,
                                                         String attach,
                                                         String outTradeNo,
                                                         String authCode,
                                                         String body,
                                                         String deviceInfo,
                                                         Double totalFee) {

        Map<String, String> paramMap = new HashMap<>();
        //交押金
        //paramMap.put("deposit", "Y");
        paramMap.put("sub_mch_id", subMchId);
        //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        paramMap.put("attach", attach);
        paramMap.put("auth_code", authCode);
        //商品简单描述
        paramMap.put("body", body);
        //订单优惠标记，代金券或立减优惠功能的参数
        paramMap.put("goods_tag", "");
        //终端设备号(商户自定义，如门店编号)
        paramMap.put("device_info", deviceInfo);
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", outTradeNo);
        //金额必须为整数  单位为转换为分
        String tfee = "" + Math.round(totalFee * 100);
        paramMap.put("total_fee", tfee);
        //支付成功后，回调地址
        return paramMap;
    }

    /**
     * 请求收款的数据Jsapi
     * <p>
     * 用户标识	openid	trade_type=JSAPI，此参数必传，用户在主商户appid下的唯一标识。
     * openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。
     * 下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
     * <p>
     * 用户子标识sub_openid	 trade_type=JSAPI，此参数必传，用户在子商户appid下的唯一标识。
     * openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。
     * 下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
     *
     * <xml>
     * <appid>wx2421b1c4370ec43b</appid>
     * <attach>支付测试</attach>
     * <body>JSAPI支付测试</body>
     * <mch_id>10000100</mch_id>
     * <detail><![CDATA[{ "goods_detail":[ { "goods_id":"iphone6s_16G", "wxpay_goods_id":"1001", "goods_name":"iPhone6s 16G", "quantity":1, "price":528800, "goods_category":"123456", "body":"苹果手机" }, { "goods_id":"iphone6s_32G", "wxpay_goods_id":"1002", "goods_name":"iPhone6s 32G", "quantity":1, "price":608800, "goods_category":"123789", "body":"苹果手机" } ] }]]></detail>
     * <nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str>
     * <notify_url>http://wxpay.wxutil.com/pub_v2/pay/notify.v2.php</notify_url>
     * <openid>oUpF8uMuAJO_M2pxb1Q9zNjWeS6o</openid>
     * <out_trade_no>1415659990</out_trade_no>
     * <spbill_create_ip>14.23.150.211</spbill_create_ip>
     * <total_fee>1</total_fee>
     * <trade_type>JSAPI</trade_type>
     * <sign>0CB01533B8C1EF103065174F50BCA001</sign>
     * </xml>
     */
    public final static Map<String, String> receiveJsapiData(String subMchId,
                                                             String subAppId,
                                                             String attach,
                                                             String body,
                                                             String outTradeNo,
                                                             Double totalFee,
                                                             String openid,
                                                             String subOpenid) {
        //如果subOpenId不为空 则必传subAppId
        if (!CheckUtil.isEmpty(subOpenid)
                && CheckUtil.isEmpty(subAppId)) {
            return null;
        }

        Map<String, String> paramMap = new HashMap<>();
        //openid
        paramMap.put("openid", openid);
        paramMap.put("sub_mch_id", subMchId);
        paramMap.put("sub_appid", subAppId);
        paramMap.put("sub_openid", subOpenid);
        //交易类型
        paramMap.put("trade_type", "JSAPI");
        //本机的Ip
        paramMap.put("spbill_create_ip", WxpayUtil.localIp());


        paramMap.put("attach", attach);
        //商户根据自己业务传递的参数 必填
        paramMap.put("product_id", "10001");
        //描述
        paramMap.put("body", body);
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", outTradeNo);
        //金额必须为整数  单位为转换为分
        paramMap.put("total_fee", "" + Math.round(totalFee * 100));
        //支付成功后，回调地址
        paramMap.put("notify_url", WxpayConfig.WXPAY_RECEIVE_NOTIFY);
        return paramMap;
    }


    /**
     * 请求收款的数据Native
     *
     * @param outTradeNo 商户系统内部的订单号
     * @param totalFee   订单总金额，单位为分
     * @author G/2016年10月19日 上午11:08:56
     */
    public final static Map<String, String> receiveNativeData(String subMchId,
                                                              String ordersName,
                                                              String outTradeNo,
                                                              Double totalFee) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("sub_mch_id", subMchId);
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //交易类型
        paramMap.put("trade_type", "NATIVE");
        //本机的Ip
        paramMap.put("spbill_create_ip", FuncUtil.getLocalIp());
        //商户根据自己业务传递的参数 必填
        paramMap.put("product_id", "10001");
        //描述
        paramMap.put("body", ordersName);
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", outTradeNo);
        //金额必须为整数  单位为转换为分
        paramMap.put("total_fee", "" + Math.round(totalFee * 100));
        //支付成功后，回调地址
        paramMap.put("notify_url", WXPayConfig.WXPAY_RECEIVE_NOTIFY);
        return paramMap;
    }


    /**
     * app收款
     *
     * @param subMchId
     * @param subAppId
     * @param body
     * @param outTradeNo
     * @param totalFee
     * @return
     */
    public final static Map<String, String> receiveAppData(String subMchId,
                                                           String subAppId,
                                                           String body,
                                                           String outTradeNo,
                                                           Double totalFee) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("sub_mch_id", subMchId);
        paramMap.put("sub_app_id", subAppId);

        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //交易类型
        paramMap.put("trade_type", "APP");
        //本机的Ip
        paramMap.put("spbill_create_ip", FuncUtil.getLocalIp());
        //商户根据自己业务传递的参数 必填
        paramMap.put("product_id", "10001");
        //描述
        paramMap.put("body", body);
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", outTradeNo);

        //金额必须为整数  单位为转换为分
        paramMap.put("total_fee", "" + Math.round(totalFee * 100));
        //支付成功后，回调地址
        paramMap.put("notify_url", WxPayConfigImpl.WXPAY_RECEIVE_NOTIFY);
        return paramMap;
    }


    /**
     * h5收款
     *
     * @param subMchId
     * @param spbill_create_ip 必须传正确的用户端IP,支持ipv4、ipv6格式，获取方式详见获取用户ip指引
     * @param body
     * @param outTradeNo
     * @param totalFee
     * @param sceneInfo        //IOS移动应用  {"h5_info": {"type":"IOS","app_name": "王者荣耀","bundle_id": "com.tencent.wzryIOS"}}
     *                         //安卓移动应用 {"h5_info": {"type":"Android","app_name": "王者荣耀","package_name": "com.tencent.tmgp.sgame"}}
     *                         //WAP网站应用  {"h5_info": {"type":"Wap","wap_url": "https://pay.qq.com","wap_name": "腾讯充值"}}
     * @return
     */
    public final static Map<String, String> receiveH5Data(String subMchId,
                                                          String spbill_create_ip,
                                                          String sceneInfo,
                                                          String body,
                                                          String outTradeNo,
                                                          Double totalFee) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("sub_mch_id", subMchId);
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //交易类型->H5支付的交易类型为MWEB
        paramMap.put("trade_type", "MWEB");
        //本机的Ip
        paramMap.put("spbill_create_ip", spbill_create_ip);
        paramMap.put("scene_info", sceneInfo);
        //描述
        paramMap.put("body", body);
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", outTradeNo);

        //金额必须为整数  单位为转换为分
        paramMap.put("total_fee", "" + Math.round(totalFee * 100));
        //支付成功后，回调地址
        paramMap.put("notify_url", WxPayConfigImpl.WXPAY_RECEIVE_NOTIFY);
        return paramMap;
    }

    /**
     * 小程序
     *
     * @param subMchId
     * @param subAppId
     * @param attach
     * @param body
     * @param outTradeNo
     * @param totalFee
     * @param openid
     * @param subOpenid
     * @return 例子参数
     * <xml>
     * <appid>wx2421b1c4370ec43b</appid>
     * <sub_appid>wx2421b1c4370ec13b</sub_appid>
     * <sub_mch_id>10000101</appid>
     * <attach>支付测试</attach>
     * <body>JSAPI支付测试</body>
     * <mch_id>10000100</mch_id>
     * <detail><![CDATA[{ "goods_detail":[ { "goods_id":"iphone6s_16G", "wxpay_goods_id":"1001", "goods_name":"iPhone6s 16G", "quantity":1, "price":528800, "goods_category":"123456", "body":"苹果手机" }, { "goods_id":"iphone6s_32G", "wxpay_goods_id":"1002", "goods_name":"iPhone6s 32G", "quantity":1, "price":608800, "goods_category":"123789", "body":"苹果手机" } ] }]]></detail>
     * <nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str>
     * <notify_url>http://wxpay.wxutil.com/pub_v2/pay/notify.v2.php</notify_url>
     * <openid>oUpF8uMuAJO_M2pxb1Q9zNjWeS6o</openid>
     * <out_trade_no>1415659990</out_trade_no>
     * <spbill_create_ip>14.23.150.211</spbill_create_ip>
     * <total_fee>1</total_fee>
     * <trade_type>JSAPI</trade_type>
     * <sign>0CB01533B8C1EF103065174F50BCA001</sign>
     * </xml>
     */
    public final static Map<String, String> receiveMiniData(String subMchId,
                                                            String subAppId,
                                                            String attach,
                                                            String body,
                                                            String outTradeNo,
                                                            Double totalFee,
                                                            String openid,
                                                            String subOpenid) {
        //如果subOpenId不为空 则必传subAppId
        if (!CheckUtil.isEmpty(subOpenid)
                && CheckUtil.isEmpty(subAppId)) {
            return null;
        }

        Map<String, String> paramMap = new HashMap<>();
        //openid
        paramMap.put("openid", openid);
        paramMap.put("sub_mch_id", subMchId);
        paramMap.put("sub_appid", subAppId);
        paramMap.put("sub_openid", subOpenid);
        //交易类型
        paramMap.put("trade_type", "JSAPI");
        //本机的Ip
        paramMap.put("spbill_create_ip", WxpayUtil.localIp());


        paramMap.put("attach", attach);
        //商户根据自己业务传递的参数 必填
        paramMap.put("product_id", "10001");
        //描述
        paramMap.put("body", body);
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", outTradeNo);
        //金额必须为整数  单位为转换为分
        paramMap.put("total_fee", "" + Math.round(totalFee * 100));
        //支付成功后，回调地址
        paramMap.put("notify_url", WxpayConfig.WXPAY_RECEIVE_NOTIFY);
        return paramMap;
    }


    /**
     * 获取全部预支付信息
     *
     * @return {nonce_str=xVY4aB6fwmqRtnS5, code_url=weixin://wxpay/bizpayurl?pr=LJDPPrh,
     * appid=wx4dc8cb7f0eb12288, sign=F71217660263C48FCE4D83B89F65AE2BFA6DA3CA9253CDDA9E4B1761E22322D0,
     * trade_type=NATIVE,
     * return_msg=OK,
     * result_code=SUCCESS,
     * mch_id=1519839251,
     * return_code=SUCCESS,
     * prepay_id=wx09174830845779aca89c32d40421876724}
     * @author G/2016年10月27日 上午9:04:42
     */
    public final static JSONObject postReceive(Map<String, String> map) throws Exception {
        WXPayConfig wxPayConfig = new WxPayConfigImpl();
        wxPayConfig.setUrl(WXPayConstants.getUnifiedOrderUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        Map<String, String> result = wxPayRequest.request(map);
        System.out.println("调用成功=" + result.toString());

        if (result.get("return_code") != null
                && result.get("return_code").equals("SUCCESS")
                && result.get("result_code") != null
                && result.get("result_code").equals("SUCCESS")
        ) {
            System.out.println("调用成功=" + result.toString());
            return JsonUtil.get(true, result.get("result_code"), result.get("return_msg"), result);
        } else {
            System.out.println("调用失败");
            return JsonUtil.get(false, result.get("result_code"), "调用失败");
        }
    }

    /**
     * 调取扫码付款
     * <p>
     * 扫码付款分两种情况
     * {1:扫码立即到账,2:系统需要等待用户确认}
     * 当第二种情况时扫码支付结果会返回 WAIT_BUYER_PAY
     * 系统必须做轮询设计等待用户付款
     * <p>
     * 轮询设计
     * 当请求支付返回【WAIT_BUYER_PAY】时，收银系统需要做轮询处理，建议：
     * 收银终端界面阻塞并提示“等待用户确认支付”。
     * 轮询间隔设为3-6秒，轮询总时长60秒左右。
     * 轮询时，收银终端界面提供手动停止功能，停止时必须调用撤销API撤销支付宝交易。
     *
     * @return 调用成功后返回微信收款单号
     * 支付成功={"success":true,
     * "code":"SUCCESS",
     * "msg":"调用成功",
     * "data":{"transaction_id":"4200000282201901106176795111",
     * "nonce_str":"ZjMe2RNNAiZlH2j1",
     * "bank_type":"CFT",
     * "openid":"o-rXO0RtnsWEHnbZsDERm2oRY6Rg",
     * "sign":"8F953DD5A213527BCF5F5D35A2087771FA3214A592010A2A4642E46CB5802821",
     * "return_msg":"OK",
     * "fee_type":"CNY",
     * "mch_id":"1519839251",
     * "cash_fee":"1",
     * "device_info":"001",
     * "out_trade_no":"1901081654330018",
     * "cash_fee_type":"CNY",
     * "appid":"wx4dc8cb7f0eb12288",
     * "total_fee":"1",
     * "trade_type":"MICROPAY",
     * "result_code":"SUCCESS",
     * "attach":"测试刷卡付",
     * "time_end":"20190110112523","is_subscribe":"N","return_code":"SUCCESS"}}
     * <p>
     * {nonce_str=Xk0qLjUIhJzr8M8t, device_info=001, appid=wx4dc8cb7f0eb12288, sign=EB85FED28C93E32DF22A52E6A3D6275388A04E0A397811E414153AF1AFC051F6, err_code=USERPAYING, return_msg=OK, result_code=FAIL, err_code_des=需要用户输入支付密码, mch_id=1519839251, return_code=SUCCESS}
     * <p>
     * 付款码无效:
     * {nonce_str=R3oG06gFiuSs7du7, device_info=001,
     * appid=wx4dc8cb7f0eb12288, sign=2E8D74E06A5792C8C3919290370C5B555D79500B2071ABAA1FC6168BD97553A6,
     * err_code=AUTH_CODE_INVALID, return_msg=OK, result_code=FAIL, err_code_des=101 付款码无效，请重新扫码,
     * mch_id=1519839251, return_code=SUCCESS}
     * <p>
     * 重复提交错误：
     * {nonce_str=gx4SS9C8nL5vjQEm, device_info=001,
     * appid=wx4dc8cb7f0eb12288, sign=20E3489180ADED698BF4F27021B9D7B88820C84237D6C7A554400CA1F224AA67,
     * err_code=TRADE_ERROR, return_msg=OK, result_code=FAIL, err_code_des=请求非法，请更换订单号再发起,
     * mch_id=1519839251, return_code=SUCCESS}
     * @author G/2016年10月27日 上午9:04:42
     */
    public final static Map<String, String> postMicroPay(Map<String, String> map) throws Exception {
        WXPayConfig wxPayConfig = new WxPayConfigImpl();
        wxPayConfig.setUrl(WXPayConstants.getMicroPayUrl());
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


    //========================================查询支付结果====================================================/

    /**
     * 条码付款结果查询
     * <xml>
     * <appid>wx2421b1c4370ec43b</appid>
     * <mch_id>10000100</mch_id>
     * <nonce_str>ec2316275641faa3aacf3cc599e8730f</nonce_str>
     * <transaction_id>1008450740201411110005820873</transaction_id>
     * <out_trade_no>1415757673</out_trade_no>
     * <sign>FDD167FAA73459FD921B144BAF4F4CA2</sign>
     * </xml>
     *
     * @param outTradeNo 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     * @return
     */
    public final static Map<String, String> payQueryData(String subMchId, String outTradeNo) {
        Map<String, String> paramMap = new HashMap<>();
        //子商户号
        paramMap.put("sub_mch_id", subMchId);
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //transaction_id是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
        //根据API给的签名规则进行签名
        paramMap.put("out_trade_no", outTradeNo);
        return paramMap;
    }

    /**
     * 提交付款结果查询
     * 支持调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，MICROPAY，详细说明见参数规定
     *
     * @return {nonce_str=onrD9MNj3CUuzAw0, out_trade_no=1901081654330016, trade_state=NOTPAY, appid=wx4dc8cb7f0eb12288, sign=FDFD78E9D5569D1E646D37387D45B77C066EB1BEBAD129A1CFD3C53F50F68F24, trade_state_desc=订单未支付, return_msg=OK, result_code=SUCCESS, mch_id=1519839251, return_code=SUCCESS}
     * 根据返回结果中的 trade_state 判断
     * SUCCESS—支付成功
     * REFUND—转入退款
     * NOTPAY—未支付
     * CLOSED—已关闭
     * REVOKED—已撤销(刷卡支付)
     * USERPAYING--用户支付中
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     * @author G/2016年10月27日 上午9:04:42
     */
    public final static JSONObject postPayQuery(Map<String, String> map) throws Exception {
        WXPayConfig wxPayConfig = new WxPayConfigImpl();
        wxPayConfig.setUrl(WXPayConstants.getOrderQueryUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        Map<String, String> result = wxPayRequest.request(map);

        System.out.println("调用结果=" + result.toString());

        if (result.get("return_code") != null
                && result.get("return_code").equals("SUCCESS")
                && result.get("result_code") != null
                && result.get("result_code").equals("SUCCESS")
        ) {
            System.out.println("调用成功=" + result.toString());
            return JsonUtil.get(true, result.get("trade_state"), "付款成功");
        } else {
            System.out.println("调用失败");
            return JsonUtil.get(false, result.get("result_code"), "调用失败");
        }
    }


    /**
     * //        String mchId = "1519839251";
     * //        String subMchId = "1414532102";
     * //真货放上去移动应用appid
     * //        String appId = "wx4dc8cb7f0eb12288";
     * //        String key = "9vf65nut0jbkpj7llrzpxmjx68er1et2";
     * <p>
     * //        String sandBoxKey = "a8ae731a3250df939c20e4994f148922";
     * <p>
     * appId = "wx2421b1c4370ec43b";
     *
     * @param args
     * @throws Exception
     */


    String subMchId = "1525006091";
    //    String subMchId = "1521225291";
    String subAppId = "wxd8de5d37b6976b55"; //


    String openid = "olFJwwK3yub_nNNXXOhkTf07nYC0";
//    String openid = "olFJwwNOyw5v5OpMq-2Ex959r5is"; //wzg


    //商户号下的公众号appid
    String gzhAppId = "wxd8de5d37b6976b55";

    public static void main(String args[]) throws Exception {

        //用户支付结果查询
//        Map<String,String> payData= payQueryData("1901081654330017");

//        Map<String,String> payData= WxpaySubmit.refundQueryData("1901081654330013");
//        Map<String,String> result = postRefundQuery(payConfig,payData);
//        System.out.println("result="+result.toString());


        //{is_subscribe=N, appid=wx2bfd0b284d2a8981, fee_type=CNY, nonce_str=c43ed6bd0bc2d9c7, out_trade_no=2017102416131015182975, transaction_id=4200000002201710240039119819, trade_type=APP, result_code=SUCCESS, sign=B828242003F1E016815E3937BB6D93D0, mch_id=1490831082, total_fee=1, time_end=20171024161332, openid=oymYh0mfBGq8emdqEAnJKaGZbgLc, bank_type=CFT, return_code=SUCCESS, cash_fee=1}
//    	String data=
//    	System.out.println("codeUrl="+data);
//    	String result=WxpaySubmit.doRefund(data,"D:/workspace/liewei/WebRoot","1490831082");
//    	System.out.println("result="+result);


//	    String data=WxpaySubmit.RefundQueryData("2001142001201703160926439734","","17316162354S3646287","","1379881902");
//	    System.out.println("data="+data);
//	    Map<String,String> result=WxpaySubmit.doRefundQuery(data);
//	    System.out.println("result="+result.toString());
    }


    /**
     * 被扫
     *
     * @throws Exception
     */
    @Test
    public void microPayTest() throws Exception {
        String attach = "订单额外描述";
        //微信付款码
//        String auth_code = "120269300684844649";
//        String auth_code = "134667220440061823";
        String auth_code = "134646462291890602";
        String body = "付款码支付测试";
        String deviceInfo = "1000";
//        String outTradeNo ="1415757673002";
        String outTradeNo = "201915757673002";

        Map<String, String> payData = microPayData(subMchId, attach, outTradeNo, auth_code, body, deviceInfo, new Double(0.01));
        Map<String, String> result = postMicroPay(payData);
        System.out.println("result=" + result.toString());
        //2019-11-16 17:35
        //result={"success":true,"code":"SUCCESS","msg":"OK","data":{"transaction_id":"4810324243320191116100403842801","openid":"085e9858e9914da9a0da5522a"}}
        //result={"success":true,"code":"SUCCESS","msg":"OK","data":{"transaction_id":"4933338013820191116105852281508","openid":"085e9858e9914da9a0da5522a"}}
        //2019-11-18 13:40 正式环境
        //result={"success":true,"code":"SUCCESS","msg":"OK","data":{"transaction_id":"4200000427201911188660138128","openid":"olFJwwNOyw5v5OpMq-2Ex959r5is"}}
        //2019-11-20 17:48
        //result={"success":true,"code":"SUCCESS","msg":"OK","data":{"transaction_id":"4200000433201911201753279992","openid":"olFJwwNOyw5v5OpMq-2Ex959r5is"}}
    }

    @Test
    public void microPayQueryTest() throws Exception {

//        Map<String, String> payData = payQueryData(subMchId, "201915757673002");
        Map<String, String> payData = payQueryData(subMchId, "201915757673002");
        Map<String, String> result = postPayQuery(payData);
        System.out.println("result=" + result.toString());
        //2019-11-18 13:40 正式环境
        //result={"success":true,"code":"SUCCESS","msg":"OK","data":{"transaction_id":"4200000427201911188660138128","openid":"olFJwwNOyw5v5OpMq-2Ex959r5is"}}
        //2019-11-20 17:49 正式环境
        //{transaction_id=4200000433201911201753279992, nonce_str=my6Yii8pVmGDeHkY, trade_state=SUCCESS, bank_type=CFT, openid=olFJwwNOyw5v5OpMq-2Ex959r5is, sign=2018005C6C0D034E2AE8DE6885B18B84, return_msg=OK, fee_type=CNY, mch_id=1513549201, sub_mch_id=1525006091, cash_fee=1, device_info=1000, out_trade_no=201915757673002, cash_fee_type=CNY, appid=wxa574b9142c67f42e, total_fee=1, trade_state_desc=支付成功, trade_type=MICROPAY, result_code=SUCCESS, attach=订单额外描述, time_end=20191120174952, is_subscribe=Y, return_code=SUCCESS}
    }

    /**
     * 主扫
     *
     * @throws Exception
     */
    @Test
    public void receiveTest() throws Exception {
        Map<String, String> payData = receiveNativeData(subMchId, "测试订单1", "20190108165433002", new Double(0.02));
        System.out.println("result=" + payData);
        Map<String, String> result = postReceive(payData);
        System.out.println("result=" + result.toString());
        //2019-11-20 17:50
        //result={"success":true,"code":"SUCCESS","msg":"OK","data":{"nonce_str":"54aoSdgaPvhQEalz","code_url":"weixin://wxpay/bizpayurl?pr=FNBIMdH","appid":"wxa574b9142c67f42e","sign":"90AB894F1780055199D4913697183590","trade_type":"NATIVE","return_msg":"OK","result_code":"SUCCESS","mch_id":"1513549201","sub_mch_id":"1525006091","return_code":"SUCCESS","prepay_id":"wx20174400980904beb055f71d1828850800"}}
    }

    /**
     * app收款
     *
     * @throws Exception
     */
    @Test
    public void receiveAppTest() throws Exception {
        Map<String, String> payData = receiveAppData(subMchId, subAppId, "测试订单1", "190108165433002", new Double(0.02));
        System.out.println("result=" + payData);
        Map<String, String> result = postReceive(payData);
        System.out.println("result=" + result.toString());
        //result={return_msg=不识别的参数sub_app_id, return_code=FAIL}
    }

    /**
     * jsapi拉起支付
     *
     * @throws Exception
     */
    @Test
    public void receiveJsapiTest() throws Exception {
        Map<String, String> payData = receiveJsapiData(subMchId, subAppId,
                "测试订单1", "190108165433002",
                "1912011111", new Double(0.02), "", openid);

        System.out.println("result=" + payData);
        Map<String, String> result = postReceive(payData);
        System.out.println("result=" + result.toString());
        //result={"success":true,"code":"SUCCESS","msg":"OK","data":{"nonce_str":"nVh7OrbLgAgApk8a","appid":"wxa574b9142c67f42e","sign":"0040E8638A91478487A948114DDDDD53403CD1FD56DC7815FB36404AA1E04DA5","trade_type":"JSAPI","return_msg":"OK","result_code":"SUCCESS","mch_id":"1513549201","sub_mch_id":"1525006091","sub_appid":"wxd8de5d37b6976b55","return_code":"SUCCESS","prepay_id":"wx16143530902125859954be2e1383085200"}}
    }

    /**
     * H5收款
     *
     * @throws Exception
     */
    @Test
    public void receiveH5Test() throws Exception {
        String localip = FuncUtil.getLocalIp();
        String sceneInfo = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://pay.qq.com\",\"wap_name\": \"腾讯充值\"}}";
        Map<String, String> payData = receiveH5Data(subMchId, localip,
                sceneInfo, "190108165433002", "19020012", new Double(0.02));

        System.out.println("result=" + payData);
        Map<String, String> result = postReceive(payData);
        System.out.println("result=" + result.toString());
        //2019-11-16 3:03
        // result={return_msg=特约子商户商户号未授权服务商的产品权限, return_code=FAIL}
    }

    /**
     * 小程序收款
     *
     * @throws Exception
     */
    @Test
    public void receiveMiniTest() throws Exception {
        Map<String, String> payData = receiveMiniData(subMchId, subAppId,
                "测试订单1", "190108165433002",
                "19120111112", new Double(0.02), "", openid);

        System.out.println("result=" + payData);
        Map<String, String> result = postReceive(payData);
        System.out.println("result=" + result.toString());
        //2019-11-16 15:21
        //result={"success":true,"code":"SUCCESS","msg":"OK","data":{"nonce_str":"JOjCK1sMQgYwbUke","appid":"wxa574b9142c67f42e","sign":"D240C055E10D313693C18B4530873818FEBD279056A045888B531884CEB49D4A","trade_type":"JSAPI","return_msg":"OK","result_code":"SUCCESS","mch_id":"1513549201","sub_mch_id":"1525006091","sub_appid":"wxd8de5d37b6976b55","return_code":"SUCCESS","prepay_id":"wx161523400643641eb19af89b1365573300"}}
    }



 }
