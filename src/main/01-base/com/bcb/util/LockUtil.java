package com.bcb.util;

import com.bcb.base.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author G./2019/5/25 9:13
 */
@Service
public class LockUtil {
    @Autowired
    private RedisService redisService;
    //保存锁支付账户的头部
    private final static String accountLockPreStr = "yt_ac_tk";
    //保存锁支付账户的头部
    private final static String payLockPreStr = "yt_pay";

    private final static long second = 300l;

    //所有redis加锁的基本方法
    private boolean lock(String key, String value, long second){
        try{
            boolean r = redisService.setIfAbsent(key,value, second);
            if (r) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    //所有redis解锁的基本方法
    private boolean unLock(String key){
        try {
            redisService.del(key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //================================具体实现方法======================================================/

    //账号加锁
    public boolean lockAccount(String token) {
        return lock(accountLockPreStr+token,token,second);
    }

    public boolean unLockAccount(String token) {
        return unLock(accountLockPreStr+ token);
    }



    /**
     * 对多个账户同时加锁
     *
     * @param ids
     * @return
     */
    public boolean lockAccountByIds(Set<Long> ids) {
        int total = ids.size();
        for (Long id:ids) {
            boolean r = lockAccountById(id);
            if (r) {
                total = total - 1;
            }
        }
        if (total < 1) {
            return true;
        } else {
            return false;
        }
    }


    //给一个账户加锁
    public boolean lockAccountById(Long id) {
        return lock(payLockPreStr + id, id.toString(), second);
    }


    //多个账户同时解锁
    public boolean unLockAccountByIds(Set<Long> ids) {
        int total = ids.size();
        for (Long id:ids) {
            boolean r = unLockAccountById(id);
            if (r) {
                total--;
            }
        }
        if (total < 1) {
            return true;
        } else {
            return false;
        }
    }

    //单个账户解锁
    public boolean unLockAccountById(Long id) {
        return unLock(payLockPreStr + id);
    }

    public static void main(String[] args) {
    }

}
