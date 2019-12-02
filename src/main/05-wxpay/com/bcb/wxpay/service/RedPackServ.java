package com.bcb.wxpay.service;

import com.bcb.wxpay.entity.WxRedPack;

/**
 * 微信红包相关
 *
 * @author G
 */
public interface RedPackServ {

    /**
     * 商户发红包
     */
    int saveRedPack(WxRedPack wxRedPack);

}
