package com.bcb.alipay.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.bcb.log.util.LogUtil;
import com.bcb.util.JsonUtil;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AlipaySubmit {

    //======================================支付宝条码收款=======================================================/

    /**
     * 当面付(条码收款)
     *
     * @param appAuthToken       三方授权，商户授权令牌
     * @param outTradeNo         平台业务定单号
     * @param authCode           扫码获取的用户条码
     * @param subject            订单标题
     * @param totalAmount        付款总金额
     * @param discountableAmount 参与优惠计算的金额
     * @return {
     * {"alipay_trade_pay_response":
     * {"code":"10003",
     * "msg":" order success pay inprocess",
     * "buyer_logon_id":"vvn***@sandbox.com",
     * "buyer_pay_amount":"0.00",
     * "buyer_user_id":"2088102169133529",
     * "buyer_user_type":"PRIVATE",
     * "invoice_amount":"0.00",
     * "out_trade_no":"191018154556136588118",
     * "point_amount":"0.00",
     * "receipt_amount":"0.00",
     * "total_amount":"1.00",
     * "trade_no":"2019011022001433520200667601"},
     * "sign":"Io//wg+Q85Qg+408yv1WGsIAFrtIRno3eURYv2J6KrXwlUPXfVkF1N40luV6KgsvMBPArg44kwC8jyIJUlIZUN3tsOhrdtcXdC1u0tQHY9U4qZ2Z0qW2S8rr8fMZTWV7Py4mViroNOoPIckFjICSYaM7nhDl46uOuXpmFTJcOd9SsXEMS8aDQK3oicWvLtItwkxQdSLEQ01Ommg3XiwhdvtDuJRbA7bDEDlZnbeGeXzfW4WBExYHJMmVvetXZcsAAhJN0R5olx9PCmabLDzKe4FiHSxORDqZtvwnr37f2xRizEW37N4vbS6/kyuNL0NtTpoVBBq5RKmd3RLtuDjHDg=="}
     */
    public static JSONObject barCodPay(String appAuthToken,
                                       String authCode,
                                       String outTradeNo,
                                       String subject,
                                       String totalAmount,
                                       String discountableAmount,
                                       String storeId) throws AlipayApiException {

        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setOutTradeNo(outTradeNo);
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        //支付宝中的付款码
        model.setAuthCode(authCode);
        model.setScene("bar_code");
        model.setStoreId(storeId);

        /**
         * 系统商编号
         * 该参数作为系统商返佣数据提取的依据，请填写系统商签约协议的PID
         */
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId(AlipayConfig.pid);
        model.setExtendParams(extendParams);

        AlipayTradePayRequest request = new AlipayTradePayRequest();
        //第三方应用授权
        request.putOtherTextParam("app_auth_token", appAuthToken);

        request.setBizModel(model);
        //绑定异步通知接口
        request.setNotifyUrl(LogUtil.getServUrl() + AlipayConfig.notify_url);

        AlipayTradePayResponse response = getAlipayClient().execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功=>"+response.getBody());
            //如果支付结果 response.code=10000时表示支付成功
            //如果支付结果 response.code=10003时表示等待用户付款

            JSONObject jo = new JSONObject();
            jo.put("tradeNo", response.getTradeNo());
            jo.put("buyerUserId", response.getBuyerUserId());
            return JsonUtil.get(true, response.getCode(), response.getMsg(), jo);
        } else {
            System.out.println("调用失败");
            return JsonUtil.get(false, response.getCode(), response.getMsg());
        }
    }

    /**
     * 查询支付结果
     *
     * @param outTradeNo
     * @param tradeNo
     * @return {"alipay_trade_query_response":{"code":"10000","msg":"Success","
     * buyer_logon_id":"vvn***@sandbox.com","buyer_pay_amount":"0.00",
     * "buyer_user_id":"2088102169133529","buyer_user_type":"PRIVATE",
     * "invoice_amount":"0.00","out_trade_no":"191018154556136588113",
     * "point_amount":"0.00","receipt_amount":"0.00",
     * "send_pay_date":"2019-01-09 17:00:09","
     * total_amount":"1.00","trade_no":"2019010922001433520200660680",
     * "trade_status":"TRADE_CLOSED"},
     * "sign":"IEtVhRN7tqDsEcWvaLW9zfwx1sR2Fxm/ivQfyXVIiEih+nNq3Ven1hLE/s86Ha0+ovOk29IhC1BrUqz7Ry/vciD+qtesVzEkZ4ypxXK+sgh0iLKFoS9jPoYQZdkkb57Jj0W3xokmwocNO1GOjUf+bMht4ZblncDaMCKK4UNnBRhTyHr/AMx8lv1qYq1aJ30s+Sr4+o9vrkW+f6o358LyrcPuDTYkh2cQ/J9+KR5G6oimRVS7kami7w8ssSI/U1qZrnBcLkllAvDCI26pbQqUO6K/4diEFu3ewmotmIa5miciGLJgrc8yBiIJ7wv3bP9+qTENUmoo+sOl1AkTFM/YRA=="}
     */
    public static JSONObject payQuery (String outTradeNo,
                                      String tradeNo) throws AlipayApiException {

        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(tradeNo);

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizModel(model);

        AlipayTradeQueryResponse response = getAlipayClient().execute(request);
        LogUtil.print("查询支付结果" + response.getBody());
        if (response.isSuccess()) {
            return JsonUtil.get(true, response.getCode(),"支付成功");
        } else {
            return JsonUtil.get(false, response.getCode(), "支付失败",response.getBody());
        }
    }


    //=========================================支付宝条码收款结束====================================================/


    //====================================支付宝授权业务====================================================//

    /**
     * 获取授权链接
     * <p>
     * ->客户登录支付宝
     * ->客户点击授权链接
     * ->发起授权
     * ->支付宝回调授权地址返回授权码 app_id 和 app_auth_code
     * ->通过app_auth_code换取app_auth_token
     * <p>
     * <p>
     * 支付宝文档如下:
     * ===================
     * 链接地址:https://docs.open.alipay.com/20160728150111277227/intro
     * <p>
     * 第四步：获取app_auth_code
     * 商户授权成功后，pc或者钱包客户端会跳转至开发者定义的回调页面（即redirect_uri参数对应的url），在回调页面请求中会带上当次授权的授权码app_auth_code和开发者的app_id，示例如下：
     * http://example.com/doc/toAuthPage.html?app_id=2015101400446982&app_auth_code=ca34ea491e7146cc87d25fca24c4cD11
     * 第五步：使用app_auth_code换取app_auth_token
     * 接口名称：alipay.open.auth.token.app
     * 开发者通过app_auth_code可以换取app_auth_token、授权商户的userId以及授权商户AppId。
     * 注意:应用授权的app_auth_code唯一的；app_auth_code使用一次后失效，一天（从生成app_auth_code开始的24小时）未被使用自动过期； app_auth_token永久有效。
     * 请求参数说明
     * 参数	参数名称	类型	必填	描述	范例
     * grant_type	授权类型	String	是	如果使用app_auth_code换取token，则为authorization_code，如果使用refresh_token换取新的token，则为refresh_token	authorization_code
     * code	授权码	String	否	与refresh_token二选一，用户对应用授权后得到，即第一步中开发者获取到的app_auth_code值	bf67d8d5ed754af297f72cc482287X62
     * refresh_token	刷新令牌	String	否	与code二选一，可为空，刷新令牌时使用	201510BB0c409dd5758b4d939d4008a525463X62
     * 接口请求示例: 请先阅读SDK接入说明
     * AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
     * AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
     * request.setBizContent("{" +
     * "    \"grant_type\":\"authorization_code\"," +
     * "    \"code\":\"1cc19911172e4f8aaa509c8fb5d12F56\"" +
     * "  }");
     * AlipayOpenAuthTokenAppResponse response = alipayClient.execute(request);
     * 同步响应参数说明
     * <p>
     * 参数	参数名称	类型	必填	描述	范例
     * app_auth_token	商户授权令牌	String	是	通过该令牌来帮助商户发起请求，完成业务	201510BBaabdb44d8fd04607abf8d5931ec75D84
     * user_id	授权商户的ID	String	是	授权者的PID	2088011177545623
     * auth_app_id	授权商户的AppId	String	是	授权商户的AppId（如果有服务窗，则为服务窗的AppId）	2013111800001989
     * expires_in	令牌有效期	Number	是	此字段已作废，默认永久有效	31536000
     * re_expires_in	刷新令牌有效期	Number	是	此字段已作废，默认永久有效	32140800
     * app_refresh_token	刷新令牌时使用	String	是	刷新令牌后，我们会保证老的app_auth_token从刷新开始10分钟内可继续使用，请及时替换为最新token	201510BB09dece3ea7654531b66bf9f97cdceE67
     * 同步响应结果示例
     * <p>
     * {
     * "alipay_open_auth_token_app_response": {
     * "code": "10000",
     * "msg": "Success",
     * "app_auth_token": "201510BBb507dc9f5efe41a0b98ae22f01519X62",
     * "app_refresh_token": "201510BB0c409dd5758b4d939d4008a525463X62",
     * "auth_app_id": "2013111800001989",
     * "expires_in": 31536000,
     * "re_expires_in": 32140800,
     * "user_id": "2088011177545623"
     * },
     * "sign": "TR5xJkWX65vRjwnNNic5n228DFuXGFOCW4isWxx5iLN8EuHoU2OTOeh1SOzRredhnJ6G9eOXFMxHWl7066KQqtyxVq2PvW9jm94QOuvx3TZu7yFcEhiGvAuDSZXcZ0sw4TyQU9+/cvo0JKt4m1M91/Quq+QLOf+NSwJWaiJFZ9k="
     * }
     * 注意：
     * <p>
     * 在授权过程中，建议在拼接授权url的时候，开发者可增加自己的一个自定义信息，便于知道是哪个商户授权。
     * 开发者代替商户发起请求时请务必带上app_auth_token，否则支付宝将认为是本应用替自己发起的请求。请注意app_auth_token是POST请求参数，不是biz_content的子参数；在SDK中带上app_auth_token代码示例
     * request.putOtherTextParam("app_auth_token", "201611BB888ae9acd6e44fec9940d09201abfE16");
     * 开发者代替商户发起请求时，POST公共请求参数中的app_id应填写开发者的app_id；如果业务参数biz_content中需要AppId，则应填写商户的AppId。
     */

    //获取授权链接
    public static String getAuthTokenUrl(String domainUrl) {
        return String.format(AlipayConfig.auth_url, AlipayConfig.app_id, domainUrl + AlipayConfig.auth_notify_url);
    }

    /**
     * 换取token
     * 1、openAuthTokenApp
     * 2、appRefreshToken
     * 3、userId
     * 4、authAppId
     */
    public static JSONObject openAuthTokenApp(String appAuthCode) throws AlipayApiException {
        AlipayOpenAuthTokenAppModel model = new AlipayOpenAuthTokenAppModel();
        model.setCode(appAuthCode);
        model.setGrantType("authorization_code");

        AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
        request.setBizModel(model);

        AlipayOpenAuthTokenAppResponse response = getAlipayClient().execute(request);
        if (response.isSuccess()) {
            JSONObject jo = new JSONObject();
            jo.put("appAuthToken", response.getAppAuthToken());
            jo.put("appRefreshToken", response.getAppRefreshToken());
            jo.put("userId", response.getUserId());
            jo.put("authAppId", response.getAuthAppId());
            return JsonUtil.get(true, response.getCode(), jo);
        } else {
            return JsonUtil.get(false, response.getCode());
        }
    }


    /**
     * 校验异步通知
     *
     * @param
     * @return
     * @author:G/2017年9月14日
     */
    public static boolean notifyCheck(HttpServletRequest request) {
        try {
            //获取支付宝POST过来反馈信息
            Map<String, String> params = new HashMap<String, String>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用。
                //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                params.put(name, valueStr);
            }
            //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
            return AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, "RSA2");
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 退款
     * 订单支付时传入的商户订单号,不能和 trade_no同时为空。
     *
     * @param outTradeNo
     * @param refundAmount
     * @param storeId
     * @return {"alipay_trade_refund_response":{"code":"10000","msg":"Success","
     * buyer_logon_id":"vvn***@sandbox.com","buyer_user_id":"2088102169133529",
     * "fund_change":"N","gmt_refund_pay":"2019-01-09 15:01:46",
     * "out_trade_no":"191018154556136588110",
     * "refund_detail_item_list":[{"amount":"1.00","fund_channel":"ALIPAYACCOUNT"}],
     * "refund_fee":"1.00","send_back_fee":"1.00","trade_no":"2019010922001433520200663215"},
     * "sign":"fsRfZ8cHsiK+NizXnvE1RweP1rDiQQe30EMj7B5mqk0V/VayuE5xXj4kVbRQsiNr4iMROnbbjKjL3iPraH9gDqCzWKn2HD4dkiXhe24MZA+9uBNpBXf0RRg1eNcT89eSGnRAfxcvuovHIvkTca3VpZ3IfzXu8/WtLe7tKWjBG9HlWEILMo2x2PhiP2jDKmv5HYu4humw6xMiMzn30JYi1kx8A5EnpY5uW9Ys1pfZpmLsOqsfgzppfpNMr8zRJaDtvbGH/bz4Z5nTVe9+BiBK+3qJbPPPWeQfPVmKXm0DJnUkFcU1ViL283yd2WUUJPHdQ77GEHqf9U/SVKACGIFRiw=="}
     */
    public static JSONObject refund(String app_id,
                                    String app_private_key,
                                    String alipay_public_key,

                                    String outTradeNo,
                                    String refundAmount,
                                    String storeId) throws AlipayApiException {

        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(outTradeNo);
        model.setRefundAmount(refundAmount);
        model.setStoreId(storeId);

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizModel(model);

        AlipayTradeRefundResponse response = getAlipayClient().execute(request);
        if (response.isSuccess()) {
            return JsonUtil.get(true, response.getCode(), response.getMsg(), response.getTradeNo());
        } else {
            return JsonUtil.get(false, response.getCode(), response.getMsg());
        }
    }


    /**
     * 退款查询
     *
     * @param outTradeNo 平台单号
     * @param tradeNo    收款单号
     * @return {"alipay_trade_fastpay_refund_query_response":{"code":"10000","msg":"Success","out_request_no":"191018154556136588113","out_trade_no":"191018154556136588113","refund_amount":"1.00","total_amount":"1.00","trade_no":"2019010922001433520200660680"},"sign":"j8OcS61wlzGJ0Zb7wUMplSTmtRDqet/vuJUUDPxCefyMZjnBWbI3tNiN4QeVwhHT1ugP0H2IdNGOJueswboEqT20Cy5fqrZj1WnzxpT1YpCGbL+eAo69A5oZLI0W4hbZnepbR0MpxnOA70A7vD3SSouPn34LQqMF9VX/cbp14zgWNDil0oEKhaoYkKX3ggpyYInE0QpMxFh4hmN5zYcQH088p4yOny8L315AN066imFBrgTLgmyu/48l1Vn89RzOfXTc88XJ7tyvHxmooR21icar5zBhGT6+8PnBcMN7d734bB+d/qZE7pKKbL8+7weMBPxpdLtx1mCbBMqE3VSyFg=="}
     */
    public static JSONObject refundQuery(String outTradeNo,
                                         String tradeNo) throws AlipayApiException {

        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setOutTradeNo(outTradeNo);
        //请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号
        model.setOutRequestNo(outTradeNo);
        //收款单号
        model.setTradeNo(tradeNo);

        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        request.setBizModel(model);

        AlipayTradeFastpayRefundQueryResponse response = getAlipayClient().execute(request);
        if (response.isSuccess()) {
            return JsonUtil.get(true, response.getCode(), response.getMsg());
        } else {
            return JsonUtil.get(false, response.getCode(), response.getMsg());
        }
    }

    private static AlipayClient getAlipayClient() {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.alipay_gateway,
                AlipayConfig.app_id,
                AlipayConfig.app_private_key,
                "json",
                AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);
        return alipayClient;
    }


    public static void main(String[] args) throws AlipayApiException {

        //收款码
        String authCode = "287749032239774976";
//        String appAuthToken ="";
        //收款
        JSONObject jo = barCodPay(null,authCode, "2019033011111111115", "测试收款", "0.01", null, "store_1");
        System.out.println("result=" + jo);
//        System.out.println("result=" + getAuthTokenUrl("https:/www.fangshangqu.cn"));


        //退款
//		JSONObject jo = refund(alipayConfig,"191018154556136588113","1","store_1");
// 		JSONObject jo = refundQuery(null,null,null,"191018154556136588113","");

        //查询
//		JSONObject jo = payQuery("234234",null);
//		System.out.println("result="+jo);

    }
}
