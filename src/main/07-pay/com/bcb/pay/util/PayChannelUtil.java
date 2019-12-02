package com.bcb.pay.util;

import com.bcb.pay.dto.PayOrderDto;

import java.util.Map;

/**
 * @Author G.
 * @Date 2019/11/26 0026 上午 9:18
 */
public interface PayChannelUtil {
    //调支付
    Map pay(String payChannelAccount, PayOrderDto payOrderDto);
}
