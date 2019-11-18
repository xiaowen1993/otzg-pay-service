//package com.bcb.pay.entity;
//
//import com.bcb.member.entity.Member;
//import com.bcb.pay.util.PayChannelType;
//import com.bcb.util.DateUtil;
//import com.bcb.util.FuncUtil;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.*;
//
///**
// * 记录账户流水信息
// * 关联一个类
// * 1、交易账户类 Account
// * 2、支付订单类 PayOrders (只关联订单号)
// */
//
//@Entity
//@Table(name = "account_log")
//public class AccountLog implements Serializable{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "create_time",  nullable = false, length = 19)
//    private Date createTime;
//
//    //=============支付渠道start===============//
//    /**
//     * 支付渠道,如果通过支付渠道支付需要保存支付渠道信息
//     * 1、用户在第一次使用系统支付时，会绑定一个默认支付渠道账户
//     * 2、用户可以重新绑定默认的支付渠道账户
//     * 3、默认支付渠道作为用户提现等操作使用
//     * @author G/2018/5/28 11:06
//     */
//    //支付渠道id(微信对应openid,支付宝对应16位的用户id)
//    @Column(name = "pay_channel_id",length = 32)
//    private String payChannelId;
//
//    //支付渠道账号(对应支付宝账号(支付宝登录账号包含邮箱号和手机号),微信对用mchid(公众号商户号|app商户号))
//    @Column(name = "pay_channel_account",length = 64)
//    private String payChannelAccount;
//
//    //支付渠道{aplipay:支付宝,wxpay:微信支付,qxpay:象过河支付}
//    @Column(name = "pay_channel",length = 16)
//    private String payChannel= PayChannelType.alipay.name();
//
//    //支付渠道单号
//    @Column(name = "pay_channel_no",length = 128)
//    private String payChannelNo;
//
//    //支付订单号
//    @Column(name = "pay_orders_no",length = 64)
//    private String payOrdersNo;
//
//    //发生额
//    @Column(name = "amount", nullable = false, precision = 8, scale = 2)
//    private BigDecimal amount;
//
//    //发生后余额
//    @Column(name = "balance",precision = 8, scale = 2)
//    private BigDecimal balance;
//
//    //交易描述信息
//    @Column(name = "details")
//    private String details;
//
//    /**
//     * 交易记录绑定一个账户
//     * @author G/2018/5/28 10:22
//     */
//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name="account_id")
//    private Account account;
//
//    /**
//     * 商户交易流水绑定的手机号
//     * 外部监听的商户流水唯一标识
//     * 有此标识记录到的金额不会汇总到本平台账户内
//     * 下面的profitType=20
//     */
//    @Column(name="member_account",length = 32)
//    private String memberAccount;
//
//    /**
//     * 收益类型
//     * {0:充值(+),1:年费收入(+),2:提现(-),3:返利收益(+),10:绑定账户(+),11:余额返利支出(-),12:年费返利支出(-),20:商户收益流水(+)}
//     */
//    @Column(name = "profit_type", nullable = false,length = 2)
//    private Integer profitType=0;
//
//    //是否已读
//    @Column(name = "is_read",nullable = false,length = 1)
//    private Integer isRead=0;
//
//    //=============支付渠道end===============//
//    public AccountLog() {
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getDetails() {
//        return details;
//    }
//
//    public void setDetails(String details) {
//        this.details = details;
//    }
//
//    public String getPayOrdersNo() {
//        return payOrdersNo;
//    }
//
//    public void setPayOrdersNo(String payOrdersNo) {
//        this.payOrdersNo = payOrdersNo;
//    }
//
//    public BigDecimal getAmount() {
//        return amount;
//    }
//
//    public void setAmount(BigDecimal amount) {
//        this.amount = amount;
//    }
//
//    public BigDecimal getBalance() {
//        return balance;
//    }
//
//    public void setBalance(BigDecimal balance) {
//        this.balance = balance;
//    }
//
//    public Account getAccount() {
//        return account;
//    }
//
//    public void setAccount(Account account) {
//        this.account = account;
//    }
//
//    public String getMemberAccount() {
//        return memberAccount;
//    }
//
//    public void setMemberAccount(String memberAccount) {
//        this.memberAccount = memberAccount;
//    }
//
//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }
//
//    public String getPayChannelId() {
//        return payChannelId;
//    }
//
//    public void setPayChannelId(String payChannelId) {
//        this.payChannelId = payChannelId;
//    }
//
//    public String getPayChannelAccount() {
//        return payChannelAccount;
//    }
//
//    public void setPayChannelAccount(String payChannelAccount) {
//        this.payChannelAccount = payChannelAccount;
//    }
//
//    public String getPayChannel() {
//        return payChannel;
//    }
//
//    public void setPayChannel(String payChannel) {
//        this.payChannel = payChannel;
//    }
//
//    public String getPayChannelNo() {
//        return payChannelNo;
//    }
//
//    public void setPayChannelNo(String payChannelNo) {
//        this.payChannelNo = payChannelNo;
//    }
//
//    public Integer getProfitType() {
//        return profitType;
//    }
//
//    public void setProfitType(Integer profitType) {
//        this.profitType = profitType;
//    }
//
//    public Integer getIsRead() {
//        return isRead;
//    }
//
//    public void setIsRead(Integer isRead) {
//        this.isRead = isRead;
//    }
//
//    public Map getBaseJson(){
//        Map<String,Object> jo=new TreeMap<>();
//        jo.put("id",this.getId());
//        jo.put("details",Optional.ofNullable(this.getDetails()).orElse(""));
//        jo.put("payOrdersNo",Optional.ofNullable(this.getPayOrdersNo()).orElse(""));
//        jo.put("payChannel",Optional.ofNullable(this.getPayChannel()).orElse(""));
//        jo.put("payChannelNo",Optional.ofNullable(this.getPayChannelNo()).orElse(""));
//        jo.put("amount", Optional.ofNullable(this.getAmount()).map(amount->FuncUtil.getBigDecimalScale(amount)).orElse(FuncUtil.ZERO));
////        jo.put("balance",Optional.ofNullable(this.getBalance()).map(balance->FuncUtil.getBigDecimalScale(balance)).orElse(FuncUtil.ZERO));
//
//        jo.put("createTime",DateUtil.dateTime2Str(this.getCreateTime()));
//        jo.put("profitType",this.getProfitType());
//        jo.put("profitTypeTips",getProfitTypeTips(this.getProfitType()));
//
////        jo.put("memberId",this.getMemberId());
////        jo.put("isRead",this.getIsRead());
//        return jo;
//    }
//
//    public Map getJson(){
//        Map jo=getBaseJson();
//        jo.put("member", Optional.ofNullable(this.account).map(account1 -> Optional.ofNullable(account1.getMember()).map(Member::getBaseJson).orElse(new HashMap())).orElse(new HashMap()));
//        return jo;
//    }
//
//    //{0:充值(+),1:年费收入(+),2:提现(-),3:返利收益(+),10:绑定账户(+),11:余额返利支出(-),12:年费返利支出(-),20:商户收益流水(+)}
//    private String getProfitTypeTips(Integer type){
//        if(type.equals(0)){
//            return "充值";
//        }else if(type.equals(1)){
//            return "年费收入";              //平台年费收入
//        }else if(type.equals(2)){           //会员提现支出
//            return "提现";
//        }else if(type.equals(3)){           //平台或会员年费返利或余额返利收益
//            return "返利收益";
//        }else if(type.equals(10)){
//            return "绑定账户";
//        }else if(type.equals(11)) {
//            return "余额返利支出";
//        }else if(type.equals(12)){           //平台年费返利支出
//            return "年费返利支出";
//        }else if(type.equals(20)) {
//            return "商户收益流水";
//        }else{
//            return "";
//        }
//    }
//}
