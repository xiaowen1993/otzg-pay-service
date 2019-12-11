package com.bcb.alipay.util;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.bcb.pay.dto.RefundOrderDto;
import com.bcb.pay.util.PayRefund;
import com.bcb.util.FastJsonUtil;
import com.bcb.util.FuncUtil;
import com.bcb.util.RespTips;

import java.util.Map;
import java.util.TreeMap;

/**
 * @Author G.
 * @Date 2019/12/11 0011 上午 10:35
 */
public class AliRefundUtil implements PayRefund {
    RefundOrderDto refundOrderDto;

    public AliRefundUtil(RefundOrderDto refundOrderDto) {
        this.refundOrderDto = refundOrderDto;
    }

    public AliRefundUtil() {
    }

    /**
     * 退款
     * 订单支付时传入的商户订单号,不能和 trade_no同时为空。
     * @return {"alipay_trade_refund_response":{"code":"10000","msg":"Success","
     * buyer_logon_id":"vvn***@sandbox.com","buyer_user_id":"2088102169133529",
     * "fund_change":"N","gmt_refund_pay":"2019-01-09 15:01:46",
     * "out_trade_no":"191018154556136588110",
     * "refund_detail_item_list":[{"amount":"1.00","fund_channel":"ALIPAYACCOUNT"}],
     * "refund_fee":"1.00","send_back_fee":"1.00","trade_no":"2019010922001433520200663215"},
     * "sign":"fsRfZ8cHsiK+NizXnvE1RweP1rDiQQe30EMj7B5mqk0V/VayuE5xXj4kVbRQsiNr4iMROnbbjKjL3iPraH9gDqCzWKn2HD4dkiXhe24MZA+9uBNpBXf0RRg1eNcT89eSGnRAfxcvuovHIvkTca3VpZ3IfzXu8/WtLe7tKWjBG9HlWEILMo2x2PhiP2jDKmv5HYu4humw6xMiMzn30JYi1kx8A5EnpY5uW9Ys1pfZpmLsOqsfgzppfpNMr8zRJaDtvbGH/bz4Z5nTVe9+BiBK+3qJbPPPWeQfPVmKXm0DJnUkFcU1ViL283yd2WUUJPHdQ77GEHqf9U/SVKACGIFRiw=="}
     */

    @Override
    public Map refund(String payChannelAccount, String payOrderNo,String refundOrderNo) {
        try{
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(payOrderNo);
            model.setRefundAmount(FuncUtil.getDoubleScale(this.refundOrderDto.getAmount()).toString());

            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            //第三方应用授权
            request.putOtherTextParam("app_auth_token", payChannelAccount);
            request.setBizModel(model);

            AlipayTradeRefundResponse response = getAlipayClient().execute(request);
            if (response.isSuccess()) {
                //如果成功应返回一个结构体payChannelNo
                Map map = new TreeMap();
                map.put("payChannelNo",response.getTradeNo());
                return FastJsonUtil.get(true, response.getCode(), response.getMsg(), map);
            } else {
                return FastJsonUtil.get(false, response.getCode(), response.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
            return FastJsonUtil.get(false, RespTips.ERROR_CODE.code, e.getMessage());
        }
    }

    AlipayClient getAlipayClient() {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.alipay_gateway,
                AlipayConfig.app_id,
                AlipayConfig.app_private_key,
                "json",
                AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);
        return alipayClient;
    }

    /**
     * 退款查询
     * @return {"alipay_trade_fastpay_refund_query_response":{"code":"10000","msg":"Success","out_request_no":"191018154556136588113","out_trade_no":"191018154556136588113","refund_amount":"1.00","total_amount":"1.00","trade_no":"2019010922001433520200660680"},"sign":"j8OcS61wlzGJ0Zb7wUMplSTmtRDqet/vuJUUDPxCefyMZjnBWbI3tNiN4QeVwhHT1ugP0H2IdNGOJueswboEqT20Cy5fqrZj1WnzxpT1YpCGbL+eAo69A5oZLI0W4hbZnepbR0MpxnOA70A7vD3SSouPn34LQqMF9VX/cbp14zgWNDil0oEKhaoYkKX3ggpyYInE0QpMxFh4hmN5zYcQH088p4yOny8L315AN066imFBrgTLgmyu/48l1Vn89RzOfXTc88XJ7tyvHxmooR21icar5zBhGT6+8PnBcMN7d734bB+d/qZE7pKKbL8+7weMBPxpdLtx1mCbBMqE3VSyFg=="}
     */
    @Override
    public Map query(String payChannelAccount, String payOrderNo, String refundOrderNo) {
        try{
            AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
            //订单支付时传入的商户订单号,和支付宝交易号不能同时为空。 trade_no,out_trade_no如果同时存在优先取trade_no
            model.setOutTradeNo(payOrderNo);
            //请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号
            model.setOutRequestNo(refundOrderNo);

            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
            //第三方应用授权
            request.putOtherTextParam("app_auth_token", payChannelAccount);
            request.setBizModel(model);

            AlipayTradeFastpayRefundQueryResponse response = getAlipayClient().execute(request);
            if (response.isSuccess()) {
                return FastJsonUtil.get(true, response.getCode(), response.getMsg());
            } else {
                return FastJsonUtil.get(false, response.getCode(), response.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
            return FastJsonUtil.get(false,RespTips.ERROR_CODE.code, e.getMessage());
        }
    }
}