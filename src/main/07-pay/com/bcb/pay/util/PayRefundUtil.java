package com.bcb.pay.util;

import com.bcb.alipay.util.AliRefundUtil;
import com.bcb.pay.dto.RefundOrderDto;
import com.bcb.wxpay.util.service.WxRefundUtil;

import java.util.Map;

/**
 * @Author G.
 * @Date 2019/12/11 0011 上午 10:18
 */
public class PayRefundUtil implements PayRefund {

    PayRefund payRefund;

    public PayRefundUtil(String payChannel) {
        //如果是微信支付
        if (payChannel.equals(PayChannelType.wxpay.name())) {
            this.payRefund = new WxRefundUtil();
        }else{
            //支付宝
            this.payRefund = new AliRefundUtil();
        }
    }

    public PayRefundUtil(String payChannel,RefundOrderDto refundOrderDto) {
        //如果是微信支付
        if (payChannel.equals(PayChannelType.wxpay.name())) {
            this.payRefund = new WxRefundUtil(refundOrderDto);
        }else{
            //支付宝
            this.payRefund = new AliRefundUtil(refundOrderDto);
        }
    }

    @Override
    public Map refund(String payChannelAccount,String payOrderNo,String refundOrderNo) {
        return this.payRefund.refund(payChannelAccount,payOrderNo,refundOrderNo);
    }

    @Override
    public Map query(String payChannelAccount, String payOrderNo, String refundOrderNo) {
        return this.payRefund.query(payChannelAccount,payOrderNo,refundOrderNo);
    }
}
