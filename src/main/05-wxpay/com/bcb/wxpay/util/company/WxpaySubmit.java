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
package com.bcb.wxpay.util.company;


import com.bcb.util.SubmitUtil;
import com.bcb.wxpay.util.*;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 *  @author G/2016年10月14日
 *
 */
public class WxpaySubmit {

    /**
     * 获取二维码链接getsignkey
     * @param data
     * @return
     */
    public final static String getCodeUrl(String data) {
        String code_url = "";
        String resultXml = HtmlUtil.postData(WxpayConfig.getUnifiedOrderUrl(), data);
        Map<String, String> returnMap = XmlUtil.parse(resultXml);
        if (returnMap != null && returnMap.get("return_code").equals("SUCCESS") && returnMap.get("result_code").equals("SUCCESS")) {
            code_url = returnMap.get("code_url");
        } else {
            code_url = returnMap.get("err_code");
        }
        return code_url;
    }

    /**
     * 获取二维码链接
     * @param data
     * @return
     */
    public final static String getCodeUrl2(String data, String mchid) {
        String code_url = "weixin://wxpay/bizpayurl";
        if (mchid.equals(WxpayConfig.GZHMCHID)) {
            code_url += "?appid=" + WxpayConfig.GZHAPPID;
            code_url += "&mch_id" + WxpayConfig.GZHMCHID;
        } else if (mchid.equals(WxpayConfig.APPMCHID)) {
            code_url += "?appid=" + WxpayConfig.APPAPPID;
            code_url += "&mch_id" + WxpayConfig.APPMCHID;
        }

        String resultXml = HtmlUtil.postData(WxpayConfig.getUnifiedOrderUrl(), data);
        Map<String, String> returnMap = XmlUtil.parse(resultXml);
        if (returnMap != null && returnMap.get("return_code").equals("SUCCESS") && returnMap.get("result_code").equals("SUCCESS")) {
            code_url = returnMap.get("code_url");
        } else {
            code_url = returnMap.get("return_msg") + "|" + returnMap.get("err_code");
        }
        return code_url;
    }



    //统一获取mch头部参数
    public final static Map<String, String> getMchHead(String mchid) {
        Map<String, String> paramMap = new HashMap<>();
        if (mchid.equals(WxpayConfig.GZHMCHID)) {
            //商户号:开通微信支付后分配,必填
            paramMap.put("mch_id", WxpayConfig.GZHMCHID);
            //appid:每个公众号都有一个appid,必填
            paramMap.put("appid", WxpayConfig.GZHAPPID);
            //商户号对应key
            paramMap.put("key", WxpayConfig.GZHAPPID);
        } else if (mchid.equals(WxpayConfig.APPMCHID)) {
            //商户号:开通微信支付后分配,必填
            paramMap.put("mch_id", WxpayConfig.APPMCHID);
            //appid:每个公众号都有一个appid,必填
            paramMap.put("appid", WxpayConfig.APPAPPID);
            //商户号对应key
            paramMap.put("key", WxpayConfig.APPKEY);
        } else {
            return null;
        }

        //如果使用沙箱环境
        if (WxpayConfig.USESANDBOX) {
            paramMap.put("key", WxpayConfig.SANDBOXKEY);
        }

        //随机数
        paramMap.put("nonce_str", WxpayUtil.getNonce("nonce", 16));
        return paramMap;
    }

    /**
     * weixin://wxpay/bizpayurl
     * ?appid=wx2421b1c4370ec43b
     * &mch_id=10000100
     * &nonce_str=f6808210402125e30663234f94c87a8c
     * &product_id=1
     * &time_stamp=1415949957
     * &sign=512F68131DD251DA4A45DA79CC7EFE9D
     */
    public final static String CodeUrlData(String mchid) {
        Map<String, String> paramMap = getMchHead(mchid);
        paramMap.put("product_id", "123");
        paramMap.put("time_stamp", "" + new Date(System.currentTimeMillis()).getTime());
        paramMap.put("sign", WxpayUtil.Sign(paramMap));
        return XmlUtil.Map2Xml(paramMap);
    }


    /**
     * 获取预付款id
     * @param data
     * @return
     */
    public final static String getPrepayId(String data) {
        String prepay_id = "";
        String resultXml = HtmlUtil.postData(WxpayConfig.getUnifiedOrderUrl(), data);
        Map<String, String> returnMap = XmlUtil.parse(resultXml);
        if (returnMap != null && returnMap.get("return_code").equals("SUCCESS") && returnMap.get("result_code").equals("SUCCESS")) {
            prepay_id = returnMap.get("prepay_id");
        }
        return prepay_id;
    }

    /**
     * 获取全部预支付信息
     * @author G/2016年10月27日 上午9:04:42
     * @param data
     * @return
     */
    public final static JSONObject doReceive(String data) {
        String resultXml = HtmlUtil.postData(WxpayConfig.getUnifiedOrderUrl(), data);
        Map<String, String> returnMap = XmlUtil.parse(resultXml);
        return JSONObject.fromObject(returnMap);
    }

    /**
     * 微信财付通退款操作
     * @author G/2016年10月19日 上午9:39:24
     * 请求参数
     * e.g.
     * <xml>
     *    <appid>wx2421b1c4370ec43b</appid>
     *    <mch_id>10000100</mch_id>
     *    <nonce_str>6cefdb308e1e2e8aabd48cf79e546a02</nonce_str>
     *    <op_user_id>10000100</op_user_id>
     *    <out_refund_no>1415701182</out_refund_no>
     *    <out_trade_no>1415757673</out_trade_no>
     *    <refund_fee>1</refund_fee>
     *    <total_fee>1</total_fee>
     *    <transaction_id></transaction_id>
     *    <sign>FE56DD4AA85C0EECA82C35595A69E153</sign>
     * </xml>
     * 通过Https往API post xml数据
     * @param data   要提交的XML数据对象
     * @param syspath    项目根目录，用于加载证书
     * @param mchid   根据商户号加载不同的证书
     * @return
     * 返回参数
     * e.g.
     * <xml>
     * <return_code><![CDATA[SUCCESS]]></return_code>
     * <return_msg><![CDATA[OK]]></return_msg>
     * <appid><![CDATA[wx4339c49fc8614bf9]]></appid>
     * <mch_id><![CDATA[1379881902]]></mch_id>
     * <nonce_str><![CDATA[hdNmwl6lhtmj0CTc]]></nonce_str>
     * <sign><![CDATA[37B5E19DA26333F6F215ACB9A4C9D511]]></sign>
     * <result_code><![CDATA[SUCCESS]]></result_code>
     * <transaction_id><![CDATA[4001142001201610197119599282]]></transaction_id>
     * <out_trade_no><![CDATA[16101815455603649]]></out_trade_no>
     * <out_refund_no><![CDATA[124444446]]></out_refund_no>
     * <refund_id><![CDATA[2001142001201610190530053091]]></refund_id>
     * <refund_channel><![CDATA[]]></refund_channel>
     * <refund_fee>1</refund_fee>
     * <coupon_refund_fee>0</coupon_refund_fee>
     * <total_fee>1</total_fee>
     * <cash_fee>1</cash_fee>
     * <coupon_refund_count>0</coupon_refund_count>
     * <cash_refund_fee>1</cash_refund_fee>
     * </xml>
     *
     *  微信财付通退款操作,并返回结果map结果
     * @author G/2016年10月20日 上午10:34:43
     * @param data
     * @param mchid 根据商户号退款
     * @return 返回退款结果
     *  操作=returnMap.get("return_code").equals("SUCCESS");
     *  结果=returnMap.get("result_code").equals("SUCCESS");
     *  微信收款单号=returnMap.get("transaction_id").equals("4001142001201610197119599282");
     *  微信退款单号=returnMap.get("refund_id").equals("2001142001201610190530053091");
     *  平台收款单号=returnMap.get("out_trade_no").equals("16101815455603649");
     *  平台退款单号=returnMap.get("out_refund_no").equals("124444446");
     *  退款金额=returnMap.get("refund_fee")
     *  订单金额=returnMap.get("total_fee")
     */
    public final static Map<String, String> doRefund(String data, String syspath, String mchid) {
        String url = WxpayConfig.getPayRefundUrl();
        //提交请求
        String resultXml = PostUtil.doPostWithCert(syspath, mchid, url, data);
        return XmlUtil.parse(resultXml);
    }

    /**
     * 查询退款接口
     * @author G/2016年10月20日 上午10:33:08
     * @param data
     * @return 完整的退款结果map 退款状态：通过get("refund_status_0")获得 SUCCESS—退款成功FAIL—退款失败PROCESSING—退款处理中CHANGE—转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，资金回流到商户的现金帐号，需要商户人工干预，通过线下或者财付通转账的方式进行退款。
     * <xml>
     * <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
     * <mch_id><![CDATA[10000100]]></mch_id>
     * <nonce_str><![CDATA[TeqClE3i0mvn3DrK]]></nonce_str>
     * <out_refund_no_0><![CDATA[1415701182]]></out_refund_no_0>
     * <out_trade_no><![CDATA[1415757673]]></out_trade_no>
     * <refund_count>1</refund_count>
     * <refund_fee_0>1</refund_fee_0>
     * <refund_id_0><![CDATA[2008450740201411110000174436]]></refund_id_0>
     * <refund_status_0><![CDATA[PROCESSING]]></refund_status_0>
     * <result_code><![CDATA[SUCCESS]]></result_code>
     * <return_code><![CDATA[SUCCESS]]></return_code>
     * <return_msg><![CDATA[OK]]></return_msg>
     * <sign><![CDATA[1F2841558E233C33ABA71A961D27561C]]></sign>
     * <transaction_id><![CDATA[1008450740201411110005820873]]></transaction_id>
     * </xml>
     */
    public final static Map<String, String> doRefundQuery(String data) {
        String resultXml = HtmlUtil.postData(WxpayConfig.getPayRefundQueryUrl(), data);
        return XmlUtil.parse(resultXml);
    }

    /**
     * 查询退款结果
     * @author G/2016年10月20日 上午10:34:43
     * @param data
     * @return 返回退款结果
     *    退款状态：SUCCESS—退款成功FAIL—退款失败PROCESSING—退款处理中CHANGE—转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，资金回流到商户的现金帐号，需要商户人工干预，通过线下或者财付通转账的方式进行退款。
     */
    public final static String doRefundQueryStatus(String data) {
        String result = "";
        String resultXml = HtmlUtil.postData(WxpayConfig.getPayRefundQueryUrl(), data);
        Map<String, String> returnMap = XmlUtil.parse(resultXml);
        if (returnMap != null && returnMap.get("return_code").equals("SUCCESS") && returnMap.get("result_code").equals("SUCCESS")) {
            result = returnMap.get("refund_status_0");
        }
        return result;
    }

    /**
     * 请求退款的数据
     * @author G/2016年10月19日 上午11:08:56
     * @param transactionID 是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
     * @param outTradeNo 商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no
     * @param outRefundNo 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
     * @param totalFee 订单总金额，单位为分
     * @param refundFee 退款总金额，单位为分     
     * REFUND_SOURCE_UNSETTLED_FUNDS 用企业未结算账户退款(默认)
     * REFUND_SOURCE_RECHARGE_FUNDS 用企业账户余额退款
     *
     * */

    public final static String RefundData(String transactionID, String outTradeNo, String outRefundNo, Double totalFee, Double refundFee, String mchid) {
        Map<String, String> paramMap = getMchHead(mchid);
        //transaction_id是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。
        //paramMap.put("transaction_id",transactionID);
        //根据API给的签名规则进行签名
        paramMap.put("out_trade_no", outTradeNo);
        paramMap.put("out_refund_no", outRefundNo);
        //金额必须为整数  单位为转换为分
        String tfee = "" + Math.round(totalFee * 100);
        paramMap.put("total_fee", tfee);
        String rfee = "" + Math.round(refundFee * 100);
        paramMap.put("refund_fee", rfee);
        paramMap.put("refund_account", "REFUND_SOURCE_RECHARGE_FUNDS");
        paramMap.put("op_user_id", mchid);
        paramMap.put("sign", WxpayUtil.Sign(paramMap));
        return XmlUtil.Map2Xml(paramMap);
    }

    /**
     * 查询退款结果需要的数据
     * 查询参数四选一
     * 微信退款单号	refund_id	String(28)	1217752501201407033233368018 微信生成的退款单号，在申请退款接口有返回
     * 微信收款单号   transaction_id String(32)	1217752501201407033233368018
     *
     * 商户订单号   out_trade_no	String(32)	1217752501201407033233368018
     * 商户退款单号	out_refund_no	String(32)	1217752501201407033233368018
     * @author G/2016年10月20日 上午9:54:04
     * @param mchid 退款时要判断是从哪个商户号退款
     * e.g.
     * <xml>
     *    <appid>wx2421b1c4370ec43b</appid>
     *    <mch_id>10000100</mch_id>
     *    <nonce_str>0b9f35f484df17a732e537c37708d1d0</nonce_str>
     *    <out_refund_no></out_refund_no>
     *    <out_trade_no>1415757673</out_trade_no>
     *    <refund_id></refund_id>
     *    <transaction_id></transaction_id>
     *    <sign>66FFB727015F450D167EF38CCC549521</sign>
     *</xml>
     * @return
     *
     *
     */
    public final static String RefundQueryData(String refundId, String transactionId, String outTradeNo, String outRefundNo, String mchid) {
        Map<String, String> paramMap = getMchHead(mchid);
        //======以下四个参数必选一个=====
        //微信收款单号
        paramMap.put("transaction_id", transactionId);
        //商户收款单号
        paramMap.put("out_trade_no", outTradeNo);
        //商户退款单号
        paramMap.put("out_refund_no", outRefundNo);
        //微信退款单号
        paramMap.put("refund_id", refundId);
        //加签名
        paramMap.put("sign", WxpayUtil.Sign(paramMap));
        return XmlUtil.Map2Xml(paramMap);
    }

    /**
     * 请求收款的数据App
     * @author G/2016年10月19日 上午11:08:56
     * @param outTradeNo 商户系统内部的订单号
     * @param totalFee 订单总金额，单位为分
     */
    public final static String ReceiveAppData(String ordersName, String outTradeNo, Double totalFee, String mchid) {
        Map<String, String> paramMap = getMchHead(mchid);
        //移动应用
        paramMap.put("trade_type", WxpayConfig.TRADETYPE_APP);
        paramMap.put("notify_url", WxpayConfig.WXPAY_RECEIVE_NOTIFY);
        //本机的Ip
        paramMap.put("spbill_create_ip", WxpayUtil.localIp());
        //商户根据自己业务传递的参数 必填
        paramMap.put("product_id", "10001");
        //描述
        paramMap.put("body", ordersName);
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", outTradeNo);

        //金额必须为整数  单位为转换为分
        String tfee = "" + Math.round(totalFee);

        paramMap.put("total_fee", tfee);
        //支付成功后，回调地址
        paramMap.put("sign", WxpayUtil.Sign(paramMap));
        return XmlUtil.Map2Xml(paramMap);
    }

    public final static JSONObject ResignForApp(String prepayId) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        //appid:每个公众号都有一个appid,必填
        paramMap.put("appid", WxpayConfig.APPAPPID);
        //商户号:开通微信支付后分配,必填
        paramMap.put("partnerid", WxpayConfig.APPMCHID);
        //移动应用
        paramMap.put("prepayid", prepayId);
        paramMap.put("package", "Sign=WXPay");
        paramMap.put("noncestr", WxpayUtil.getNonce("nonce", 32));
        paramMap.put("timestamp", WxpayUtil.create_timestamp());
        paramMap.put("key", WxpayConfig.APPKEY);
        paramMap.put("sign", WxpayUtil.Sign(paramMap));
        return JSONObject.fromObject(paramMap);
    }

    /**
     * 请求收款的数据Native
     * @author G/2016年10月19日 上午11:08:56
     * @param outTradeNo 商户系统内部的订单号
     * @param totalFee 订单总金额，单位为分
     */
    public final static String ReceiveNativeData(String ordersName, String outTradeNo, Double totalFee) {
        Map<String, String> paramMap = getMchHead(WxpayConfig.APPMCHID);

        //交易类型
        paramMap.put("trade_type", WxpayConfig.TRADETYPE_NATIVE);
        //本机的Ip
        paramMap.put("spbill_create_ip", WxpayUtil.localIp());
        //商户根据自己业务传递的参数 必填
        paramMap.put("product_id", "10001");
        //描述
        paramMap.put("body", ordersName);
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", outTradeNo);
        //金额必须为整数  单位为转换为分
        String tfee = "" + Math.round(totalFee);
        paramMap.put("total_fee", tfee);
        //支付成功后，回调地址
        paramMap.put("notify_url", WxpayConfig.WXPAY_RECEIVE_NOTIFY);
        paramMap.put("sign", WxpayUtil.Sign(paramMap));
        return XmlUtil.Map2Xml(paramMap);
    }


    /**
     * 请求收款的数据Jsapi
     * @author G/2016年10月19日 上午11:08:56
     * @param outTradeNo 商户系统内部的订单号
     * @param totalFee 订单总金额，单位为分
     */
    public final static String ReceiveJsapiData(String ordersName, String outTradeNo, Double totalFee, String openid) {
        Map<String, String> paramMap = getMchHead(WxpayConfig.GZHMCHID);
        //交易类型
        paramMap.put("trade_type", WxpayConfig.TRADETYPE_JSAPI);
        //openid
        paramMap.put("openid", openid);
        //本机的Ip
        paramMap.put("spbill_create_ip", WxpayUtil.localIp());
        //商户根据自己业务传递的参数 必填
        paramMap.put("product_id", "10001");
        //描述
        paramMap.put("body", ordersName);
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", outTradeNo);
        //金额必须为整数  单位为转换为分
        String tfee = "" + Math.round(totalFee);
        paramMap.put("total_fee", tfee);
        //支付成功后，回调地址
        paramMap.put("notify_url", WxpayConfig.WXPAY_RECEIVE_NOTIFY);
        paramMap.put("sign", WxpayUtil.Sign(paramMap));
        return XmlUtil.Map2Xml(paramMap);
    }

    /**
     *
     * @author G/2016年11月2日 下午5:22:38
     * @param outTradeNo 平台订单号
     * @return
     *
     * <xml>
     * 	  <appid>wx2421b1c4370ec43b</appid>
     * 	  <mch_id>10000100</mch_id>
     *    <nonce_str>4ca93f17ddf3443ceabf72f26d64fe0e</nonce_str>
     *    <out_trade_no>1415983244</out_trade_no>
     *    <sign>59FF1DF214B2D279A0EA7077C54DD95D</sign>
     * </xml>
     */
    public final static String closeOrderData(String outTradeNo, String mchid) {
        Map<String, String> paramMap = getMchHead(mchid);
        //商户 后台的贸易单号
        paramMap.put("out_trade_no", outTradeNo);
        //签名
        paramMap.put("sign", WxpayUtil.Sign(paramMap));
        return XmlUtil.Map2Xml(paramMap);
    }

    /**
     * 关闭预支付订单，以便于重新调起微信支付
     * @author G/2016年11月3日 上午9:47:02
     * @param data
     * @return
     */
    public final static JSONObject doCloseOrder(String data) {
        String resultXml = HtmlUtil.postData(WxpayConfig.getPayCloseOrderUrl(), data);
        Map<String, String> returnMap = XmlUtil.parse(resultXml);
        return JSONObject.fromObject(returnMap);
    }

    /**
     * 获取openid
     * @author G/2016年10月31日 下午3:44:12
     * @param code
     * @return 微信回调结果
     *   {"access_token":"kctWjZAa1e2tMI_BoCOqnkJNRsX2XZFUW5ZZc6S_wmqvcNPPwyqNCPzg3xK-OoZDtlUe3bO8c9CRTmECXnzzqUR7FmadvAsnItEqJLXsp4Y"
     *   ,"expires_in":7200
     *   ,"refresh_token":"3338QNUT1mVBNKQ9g6jASyvDCivaRjTUd0AsQNMP81FZK2V1NgE-ojMRsaUR8Y5ZZu81kvZrnguQXPxK4XRcSSrt6CFmWdODNKdOHnZWUew"
     *   ,"openid":"o3pt-wOZJ2wkOCOMFStANw6wYLaM"
     *   ,"scope":"snsapi_userinfo"
     *   ,"unionid":"oJChXwtaJ5F4qSttcVnuRUGYhDs4"}
     * @throws UnsupportedEncodingException
     */
    public static JSONObject getOpenId(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";

        Map<String, String> params = new HashMap<>();
        params.put("appid", WxpayConfig.GZHAPPID);
        params.put("secret", WxpayConfig.GZHAPPSECRET);
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        String result = SubmitUtil.postMap(url, params);
        return JSONObject.fromObject(result);
    }

    /**
     * <xml>
     <mch_appid>wxe062425f740c30d8</mch_appid> 企业corpid
     <mchid>10000098</mchid>
     <nonce_str>3PG2J4ILTKCH16CQ2502SI8ZNMTM67VS</nonce_str>
     <partner_trade_no>100000982014120919616</partner_trade_no>
     <openid>ohO4Gt7wVPxIT1A9GjFaMYMiZY1s</openid>
     <check_name>FORCE_CHECK</check_name>
     <re_user_name>张三</re_user_name>
     <amount>100</amount> 企业付款金额，单位为分
     <desc>节日快乐!</desc>
     <spbill_create_ip>10.2.3.10</spbill_create_ip>
     <sign>C97BDBACF37622775366F38B629F45E3</sign>
     </xml>
     * @author:G/2017年10月25日
     * @param partner_trade_no
     * @param openid 公众号和开放平台的openid 必须对应公众号和开放平台的 mchid
     * @param amount
     * @param remark
     * @param mchid
     * @return
     */
    public static String transPay(String partner_trade_no, String openid, Double amount, String remark, String mchid) {
        Map<String, String> paramMap = getMchHead(mchid);
        //猎位提现订单号
        paramMap.put("partner_trade_no", partner_trade_no);
        //收款人id
        paramMap.put("openid", openid);
        //不校验真实姓名
        paramMap.put("check_name", "NO_CHECK");
        //金额必须为整数  单位为转换为分
        String tfee = "" + Math.round(amount * 100);
        paramMap.put("amount", tfee);
        //支付成功后，回调地址
        paramMap.put("desc", remark);
        //本机的Ip
        paramMap.put("spbill_create_ip", WxpayUtil.localIp());
        paramMap.put("sign", WxpayUtil.Sign(paramMap));
        return XmlUtil.Map2Xml(paramMap);
    }

    /**
     * 提交提现申请
     * @author:G/2017年10月25日
     * @param data 提现数据
     * @param syspath 系统路径
     * @param mchid 商户号id
     * @return
     */
    private final static String getTransPay(String data, String syspath, String mchid) {
        String url = WxpayConfig.getTransPayUrl();
        return PostUtil.doPostWithCert(syspath,mchid,url,data);
    }

    /**
     * 修改
     * @author:G/2017年10月25日
     * @param
     * @return
     */
    public final static JSONObject doTransPay(String data, String syspath, String mchid) {
        String resultXml = getTransPay(data, syspath, mchid);
        Map<String, String> returnMap = XmlUtil.parse(resultXml);
        return JSONObject.fromObject(returnMap);
    }

    // 获取package的签名包
    public String genPackage(SortedMap<String, String> packageParams)
            throws UnsupportedEncodingException {
        String sign = createSign(packageParams);

        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append(k + "=" + UrlEncode(v) + "&");
        }

        // 去掉最后一个&
        String packageValue = sb.append("sign=" + sign).toString();
//		System.out.println("UrlEncode后 packageValue=" + packageValue);
        return packageValue;
    }

    // 特殊字符处理
    public String UrlEncode(String src) throws UnsupportedEncodingException {
        return URLEncoder.encode(src, "UTF-8").replace("+", "%20");
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public static String createSign(SortedMap<String, String> packageParams) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
//        sb.append("key=" + WxpayConfig.KEY);
        sb.append("key=" + WxpayConfig.APPKEY);
        System.out.println("md5 sb:" + sb);
        String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8")
                .toUpperCase();
        System.out.println("sign签名:" + sign);
        return sign;

    }


    /**
     * 获取沙箱key
     * @param mchId
     * @return
     */
    public final static String getSignKeyData(String appId, String mchId, String key) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("app_id", appId);
        paramMap.put("mch_id", mchId);
        paramMap.put("key", key);
        paramMap.put("nonce_str", WxpayUtil.getNonce("nonce", 16));
        paramMap.put("sign", WxpayUtil.Sign(paramMap));
        System.out.println("paramMap=" + paramMap.toString());

        String data = XmlUtil.Map2Xml(paramMap);

        //获取微信沙箱密钥api
        String wxpay_micropay_getway = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";
        String resultXml = HtmlUtil.postData(wxpay_micropay_getway, data);
//        return XmlUtil.parse(resultXml);
        System.out.println("key=" + resultXml);
        return XmlUtil.parse(resultXml).get("sandbox_signkey").toString();
    }

    /**
     * httpClient 请求获取公钥
     * @return
     * @throws Exception
     */
    @Test
    public void getPublicKey() throws Exception {
        //1.0 拼凑所需要传递的参数 map集合
        SortedMap<String, String> paramMap = new TreeMap<>();
        paramMap.put("mch_id", WxpayConfig.APPMCHID);
        paramMap.put("nonce_str", WxpayUtil.getNonce("nonce", 16));
        paramMap.put("key", WxpayConfig.APPKEY);
        paramMap.put("sign", WxpayUtil.Sign(paramMap));

        //4.0将当前的map结合转化成xml格式
        String xmlData = XmlUtil.Map2Xml(paramMap);
        System.out.println(xmlData);

        //5.0 发送请求到微信请求公钥Api。发送请求是一个方法来的~~注意需要带着证书哦
        String url = "https://fraud.mch.weixin.qq.com/risk/getpublickey";
        String certPath = "D:/workspace/bcb-pay/target/artifacts/bcb_pay_war_exploded";

        String resultXml = PostUtil.doPostWithCert(certPath, WxpayConfig.APPMCHID, url,xmlData);
        //6.0 解析返回的xml数据===》map集合
        System.out.println(XmlUtil.parse(resultXml));
    }

    @Test
    public void test1() throws Exception {
//        String mchId = "1519839251";
//        String subMchId = "1414532102";
//        //真货放上去移动应用appid
//        String appId = "wx4dc8cb7f0eb12288";
//        String key = "9vf65nut0jbkpj7llrzpxmjx68er1et2";
//        String certRootPath = "D:/workspace/ISV_PAY_SVR/src/main/webapp/";
//
//        getSignKeyData(appId,mchId,key);


//		WxpaySubmit.getCode("snsapi_userinfo");

        //把参数转换成XML数据格式
//	    String data = WxpaySubmit.ReceiveAppData("测试商品","180827231545561365921",new Double(1));
//        String data=WxpaySubmit.ReceiveJsapiData("测试商品","1811231545561365923",new Double(0.02),"oHZB8wQ0ECmMcmFlrJwbCygjwEPk");
//        System.out.println(data);

//        JSONObject jo = WxpaySubmit.getReceive(data);
//        System.out.println(jo);

//	    System.out.println(WxpaySubmit.ResignForApp(jo.getString("prepay_id")));
        //wx20171023175434958adde7c10905135015
//	    String data = WxpaySubmit.closeOrderData("1612611575903646","1379881902");
//	    System.out.println(data);
//	    JSONObject ja=WxpaySubmit.doCloseOrder(data);
//	    System.out.println(ja.toString());
//	    
//		JSONObject r=WxpaySubmit.getOpenId("001zAsV82z2p7P0o61X82vnoV82zAsVq");
//	    System.out.println(r.get("openid"));

        //获取二维码链接
//	    String codeUrl = getCodeUrl(data);
//	    System.out.println("codeUrl="+codeUrl);

        //gzh 我的openid
//		String openid="ok06CxIndKZFa1Ia9I2a6ddoBGC4";

        //app的openid
//		String openid="oymYh0mfBGq8emdqEAnJKaGZbgLc";
//    	String data=WxpaySubmit.transPay("124444461",openid,new Double(1),"测试提现",WxpayConfig.APPMCHID);
//    	System.out.println("codeUrl="+data);
//    	String ja=WxpaySubmit.getTransPay(data,"D:/workspace/liewei/WebRoot",WxpayConfig.APPMCHID);
//	    System.out.println(ja.toString());

        //{is_subscribe=N, appid=wx2bfd0b284d2a8981, fee_type=CNY, nonce_str=c43ed6bd0bc2d9c7, out_trade_no=2017102416131015182975, transaction_id=4200000002201710240039119819, trade_type=APP, result_code=SUCCESS, sign=B828242003F1E016815E3937BB6D93D0, mch_id=1490831082, total_fee=1, time_end=20171024161332, openid=oymYh0mfBGq8emdqEAnJKaGZbgLc, bank_type=CFT, return_code=SUCCESS, cash_fee=1}
//    	String data=WxpaySubmit.RefundData("4200000016201710250192350250","","124444460",new Double(0.01),new Double(0.01),"1490831082");
//    	System.out.println("codeUrl="+data);
//        JSONObject result=WxpaySubmit.doRefund("0001201912716147660213199425","F:/myProject/demolist/member/src/main/webapp/","1414532102");
//    	System.out.println("result="+result.toString());
	    
	    


        /*String get_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=APPID" +
                "&secret=SECRET&" +
                "code=CODE&grant_type=authorization_code";
        get_access_token_url = get_access_token_url.replace("APPID", "wx768c398f4aacabe3").replace("SECRET", "673de4d8efe4ed53087d5cba28f151dc").replace("CODE", "001qrhIr12U8Ol0jwXJr1vT0Ir1qrhIc");
        String result = UrlConnUtil.getResult(get_access_token_url);
        JSONObject jsonObject = JSONObject.fromObject(result);
        System.out.println(jsonObject.getString("openid"));*/

    }


    /**
     * 测试子商户收款
     */
    @Test
    public void test2() {


        String data=WxpaySubmit.ReceiveNativeData("test","34234234234234234",new Double(101));
        System.out.println("data=" + data);
        Map<String, String> result = WxpaySubmit.doReceive(data);
        System.out.println("result=" + result.toString());

//	    <xml>
//   <appid>wx2421b1c4370ec43b</appid>
//   <mch_id>10000100</mch_id>
//   <nonce_str>6cefdb308e1e2e8aabd48cf79e546a02</nonce_str>
//   <out_refund_no>1415701182</out_refund_no>
//   <out_trade_no>1415757673</out_trade_no>
//   <refund_fee>1</refund_fee>
//   <total_fee>1</total_fee>
//   <transaction_id>4006252001201705123297353072</transaction_id>
//   <sign>FE56DD4AA85C0EECA82C35595A69E153</sign>
//</xml>
    }


    @Test
    public void test3() {
        String key = WxpaySubmit.getSignKeyData(WxpayConfig.APPAPPID, WxpayConfig.APPMCHID, WxpayConfig.APPKEY);
        System.out.println("result=" + key);
    }
}
