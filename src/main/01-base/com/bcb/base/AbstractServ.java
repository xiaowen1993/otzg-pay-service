package com.bcb.base;

import org.springframework.transaction.interceptor.TransactionAspectSupport;

public abstract class AbstractServ extends BaseBean{
    protected final void rollBack(){
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}
