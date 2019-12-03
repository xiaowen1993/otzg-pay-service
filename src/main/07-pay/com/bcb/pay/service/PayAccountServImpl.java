package com.bcb.pay.service;

import com.bcb.base.AbstractServ;
import com.bcb.base.Finder;
import com.bcb.pay.dao.PayAccountDao;
import com.bcb.pay.dao.PayAccountLogDao;
import com.bcb.pay.dao.PayChannelAccountDao;
import com.bcb.pay.entity.PayAccount;
import com.bcb.pay.entity.PayAccountLog;
import com.bcb.pay.entity.PayChannelAccount;
import com.bcb.pay.util.PayAccountType;
import com.bcb.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
public class PayAccountServImpl extends AbstractServ implements PayAccountServ {

    @Autowired
    PayAccountDao payAccountDao;
    @Autowired
    PayChannelAccountDao payChannelAccountDao;
    @Autowired
    PayAccountLogDao payAccountLogDao;


    @Override
    @Transactional
    public int create(String unitId,String name,String contact,String mobilePhone) {
        try{
            PayAccount payAccount = new PayAccount(unitId,name,contact,mobilePhone, PayAccountType.UNIT.index);
            payAccount.setId(getId());
            payAccountDao.save(payAccount);
            return 0;
        }catch (Exception e){
            return 1;
        }
    }



    @Override
    public PayAccount findById(Long id) {
        return null;
    }
    @Override
    public PayAccount findByMemberId(String memberId) {
        return null;
    }

    @Override
    public PayAccount findByUnitId(String unitId) {
        Optional<PayAccount> op = payAccountDao.findByUnitId(unitId);
        return op.orElse(null);
    }

    @Override
    public PayAccount findPlatform() {
        return null;
    }

    @Override
    public boolean checkPayAccountByMember(String memberId) {
        return false;
    }

    @Override
    public boolean checkPayAccountUnit(String unitId) {
        return false;
    }

    @Override
    public boolean checkPayAccountAmountByMember(String payerMemberId, BigDecimal profitAmount) {
        return false;
    }

    @Override
    public boolean checkPayAccountAmountByUnit(String payerUnitId, BigDecimal profitAmount) {
        return false;
    }

    @Override
    public int savePayPassword(String password, String unitId, String memberId) {
        return 0;
    }

    @Override
    public int checkPayPassword(String password, String unitId, String memberId) {
        return 0;
    }

    @Override
    public Map findPayAccount(Finder finder, String accountId) {
        return null;
    }

    @Override
    public Map findPayAccountByAdmin(Finder finder, String memberId, String unitId) {
        return null;
    }

    @Override
    public boolean testLock() {
        return false;
    }

    @Override
    public Map testWatch(Integer k) {
        return null;
    }
}