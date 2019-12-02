package com.bcb.pay.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 支付渠道收款账户
 * 1.每个基本账户(PayAccount)可以有多个渠道收款账户(PayChannelAccount)
 * 2.系统支持的收款账户,商户如果创建都可以进行对应的收款
 */
@Entity
public class PayChannelAccount implements Serializable {

    @Id
    Long id;

    //基本账户
    @ManyToOne
    @JoinColumn(name = "pay_account_id",nullable = false)
    PayAccount payAccount;

    //支付渠道类型{}
    @Column(name = "pay_channel",length = 16)
    String payChannel;

    //账户余额付款
    @Column(name = "balance", nullable = false, precision = 8, scale = 2)
    BigDecimal balance;

    //账户冻结余额
    @Column(name = "freeze_balance", nullable = false, precision = 8, scale = 2)
    BigDecimal freezeBalance;

    //账户状态{-1:删除,0:冻结,1:正常}
    @Column(name = "status", nullable = false, length = 1)
    Integer status = 1;

    //创建时间
    @Column(name = "create_time", nullable = false, length = 19)
    Date createTime;

    //修改时间
    @Column(name = "update_time", nullable = false, length = 19)
    Date updateTime;




    //支付渠道付款账号(支付宝buyer_id,微信opend_id)
    @Column(name = "pay_channel_id",length = 32)
    String payChannelId;

    /**
     * 支付渠道账号
     *
     * 支付宝对应 pid (支付宝商户号 登录账号包含邮箱号和手机号
     * 微信对应 mchid (公众号|app 商户号))
     */
    @Column(name = "pay_channel_account",length = 64)
    String payChannelAccount;

    //支付渠道账号名称
    @Column(name = "pay_channel_account_name",length = 16)
    String payChannelAccountName;


    public PayChannelAccount() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PayAccount getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(PayAccount payAccount) {
        this.payAccount = payAccount;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getPayChannelId() {
        return payChannelId;
    }

    public void setPayChannelId(String payChannelId) {
        this.payChannelId = payChannelId;
    }

    public String getPayChannelAccount() {
        return payChannelAccount;
    }

    public void setPayChannelAccount(String payChannelAccount) {
        this.payChannelAccount = payChannelAccount;
    }

    public String getPayChannelAccountName() {
        return payChannelAccountName;
    }

    public void setPayChannelAccountName(String payChannelAccountName) {
        this.payChannelAccountName = payChannelAccountName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getFreezeBalance() {
        return freezeBalance;
    }

    public void setFreezeBalance(BigDecimal freezeBalance) {
        this.freezeBalance = freezeBalance;
    }
}
