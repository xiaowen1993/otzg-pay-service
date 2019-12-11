package com.bcb.pay.util;

import java.util.Map;

/**
 * @Author G.
 * @Date 2019/12/10 0010 下午 4:35
 */
public interface PayQuery {

    //String payChannelNo,String payerId,String payeeId
    Map query(String payChannelAccount, String payOrderNo);
}
