package com.bcb.pay.util;

import com.bcb.pay.dto.PayOrderDto;

import java.util.Map;

/**
 * 策略模式校验器
 *
 * @Author G.
 * @Date 2019/12/10 0010 下午 1:55
 */
public class PayOrderDtoCheckUtil extends PayOrderDtoCheck{
    PayOrderDtoCheck payOrderDtoCheck;

    public PayOrderDtoCheckUtil(PayOrderDto payOrderDto) {
        super(payOrderDto);

        if(payOrderDto.getPayChannel().equals("alipay")){
            payOrderDtoCheck = new PayOrderDtoAlipayCheck(payOrderDto);
        }
        if(payOrderDto.getPayChannel().equals("wxpay")){
            payOrderDtoCheck = new PayOrderDtoWxpayCheck(payOrderDto);
        }
    }

    @Override
    public PayOrderDto get() {
        return payOrderDtoCheck.get();
    }

    @Override
    public Map getMsg() {
        return payOrderDtoCheck.getMsg();
    }
}
