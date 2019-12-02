package com.bcb.wxpay.dao;

import com.bcb.base.AbstractDao;
import com.bcb.wxpay.entity.WxMicroAccountLog;

import java.util.Optional;

/**
 * @Author G.
 * @Date 2019/11/30 0030 下午 4:33
 */
public interface WxMicroAccountLogDao extends AbstractDao<WxMicroAccountLog,Long> {
    Optional<WxMicroAccountLog> findByUnitId(String unitId);

    Optional<WxMicroAccountLog> findByBusinessCode(String businessCode);
}
