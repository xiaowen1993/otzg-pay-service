package com.bcb.base;

import com.dangdang.ddframe.rdb.sharding.keygen.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

public abstract class AbstractServ extends BaseBean{

    @Autowired
    KeyGenerator keyGenerator;

    protected Long getId(){
        return keyGenerator.generateKey().longValue();
    }
    protected final void rollBack(){
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}
