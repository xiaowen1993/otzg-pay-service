package com.bcb.pay.util;

import com.bcb.alipay.util.AlipayUtil;
import com.bcb.pay.entity.PayOrder;
import com.bcb.wxpay.util.service.WxpayUtil;

import java.util.Map;

/**
 * @Author G.
 * @Date 2019/12/10 0010 下午 4:40
 */
public class PayQueryUtil implements PayQuery {
    PayQuery payQuery;

    public PayQueryUtil(PayOrder payOrder) {
        if (payOrder.getPayChannel().equals(PayChannelType.wxpay.name())) {
            this.payQuery = new WxpayUtil();
        }else {
            this.payQuery = new AlipayUtil();
        }
    }

    @Override
    public Map query(String payChannelAccount, String payOrderNo) {
        return this.payQuery.query(payChannelAccount,payOrderNo);
    }
}
