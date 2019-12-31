package com.bcb.pay.util;

import com.bcb.pay.dto.PayOrderDto;

import java.util.Map;

/**
 * @Author G.
 * @Date 2019/12/30 0030 下午 6:23
 */
public interface PayOrderDtoCheckUtil {
    void check();
    PayOrderDto get();
    Map getMsg();
}
