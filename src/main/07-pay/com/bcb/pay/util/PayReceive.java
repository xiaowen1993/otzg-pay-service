package com.bcb.pay.util;

import com.bcb.pay.dto.PayOrderDto;

import java.util.Map;

/**
 * @Author G.
 * @Date 2019/12/10 0010 下午 2:37
 */
public interface PayReceive {
    Map pay(String payChannelAccount, String payOrderNo, PayOrderDto payOrderDto);
}
