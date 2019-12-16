package com.bcb.wxpay.dao;

import com.bcb.base.AbstractDao;
import com.bcb.wxpay.entity.WxRedPack;

import java.util.Optional;

/**
 * @Author G.
 * @Date 2019/12/13 0013 下午 2:34
 */
public interface WxRedPackDao extends AbstractDao<WxRedPack,Long> {
    Optional<WxRedPack> findByRedPackOrderNo(String redpackOrderNo);
}
