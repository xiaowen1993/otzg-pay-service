package com.bcb.pay.entity;

import com.bcb.util.DateUtil;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * 基本账户
 *
 * 一、账户的分类
 * 1、平台账户
 * 2、会员账户
 */
@Entity
@Table(name = "account")
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //账户余额
    @Column(name = "balance", nullable = false, precision = 8, scale = 2)
    private BigDecimal balance = new BigDecimal("0");

    //账户类型{0:平台,1:会员}
    @Column(name = "type", nullable = false, length = 1)
    private Integer type=1;

    //账户状态{-1:删除,0:冻结,1:正常}
    @Column(name = "status",nullable = false,length = 1)
    private Integer status=1;

    //创建时间
    @Column(name = "create_time", nullable = false, length = 19)
    private Date createTime;

    //修改时间
    @Column(name = "update_time", nullable = false, length = 19)
    private Date updateTime;

    //支付密码
    @Column(name = "password",length = 32)
    private String password;

    //会员id
    @Column(name = "member",length = 32)
    private String member;

    //==========================账户收益积分信用============================//
    //账户收益余额
    @Column(name = "profit_balance", nullable = false, precision = 8, scale = 2)
    private BigDecimal profitBalance = new BigDecimal("0");

    //账户收益(历史总收益,只加不减)
    @Column(name = "profit_total", nullable = false, precision = 8, scale = 2)
    private BigDecimal profitTotal = new BigDecimal("0");

    //奖励积分
    @Column(name = "bonus_points",nullable = false)
    private Long bonusPoints=0l;

    //信用等级
    @Column(name = "credit_rating",nullable = false,length = 2)
    private Integer creditRating=0;
    //==========================交押金============================//
    //押金
    @Column(name = "deposit", nullable = false, precision = 5, scale = 2)
    private BigDecimal deposit = new BigDecimal("0");

    //缴纳押金时间
    @Column(name = "deposit_time",length = 19)
    private Date depositTime;

    //======================支付渠道=======================//
    //支付渠道付款账号(支付宝buyer_id,微信opend_id)
    @Column(name = "pay_channel_id",length = 32)
    private String payChannelId;

    //支付渠道账号(对应支付宝账号(支付宝登录账号包含邮箱号和手机号),微信对用mchid(公众号商户号|app商户号))
    @Column(name = "pay_channel_account",length = 64)
    private String payChannelAccount;

    //支付渠道账号名称
    @Column(name = "pay_channel_account_name",length = 16)
    private String payChannelAccountName;

    //默认支付渠道{aplipay:支付宝,wxpay:微信支付,}
    @Column(name = "pay_channel",length = 16)
    private String payChannel= "alipay";

    public Account() {
    }
    public Account(String memberId) {
        this.setMember(memberId);
        this.setCreateTime(DateUtil.now());
        this.setUpdateTime(DateUtil.now());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getProfitBalance() {
        return profitBalance;
    }

    public void setProfitBalance(BigDecimal profitBalance) {
        this.profitBalance = profitBalance;
    }

    public BigDecimal getProfitTotal() {
        return profitTotal;
    }

    public void setProfitTotal(BigDecimal profitTotal) {
        this.profitTotal = profitTotal;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(Long bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    public Integer getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(Integer creditRating) {
        this.creditRating = creditRating;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public Date getDepositTime() {
        return depositTime;
    }

    public void setDepositTime(Date depositTime) {
        this.depositTime = depositTime;
    }

    public Map getNameJson() {
        Map jo = new TreeMap();
        jo.put("id", this.id);
        jo.put("balance", this.balance);
        jo.put("type",this.type);
        jo.put("status",this.status);
        return jo;
    }

    public Map getJson(){
        Map jo = getNameJson();
        jo.put("payChannel",Optional.ofNullable(this.payChannel).orElse(""));
        jo.put("payChannelId",Optional.ofNullable(this.payChannelId).orElse(""));
        jo.put("payChannelAccount",Optional.ofNullable(this.payChannelAccount).orElse(""));
        jo.put("payChannelAccountName",Optional.ofNullable(this.payChannelAccountName).orElse(""));

        jo.put("member", this.member);
        jo.put("createTime",DateUtil.dateTime2Str(this.createTime));
        jo.put("updateTime",DateUtil.dateTime2Str(this.updateTime));

        return jo;
    }

    //押金
    public Map getDepositeJson(){
        Map jo = getNameJson();
        jo.put("deposit",Optional.ofNullable(this.deposit).orElse(BigDecimal.ZERO));
        jo.put("depositTime",Optional.ofNullable(this.depositTime).map(depositTime->DateUtil.dateTime2Str(depositTime)).orElse(""));
        return jo;
    }

    //收益和积分
    public Map getProfitJson(){
        Map jo = getNameJson();
        jo.put("profitBalance",Optional.ofNullable(this.profitBalance).orElse(BigDecimal.ZERO));
        jo.put("profitTotal",Optional.ofNullable(this.profitTotal).orElse(BigDecimal.ZERO));

        jo.put("bonusPoints",Optional.ofNullable(this.bonusPoints).orElse(0L));
        jo.put("creditRating",Optional.ofNullable(this.creditRating).orElse(0));
        return jo;
    }

}
