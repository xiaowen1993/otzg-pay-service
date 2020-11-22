package com.otzg.pay.util;

import com.otzg.pay.dto.PayOrderDto;

import java.util.Map;

/**
 * 收款单校验器
 *
 * @Author G.
 * @Date 2019/12/10 0010 下午 1:55
 */
public class PayOrderDtoCheckImpl implements PayOrderDtoCheck {
    BasePayOrderDtoCheckUtil payOrderDtoCheckUtil;

    public PayOrderDtoCheckImpl(PayOrderDto payOrderDto) {
        if(payOrderDto.getPayChannel().equals("alipay")){
            payOrderDtoCheckUtil = new AliPayOrderDtoCheckUtil(payOrderDto);
        }else
        if(payOrderDto.getPayChannel().equals("wxpay")){
            payOrderDtoCheckUtil = new WxPayOrderDtoCheckUtil(payOrderDto);
        }else{
            payOrderDtoCheckUtil = null;
        }
    }

    @Override
    public void check() {
        payOrderDtoCheckUtil.check();
    }

    @Override
    public PayOrderDto get() {
        return payOrderDtoCheckUtil.get();
    }

    @Override
    public Map getMsg() {
        return payOrderDtoCheckUtil.getMsg();
    }
}
