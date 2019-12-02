package com.bcb.wxpay.dao;

import com.bcb.base.AbstractDao;
import com.bcb.wxpay.entity.WxMicroAccount;

import java.util.Optional;

/**
 * @Author G.
 * @Date 2019/11/30 0030 上午 9:33
 */
public interface WxMicroAccountDao extends AbstractDao<WxMicroAccount,Long> {
    Optional<WxMicroAccount> findByUnitId(String unitId);
}
