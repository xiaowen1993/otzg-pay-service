package com.bcb.pay.util;

import com.bcb.pay.dto.PayOrderDto;

import java.util.Map;

/**
 * 策略模式校验器
 *
 * @Author G.
 * @Date 2019/12/10 0010 下午 1:55
 */
public class PayOrderDtoCheckImpl implements PayOrderDtoCheckUtil {
    PayOrderDtoCheck payOrderDtoCheck;

    public PayOrderDtoCheckImpl(PayOrderDto payOrderDto) {
        if(payOrderDto.getPayChannel().equals("alipay")){
            payOrderDtoCheck = new PayOrderDtoAlipayCheck(payOrderDto);
        }else
        if(payOrderDto.getPayChannel().equals("wxpay")){
            payOrderDtoCheck = new PayOrderDtoWxpayCheck(payOrderDto);
        }else{
            payOrderDtoCheck = null;
        }
    }

    @Override
    public void check() {
        payOrderDtoCheck.check();
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
