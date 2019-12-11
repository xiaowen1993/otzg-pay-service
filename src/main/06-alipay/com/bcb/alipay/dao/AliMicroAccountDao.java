package com.bcb.alipay.dao;

import com.bcb.alipay.entity.AliMicroAccount;
import com.bcb.base.AbstractDao;

import java.util.Optional;

/**
 * @Author G.
 * @Date 2019/12/5 0005 下午 5:44
 */
public interface AliMicroAccountDao extends AbstractDao<AliMicroAccount,Long> {
    Optional<AliMicroAccount> findByUnitId(String unitId);
}
