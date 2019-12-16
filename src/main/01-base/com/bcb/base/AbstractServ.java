package com.bcb.base;

import com.bcb.util.DateUtil;
import com.bcb.util.FuncUtil;
import com.dangdang.ddframe.rdb.sharding.keygen.KeyGenerator;
import org.redisson.Redisson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

public abstract class AbstractServ extends BaseBean{

    @Autowired
    KeyGenerator keyGenerator;
    @Autowired
    protected Redisson redisson;

    protected Long getId(){
        return keyGenerator.generateKey().longValue();
    }

    //自动生成订单号
    protected String getPayOrderNo(String unitId) {
        return DateUtil.yearMonthDayTimeShort() + FuncUtil.getRandInt(0001, 9999) + Math.abs(unitId.hashCode());
    }

    protected final void rollBack(){
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}
