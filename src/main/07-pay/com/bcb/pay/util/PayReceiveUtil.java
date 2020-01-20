package com.bcb.pay.util;

import com.bcb.alipay.util.AlipayUtil;
import com.bcb.pay.dto.PayOrderDto;
import com.bcb.wxpay.util.service.WxpayUtil;

import java.util.Map;

/**
 * 策略模式收款方法
 * @Author G.
 * @Date 2019/12/10 0010 下午 2:22
 */
public class PayReceiveUtil implements PayReceive {

    PayReceive payReceive;

    public PayReceiveUtil(PayOrderDto payOrderDto) {
        //如果是微信支付
        if (payOrderDto.getPayChannel().equals(PayChannelType.wxpay.name())) {
            this.payReceive = new WxpayUtil();
        }else if (payOrderDto.getPayChannel().equals(PayChannelType.alipay.name())){
            //如果是支付宝
            this.payReceive = new AlipayUtil();
        }else{
            //TODO:邮储
        }
    }

    @Override
    public PayResult pay(String payChannelAccount, String payOrderNo, PayOrderDto payOrderDto) {
        return this.payReceive.pay(payChannelAccount, payOrderNo, payOrderDto);
    }
}
