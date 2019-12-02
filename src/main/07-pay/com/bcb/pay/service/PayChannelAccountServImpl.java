package com.bcb.pay.service;

import com.bcb.base.AbstractServ;
import com.bcb.pay.dao.PayAccountLogDao;
import com.bcb.pay.dao.PayChannelAccountDao;
import com.bcb.pay.entity.PayAccountLog;
import com.bcb.pay.entity.PayChannelAccount;
import com.bcb.util.DateUtil;
import com.bcb.util.LockUtil;
import com.bcb.util.RespTips;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @Author G.
 * @Date 2019/11/27 0027 下午 1:47
 */
@Service
public class PayChannelAccountServImpl extends AbstractServ implements PayChannelAccountServ {
    @Autowired
    PayChannelAccountDao payChannelAccountDao;
    @Autowired
    PayAccountLogDao payAccountLogDao;
    @Autowired
    LockUtil lockUtil;

    @Override
    @Transactional
    public int addPayAccount(String unitId,
                              String payOrderNo,
                              String subject,
                              String payChannel,
                              String payChannelNo,
                              BigDecimal amount) {

        Optional<PayChannelAccount> op = payChannelAccountDao.findByUnitAndPayChannel(unitId,payChannel);
        PayChannelAccount payChannelAccount = op.get();
        payChannelAccount.setBalance(payChannelAccount.getBalance().add(amount));
        payChannelAccount.setUpdateTime(DateUtil.now());
        payChannelAccountDao.save(payChannelAccount);

        PayAccountLog payAccountLog = new PayAccountLog();
        payAccountLog.setId(getId());
        payAccountLog.setBalanceBefore(payChannelAccount.getBalance().subtract(amount));
        payAccountLog.setBalanceAfter(payChannelAccount.getBalance());
        payAccountLog.setCreateTime(DateUtil.now());
        payAccountLog.setDetails(subject);
        payAccountLog.setUnitId(unitId);
        payAccountLog.setPayChannel(payChannel);
        payAccountLog.setPayChannelNo(payChannelNo);
        payAccountLog.setPayOrderNo(payOrderNo);
        payAccountLog.setIsRead(0);
        payAccountLog.setProfitType(0);
        payAccountLogDao.save(payAccountLog);
        return 0;
    }

    @Override
    public PayChannelAccount findByAccountAndPayChannel(Long payAccountId, String payChannel) {
        Optional<PayChannelAccount> op = payChannelAccountDao.findByPayAccountIdAndPayChannel(payAccountId,payChannel);
        if(!op.isPresent()){
            return null;
        }
        return op.get();
    }

    @Override
    public int savePayChannelAccount(String payChannelId, String payChannelAccount, String payChannelAccountName, String payChannel, String memberId) {
        return 0;
    }




    @Override
    public boolean substract(PayChannelAccount payChannelAccount,String unitId,String payChannel,String payOrderNo, String subject, String payChannelNo, BigDecimal amount) {

        //冻结金额删除
        if(!unFreezeBalance(payChannelAccount,amount, false)){
            return false;
        }

        PayAccountLog payAccountLog = new PayAccountLog();
        payAccountLog.setId(getId());
        //账户操作前的余额等于当前的余额加上冻结的金额
        payAccountLog.setBalanceBefore(payChannelAccount.getBalance().add(amount));
        //操作后端余额
        payAccountLog.setBalanceAfter(payChannelAccount.getBalance());
        payAccountLog.setCreateTime(DateUtil.now());
        payAccountLog.setDetails(subject);
        payAccountLog.setUnitId(unitId);
        payAccountLog.setPayChannel(payChannel);
        payAccountLog.setPayChannelNo(payChannelNo);
        payAccountLog.setPayOrderNo(payOrderNo);
        payAccountLog.setIsRead(0);
        payAccountLog.setProfitType(0);
        payAccountLogDao.save(payAccountLog);
        return true;
    }

    /**
     * 两阶段事务动账之前需要冻结付款金额
     * 1.余额-冻结金额
     * 2.增加冻结金额
     *
     * 要求：
     * 开启全局账户锁
     * @param freezeAmount
     * @return
     */
    @Override
    @Transactional
    public boolean freezeBalance(PayChannelAccount payChannelAccount, BigDecimal freezeAmount) {
        try{
            //==================================创建锁===============================================
            boolean lock = lockUtil.lockAccountById(payChannelAccount.getId());
            if(!lock){
                PT("账户加锁失败 payChannelAccountId="+payChannelAccount.getId());
                return false;
            }
            P("账户加锁成功");

            //余额减掉冻结金额
            payChannelAccount.setBalance(payChannelAccount.getBalance().subtract(freezeAmount));
            payChannelAccount.setFreezeBalance(freezeAmount);
            payChannelAccount.setUpdateTime(DateUtil.now());
            payChannelAccountDao.save(payChannelAccount);
            P("冻结账户余额成功");
            return true;
        }catch (Exception e){
            rollBack();
            PT("冻结账户余额失败=>"+e.toString());
            e.printStackTrace();
            return false;
        }finally {
            P("账户解锁成功");
            lockUtil.unLockAccountById(payChannelAccount.getId());
        }
    }


    /**
     * 解冻冻结金额
     * 1.删除冻结金额
     * 2.如果tag为true,余额增加。
     *
     * @param freezeAmount
     * @return
     */
    @Override
    @Transactional
    public boolean unFreezeBalance(PayChannelAccount payChannelAccount, BigDecimal freezeAmount, boolean tag) {
        try{
            //==================================创建锁===============================================
            boolean lock = lockUtil.lockAccountById(payChannelAccount.getId());
            if(!lock){
                PT("账户加锁失败 payChannelAccountId="+payChannelAccount.getId());
                return false;
            }
            P("账户加锁成功");

            //减掉冻结金额
            payChannelAccount.setFreezeBalance(payChannelAccount.getFreezeBalance().subtract(freezeAmount));

            //如果tag为真表示要返还到余额
            if(tag){
                payChannelAccount.setBalance(payChannelAccount.getBalance().add(freezeAmount));
            }

            payChannelAccount.setUpdateTime(DateUtil.now());
            payChannelAccountDao.save(payChannelAccount);
            P("冻结账户余额成功");
            return true;
        }catch (Exception e){
            rollBack();
            PT("冻结账户余额失败=>"+e.toString());
            e.printStackTrace();
            return false;
        }finally {
            P("账户解锁成功");
            lockUtil.unLockAccountById(payChannelAccount.getId());
        }
    }
}
