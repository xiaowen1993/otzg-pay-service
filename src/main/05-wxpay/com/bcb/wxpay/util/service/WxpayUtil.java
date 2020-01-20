/**
 * 功能：
 * 1、微信财付通收款-》获取支付二维码 或者获取预付款id
 * 2、退款操作并获取返回值
 * <p>
 * //把参数转换成XML数据格式
 * String data = WxpaySubmit.ReceiveData("测试商品","16101815455603649","1");
 * //获取二维码链接
 * String codeUrl = getCodeUrl(data);
 * P("codeUrl="+codeUrl);
 * //获取预付款id
 * String prepayId = getPrepayId(data);
 * P("prepayId="+prepayId);
 * <p>
 * String data=WxpaySubmit.RefundData("","16101815455603649","124444446","1","1");
 * String result=WxpaySubmit.doRefund(data,path);
 * P("codeUrl="+result);
 */
package com.bcb.wxpay.util.service;


import com.alibaba.fastjson.JSONObject;
import com.bcb.log.util.LogUtil;
import com.bcb.pay.dto.PayOrderDto;
import com.bcb.pay.util.PayOrderDtoWxpayCheck;
import com.bcb.pay.util.PayQuery;
import com.bcb.pay.util.PayReceive;
import com.bcb.pay.util.PayResult;
import com.bcb.util.FastJsonUtil;
import com.bcb.util.FuncUtil;
import com.bcb.util.RespTips;
import com.bcb.wxpay.util.sdk.WXPayConstants;
import com.bcb.wxpay.util.sdk.WXPayRequest;
import com.bcb.wxpay.util.sdk.WXPayUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 适用于境内服务商收款接口
 *
 * @author G/2019年11月16日
 */
public class WxpayUtil implements PayReceive, PayQuery {

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
    final Map<String, String> microPayData(String subMchId, String payOrderNo, PayOrderDto payOrderDto) {

        Map<String, String> paramMap = new HashMap<>();
        //交押金
        //paramMap.put("deposit", "Y");
        paramMap.put("sub_mch_id", subMchId);
        //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        if (null != payOrderDto.getAttach())
            paramMap.put("attach", payOrderDto.getAttach());
        paramMap.put("auth_code", payOrderDto.getAuthCode());
        //商品简单描述
        paramMap.put("body", payOrderDto.getSubject());
        //订单优惠标记，代金券或立减优惠功能的参数
        paramMap.put("goods_tag", "");
        //终端设备号(商户自定义，如门店编号)
        if (null != payOrderDto.getDeviceInfo())
            paramMap.put("device_info", payOrderDto.getDeviceInfo());
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", payOrderNo);
        //金额必须为整数  单位为转换为分
        String tfee = "" + Math.round(payOrderDto.getAmount() * 100);
        paramMap.put("total_fee", tfee);
        if (payOrderDto.getIsProfitSharing().equals(1)) {
            paramMap.put("profit_sharing", "Y");
        }
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
    final Map<String, String> receiveJsapiData(String subMchId, String payOrderNo, PayOrderDto payOrderDto) {
        //如果subOpenId不为空 则必传subAppId
//        if (!CheckUtil.isEmpty(payOrderDto.getSubOpenid())
//                && CheckUtil.isEmpty(payOrderDto.getSubAppId())) {
//            return null;
//        }

        Map<String, String> paramMap = new HashMap<>();
        //openid
        paramMap.put("openid", payOrderDto.getMemberId());
        paramMap.put("sub_mch_id", subMchId);
//        paramMap.put("sub_appid", payOrderDto.getSubAppId());
//        paramMap.put("sub_openid", payOrderDto.getSubOpenid());
        //交易类型
        paramMap.put("trade_type", "JSAPI");
        //本机的Ip
        paramMap.put("spbill_create_ip", FuncUtil.getLocalIp());

        if (null != payOrderDto.getAttach())
            paramMap.put("attach", payOrderDto.getAttach());
        //描述
        paramMap.put("body", payOrderDto.getSubject());
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", payOrderNo);
        //金额必须为整数  单位为转换为分
        paramMap.put("total_fee", "" + Math.round(payOrderDto.getAmount() * 100));
        //支付成功后，回调地址
        paramMap.put("notify_url", WXPayConfig.getNotifyUrl());
        return paramMap;
    }


    /**
     * 请求收款的数据Native
     *
     * @author G/2016年10月19日 上午11:08:56
     */
    final Map<String, String> receiveNativeData(String subMchId, String payOrderNo, PayOrderDto payOrderDto) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("sub_mch_id", subMchId);
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //交易类型
        paramMap.put("trade_type", "NATIVE");

        if (null != payOrderDto.getAttach())
            paramMap.put("attach", payOrderDto.getAttach());

        //本机的Ip
        paramMap.put("spbill_create_ip", FuncUtil.getLocalIp());
        //商户根据自己业务传递的参数 必填
        paramMap.put("product_id", payOrderDto.getProductId());
        //描述
        paramMap.put("body", payOrderDto.getSubject());
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", payOrderNo);
        //金额必须为整数  单位为转换为分
        paramMap.put("total_fee", "" + Math.round(payOrderDto.getAmount() * 100));
        //支付成功后，回调地址
        paramMap.put("notify_url", WXPayConfig.getNotifyUrl());
        if (payOrderDto.getIsProfitSharing().equals(1)) {
            paramMap.put("profit_sharing", "Y");
        }
        return paramMap;
    }


    /**
     * app收款
     *
     * @return
     */
    final Map<String, String> receiveAppData(String subMchId, String payOrderNo, PayOrderDto payOrderDto) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("sub_mch_id", subMchId);
        paramMap.put("sub_appid", payOrderDto.getAppId());

        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //交易类型
        paramMap.put("trade_type", "APP");
        //本机的Ip
        paramMap.put("spbill_create_ip", FuncUtil.getLocalIp());
        //商户根据自己业务传递的参数 必填
        paramMap.put("product_id", "10001");
        //描述
        paramMap.put("body", payOrderDto.getSubject());

        if (null != payOrderDto.getAttach())
            paramMap.put("attach", payOrderDto.getAttach());
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", payOrderNo);

        //金额必须为整数  单位为转换为分
        paramMap.put("total_fee", "" + Math.round(payOrderDto.getAmount() * 100));
        //支付成功后，回调地址
        paramMap.put("notify_url", WXPayConfig.getNotifyUrl());
        return paramMap;
    }


    /**
     * h5收款
     *
     * @return
     */
    final Map<String, String> receiveH5Data(String subMchId, String payOrderNo, PayOrderDto payOrderDto) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("sub_mch_id", subMchId);
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //交易类型->H5支付的交易类型为MWEB
        paramMap.put("trade_type", "MWEB");
        //本机的Ip
        paramMap.put("spbill_create_ip", payOrderDto.getIpAddress());
        paramMap.put("scene_info", payOrderDto.getSubject());
        //描述
        paramMap.put("body", payOrderDto.getSubject());
        if (null != payOrderDto.getAttach())
            paramMap.put("attach", payOrderDto.getAttach());
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", payOrderNo);

        //金额必须为整数  单位为转换为分
        paramMap.put("total_fee", "" + Math.round(payOrderDto.getAmount() * 100));
        //支付成功后，回调地址
        paramMap.put("notify_url", WXPayConfig.getNotifyUrl());
        return paramMap;
    }

    /**
     * 小程序
     *
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
    final Map<String, String> receiveMiniData(String subMchId, String payOrderNo, PayOrderDto payOrderDto) {
        //如果subOpenId不为空 则必传subAppId
//        if (!CheckUtil.isEmpty(payOrderDto.getSubOpenid())
//                && CheckUtil.isEmpty(payOrderDto.getSubAppId())) {
//            return null;
//        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("sub_mch_id", subMchId);
        //openid
        paramMap.put("openid", payOrderDto.getMemberId());
//        paramMap.put("sub_appid", payOrderDto.getSubAppId());
//        paramMap.put("sub_openid", payOrderDto.getSubOpenid());
        //交易类型
        paramMap.put("trade_type", "JSAPI");
        //本机的Ip
        paramMap.put("spbill_create_ip", FuncUtil.getLocalIp());

        if (null != payOrderDto.getAttach())
            paramMap.put("attach", payOrderDto.getAttach());
        //描述
        paramMap.put("body", payOrderDto.getSubject());
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", payOrderNo);
        //金额必须为整数  单位为转换为分
        paramMap.put("total_fee", "" + Math.round(payOrderDto.getAmount() * 100));
        //支付成功后，回调地址
        paramMap.put("notify_url", WXPayConfig.getNotifyUrl());
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
    final PayResult postReceive(Map<String, String> map) throws Exception {
        WXPayConfig wxPayConfig = new WXPayConfig();
        wxPayConfig.setUrl(WXPayConstants.getUnifiedOrderUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        Map<String, Object> result = wxPayRequest.request(map);
        LogUtil.saveTradeLog("调用结果=" + result.toString());

        if (result.get("return_code") != null
                && result.get("return_code").equals("SUCCESS")
                && result.get("result_code") != null
                && result.get("result_code").equals("SUCCESS")
        ) {
            LogUtil.saveTradeLog("调用成功=" + result.toString());
//            return FastJsonUtil.get(true, result.get("result_code").toString(), "调用成功", result);
            return new PayResult(0, result.toString());
        } else {
            LogUtil.saveTradeLog("调用失败");
//            return FastJsonUtil.get(false, result.get("result_code").toString(), "调用失败", result);
            return new PayResult(-1);
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
    final PayResult postMicroPay(Map<String, String> map) throws Exception {
        WXPayConfig wxPayConfig = new WXPayConfig();
        wxPayConfig.setUrl(WXPayConstants.getMicroPayUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        //内置60秒的轮询
//        Map<String, Object> result = wxPayRequest.requestTimes(map);

        //不使用轮询
        Map<String, Object> result = wxPayRequest.request(map);

        LogUtil.saveTradeLog("调用结果=" + result);
        //调用结果={transaction_id=4164583679620190411182047672979, nonce_str=LbQjExfMQquvNd7A7hapTT4SzjesdobU, bank_type=CMC, openid=085e9858e9914da9a0da5522a, sign=64DA7555B343847E35A316B3E1E10322, err_code=SUCCESS, err_code_des=ok, return_msg=OK, fee_type=CNY, mch_id=1519839251, cash_fee=1, device_info=001, out_trade_no=134520174432770725, cash_fee_type=CNY, total_fee=1, appid=wx4dc8cb7f0eb12288, trade_type=MICROPAY, result_code=SUCCESS, time_end=20190411182047, attach=测试刷卡付, is_subscribe=Y, return_code=SUCCESS}
        //2020-01-04
        //{nonce_str=rZvyVJqCIzKn7VDc, out_trade_no=2020141528327765745463060,
        // trade_state=PAYERROR, appid=wxa574b9142c67f42e, sign=5540E46024AA4711F08B0C1A7C33BEEF,
        // trade_state_desc=支付失败，请撤销订单, return_msg=OK, result_code=SUCCESS,
        // attach=, mch_id=1513549201, sub_mch_id=1525006091, return_code=SUCCESS}
        if (result.get("return_code") != null
                && result.get("return_code").equals("SUCCESS")
                && result.get("result_code") != null
                && result.get("result_code").equals("SUCCESS")
        ) {
//            JSONObject jo = new JSONObject();
//            jo.put("transaction_id", result.get("transaction_id"));
//            jo.put("openid", result.get("openid"));
//            return FastJsonUtil.get(true, result.get("result_code").toString(), result.get("return_msg").toString(), jo);

            //如果返回支付结果
            if (result.get("trade_state") != null
                    && result.get("trade_state").equals("SUCCESS")) {
                LogUtil.saveTradeLog("收款成功=" + result.toString());
                return new PayResult(1);
            } else {
                return new PayResult(-1);
            }
        } else {
//            return FastJsonUtil.get(false, result.get("result_code").toString(), result.get("err_code_des").toString(), result);
            return new PayResult(-1);
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
     * @return
     */
    final Map<String, String> payQueryData(String subMchId, String payOrderNo) {
        Map<String, String> paramMap = new HashMap<>();
        //子商户号
        paramMap.put("sub_mch_id", subMchId);
        //随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //transaction_id是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
        //根据API给的签名规则进行签名
        paramMap.put("out_trade_no", payOrderNo);
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
    final PayResult postPayQuery(Map<String, String> map) throws Exception {
        WXPayConfig wxPayConfig = new WXPayConfig();
        wxPayConfig.setUrl(WXPayConstants.getOrderQueryUrl());
        WXPayRequest wxPayRequest = new WXPayRequest(wxPayConfig);
        Map<String, Object> result = wxPayRequest.request(map);

        LogUtil.saveTradeLog("调用结果=" + result.toString());
        //2019-11-26 17:41
        //{nonce_str=0chyfIZe9ENsYPZ3, trade_state=NOTPAY, sign=33D5DE519B07D4D1BBA90EBDF355F980, return_msg=OK, mch_id=1513549201, sub_mch_id=1525006091, device_info=, out_trade_no=2019112617355246141867378635, appid=wxa574b9142c67f42e, total_fee=1, trade_state_desc=订单未支付, result_code=SUCCESS, return_code=SUCCESS}
        //{nonce_str=jO9ZItXaoRewh1o3, appid=wxa574b9142c67f42e, sign=4F04D482C13F5FD14712D1730354983D, err_code=ORDERNOTEXIST, return_msg=OK, result_code=FAIL, err_code_des=订单不存在, mch_id=1513549201, sub_mch_id=1525006091, return_code=SUCCESS}
        //{transaction_id=4200000423201911268573008667, nonce_str=Htuf1711NiY17XXd, trade_state=SUCCESS, bank_type=OTHERS, openid=olFJwwNOyw5v5OpMq-2Ex959r5is, sign=130164F6749E46A9F85C5C12C426388E, return_msg=OK, fee_type=CNY, mch_id=1513549201, sub_mch_id=1525006091, cash_fee=1, out_trade_no=2019112617331281031867378635, cash_fee_type=CNY, appid=wxa574b9142c67f42e, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20191126173621, is_subscribe=Y, return_code=SUCCESS}

        //{nonce_str=sWgHGMySzRRqfB4d, trade_state=NOTPAY,
        // sign=85102AC90AC1681152E53C6691B16C56, return_msg=OK,
        // mch_id=1513549201, sub_mch_id=1525006091, device_info=,
        // out_trade_no=2020141040202900745463060, appid=wxa574b9142c67f42e,
        // total_fee=1000000, trade_state_desc=订单未支付, result_code=SUCCESS, return_code=SUCCESS}

        //{nonce_str=1I6ZXlHxrcajrmsr, out_trade_no=2020141528327765745463060, trade_state=PAYERROR,
        // appid=wxa574b9142c67f42e, sign=1FF1C741B9B9AC51ED5EF13153458E93, trade_state_desc=支付失败，
        // 请撤销订单, return_msg=OK, result_code=SUCCESS, attach=, mch_id=1513549201,
        // sub_mch_id=1525006091, return_code=SUCCESS}

        if (null == result.get("return_code")
                || !result.get("return_code").equals("SUCCESS")) {
            LogUtil.saveTradeLog("调用接口失败");
//            return FastJsonUtil.get(false, RespTips.ERROR_CODE.code, "调用接口失败");
            return new PayResult(-1);
        }

        //如果不正常返回
        if (null == result.get("result_code")
                || !result.get("result_code").equals("SUCCESS")) {
            LogUtil.saveTradeLog("支付结果错误");
//            return FastJsonUtil.get(false, result.get("err_code").toString(), "支付结果错误");
            return new PayResult(-1);
        }

        //如果返回支付结果
        if (result.get("trade_state") != null
                && result.get("trade_state").equals("SUCCESS")) {
            LogUtil.saveTradeLog("收款成功=" + result.toString());
            //{transaction_id=4200000423201911268573008667, nonce_str=Htuf1711NiY17XXd, trade_state=SUCCESS, bank_type=OTHERS,
            // openid=olFJwwNOyw5v5OpMq-2Ex959r5is, sign=130164F6749E46A9F85C5C12C426388E, return_msg=OK, fee_type=CNY,
            // mch_id=1513549201, sub_mch_id=1525006091, cash_fee=1, out_trade_no=2019112617331281031867378635,
            // cash_fee_type=CNY, appid=wxa574b9142c67f42e, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE,
            // result_code=SUCCESS, attach=, time_end=20191126173621, is_subscribe=Y, return_code=SUCCESS}

            /**
             * 如果支付成功需要做一个字段转换
             * result.get("transaction_id").toString(),result.get("openid").toString(),result.get("sub_mch_id").toString();
             * String payChannelNo,String payerId,String payeeId;
             */
//            JSONObject jo = new JSONObject();
//            jo.put("payChannelNo",result.get("transaction_id"));
//            jo.put("payerId",result.get("openid"));
//            jo.put("payeeId",result.get("sub_mch_id"));
//            return FastJsonUtil.get(true, result.get("trade_state").toString(), "收款成功",jo);
            return new PayResult(1);

        } else if (result.get("trade_state") != null
                && result.get("trade_state").equals("PAYERROR")) {
            return new PayResult(-1);
        } else {
            LogUtil.saveTradeLog("收款未成功");
//            return FastJsonUtil.get(false, result.get("trade_state").toString(), "收款未成功");
            return new PayResult(0);
        }
    }

    PayResult payResult;

    //微信支付收款业务入口
    public final PayResult pay(String subMchId, String payOrderNo, PayOrderDto payOrderDto) {
        try {
            if (payOrderDto.getPayType().equals(PayOrderDtoWxpayCheck.TradeType.JSAPI.name())) {
                payResult = postReceive(receiveJsapiData(subMchId, payOrderNo, payOrderDto));
            } else if (payOrderDto.getPayType().equals(PayOrderDtoWxpayCheck.TradeType.APP.name())) {
                payResult = postReceive(receiveAppData(subMchId, payOrderNo, payOrderDto));
            } else if (payOrderDto.getPayType().equals(PayOrderDtoWxpayCheck.TradeType.MWEB.name())) {   //h5收款校验
                payResult = postReceive(receiveH5Data(subMchId, payOrderNo, payOrderDto));
            } else if (payOrderDto.getPayType().equals(PayOrderDtoWxpayCheck.TradeType.MICROPAY.name())) {   //扫码收款校验
                payResult = postMicroPay(microPayData(subMchId, payOrderNo, payOrderDto));
            } else if (payOrderDto.getPayType().equals(PayOrderDtoWxpayCheck.TradeType.NATIVE.name())) {     //主扫
                payResult = postReceive(receiveNativeData(subMchId, payOrderNo, payOrderDto));
            }

            LogUtil.saveTradeLog("微信支付结果=>" + payResult.toString());
            return payResult;

        } catch (Exception e) {
            LogUtil.saveTradeLog("微信支付错误=>" + e.toString());
            return payResult;
        }
    }

    //微信支付收款查询接口
    public final PayResult query(String subMchId, String payOrderNo) {
        try {
            return postPayQuery(payQueryData(subMchId, payOrderNo));
        } catch (Exception e) {
            LogUtil.saveTradeLog("微信收款查询错误=>" + e.toString());
            return payResult;
        }
    }

}
