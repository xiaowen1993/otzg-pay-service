package com.bcb.pay.dao;

import com.bcb.base.AbstractDao;
import com.bcb.pay.entity.Account;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

/**
 * @author G./2018/6/29 11:56
 */
public interface AccountDao extends AbstractDao<Account,Long> {

    /**
     * 账户余额
     */
    @Query("select ac.balance from Account ac where ac.id = ?1 and ac.status=1")
    BigDecimal findProfitAmount(Long payerAccountId);

    /** 
     * 获取账户状态
     */
    @Query("select ac.status from Account ac where ac.id = ?1")
    Integer findAccountStatusByMember(Long accountId);

    //返回平台账户
    @Query("select ac from Account ac where ac.type = 0")
    Account findPlatform();

}
