package com.bcb.pay.entity;

import com.bcb.pay.util.PayAccountType;
import com.bcb.util.DateUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * 平台基本账户
 */
@Entity
public class PayAccount implements Serializable {

    @Id
    Long id;

    //平台会员id
    @Column(name = "member_id", length = 32, unique = true)
    String memberId;

    //平台商户id
    @Column(name = "unit_id", length = 32, unique = true)
    String unitId;

    //账户名称
    @Column(name = "name", nullable = false, length = 64, unique = true)
    String name;

    //开户人手机号
    @Column(name = "mobile_phone", length = 13)
    String mobilePhone;

    //联系人
    @Column(name = "contact", nullable = false, length = 64)
    String contact;

    //支付密码
    @Column(name = "password", length = 32)
    String password;


    //账户类型{0:平台账户,1:会员账户,2:单位账户}
    @Column(name = "type", nullable = false, length = 1)
    Integer type = 1;

    //账户余额
    @Column(name = "balance", nullable = false, precision = 8, scale = 2)
    BigDecimal balance;

    //账户状态{-1:删除,0:冻结,1:正常}
    @Column(name = "status", nullable = false, length = 1)
    Integer status = 1;

    //创建时间
    @Column(name = "create_time", nullable = false, length = 19)
    Date createTime;

    //修改时间
    @Column(name = "update_time", nullable = false, length = 19)
    Date updateTime;


    //==========================账户收益积分信用============================//
    //账户收益余额
    @Column(name = "profit_balance", nullable = false, precision = 8, scale = 2)
    BigDecimal profitBalance;

    //账户收益(历史总收益,只加不减)
    @Column(name = "profit_total", nullable = false, precision = 8, scale = 2)
    BigDecimal profitTotal;

    //奖励积分
    @Column(name = "bonus_points", nullable = false)
    Long bonusPoints = 0l;

    //信用等级
    @Column(name = "credit_rating", nullable = false, length = 2)
    Integer creditRating = 0;


    //==========================交押金============================//
    //押金
    @Column(name = "deposit", nullable = false, precision = 5, scale = 2)
    BigDecimal deposit;

    //缴纳押金时间
    @Column(name = "deposit_time", length = 19)
    Date depositTime;


    public PayAccount() {
    }

    public PayAccount(String id,
                      String name,
                      String contact,
                      String mobilePhone,
                      Integer type) {
        if (!PayAccountType.has(type)) {
            return;
        } else if (PayAccountType.MEMBER.index.equals(type)) {
            //创建个人账户
            this.memberId = id;
        } else if (PayAccountType.UNIT.index.equals(type)) {
            //创建单位账户
            this.unitId = id;
        }

        this.name = name;
        this.mobilePhone = mobilePhone;
        this.contact = contact;
        this.type = type;
        this.balance = new BigDecimal("0");
        this.profitBalance = new BigDecimal("0");
        this.profitTotal = new BigDecimal("0");
        this.deposit = new BigDecimal("0");
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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Map getNameJson() {
        Map jo = new TreeMap();
        jo.put("id", this.id);
        if (null != this.unitId) {
            jo.put("unitId", this.unitId);
        } else if (null != this.memberId) {
            jo.put("memberId", this.memberId);
        }
        jo.put("name", this.getName());
        jo.put("balance", this.getBalance());
        jo.put("type", this.getType());
        jo.put("status", this.getStatus());
        return jo;
    }

    //押金
    public Map getDepositeJson() {
        Map jo = getNameJson();
        jo.put("deposit", Optional.ofNullable(this.deposit).orElse(BigDecimal.ZERO));
        jo.put("depositTime", Optional.ofNullable(this.depositTime).map(depositTime -> DateUtil.dateTime2Str(depositTime)).orElse(""));
        return jo;
    }

    //收益和积分
    public Map getProfitJson() {
        Map jo = getNameJson();
        jo.put("profitBalance", Optional.ofNullable(this.profitBalance).orElse(BigDecimal.ZERO));
        jo.put("profitTotal", Optional.ofNullable(this.profitTotal).orElse(BigDecimal.ZERO));

        jo.put("bonusPoints", Optional.ofNullable(this.bonusPoints).orElse(0L));
        jo.put("creditRating", Optional.ofNullable(this.creditRating).orElse(0));
        return jo;
    }

    public boolean isUseable() {
        if (this.status < 1) {
            return false;
        }
        return true;
    }

}
