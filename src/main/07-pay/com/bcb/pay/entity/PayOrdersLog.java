//package com.bcb.pay.entity;
//
//import com.bcb.member.entity.Member;
//import com.bcb.pay.util.PayChannelType;
//import com.bcb.util.DateUtil;
//import net.sf.json.JSONObject;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.Date;
//
///**
// *
// * 支付订单记录
// * @author G./2018/5/29 10:50
// */
//
//@Entity
//@Table(name = "pay_orders_log")
//public class PayOrdersLog implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    //创建时间
//    @Column(name = "create_time", nullable = false, length = 19)
//    private Date createTime;
//
//    //支付订单编号
//    @Column(name = "orders_no",length = 64,nullable = false)
//    private String ordersNo;
//
//    //支付发生额
//    @Column(name = "amount", precision = 8, scale = 2,nullable = false)
//    private BigDecimal amount;
//
//
//    //=============================支付渠道=============================//
//    /**
//     * 支付
//     * @author G/2018/6/4 11:47
//     */
//
//    //支付渠道{aplipay:支付宝,wxpay:微信支付,xghpay:象过河支付}
//    @Column(name = "pay_channel",nullable = false,length=8)
//    private String payChannel= PayChannelType.alipay.name();
//
//    //支付渠道单号
//    @Column(name = "pay_channel_no",length = 64)
//    private String payChannelNo;
//
//    /**
//     * 付款人支付渠道账号,
//     * 1、支付渠道id或账号(对应支付宝账号(支付宝登录账号包含邮箱号或手机号),
//     * 2、微信对应openId , mchid(公众号商户号|app商户号))
//     * @author G/2018/6/4 11:51
//     * @param null
//     */
//    @Column(name = "payer_channel_account",length = 64)
//    private String payerChannelAccount;
//
//    /**
//     * 收款人支付渠道账号
//     * 1、支付渠道id或账号(对应支付宝账号(支付宝登录账号包含邮箱号或手机号),
//     * 2、微信对应mchid(公众号商户号|app商户号))
//     * @author G/2018/6/4 11:51
//     * @param null
//     */
//    @Column(name = "payee_channel_account",length = 64)
//    private String payeeChannelAccount;
//
//    @Lob
//    @Basic(fetch = FetchType.LAZY)
//    @Column(name = "pay_channel_details",columnDefinition="TEXT")
//    private String payChannelDetails;
//
//    //========================支付单位信息==============================//
//
//    /**
//     * 付款单位
//     * @author G/2018/6/4 14:03
//     * @param null
//     */
//    @ManyToOne
//    @JoinColumn(name="payer_member_id")
//    private Member payerMember;
//
//    //收款单位
//    @ManyToOne
//    @JoinColumn(name="payee_member_id")
//    private Member payeeMember;
//
//    //========================支付状态==============================//
//
//    //支付状态{-1:失败,1:成功}
//    @Column(name = "status",nullable = false,length = 1)
//    private Integer status;
//
//    public PayOrdersLog() {
//    }
//
//    public PayOrdersLog(PayOrders payOrders,
//                        String payChannel,
//                        String payChannelNo,
//                        String payChannelDetails,
//                        Integer status) {
//        this.payChannel = payChannel;
//        this.payChannelNo = payChannelNo;
//        this.createTime = DateUtil.now();
//        this.ordersNo = payOrders.getOrdersNo();
//        this.amount = payOrders.getAmount();
//        this.payChannelDetails = payChannelDetails;
//        this.status = status;
//    }
//
//    public PayOrdersLog(PayOrders payOrders,
//                        String payChannelNo,
//                        String payerChannelAccount,
//                        String payeeChannelAccount,
//                        String payChannelDetails,
//                        int status) {
//        this.createTime = DateUtil.now();
//        this.amount = payOrders.getAmount();
//        this.payChannel = payOrders.getPayChannel();
//        this.payChannelNo = payChannelNo;
//        this.payerChannelAccount = payerChannelAccount;
//        this.payeeChannelAccount = payeeChannelAccount;
//        this.payChannelDetails = payChannelDetails;
//        this.status = status;
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
//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }
//
//    public String getOrdersNo() {
//        return ordersNo;
//    }
//
//    public void setOrdersNo(String ordersNo) {
//        this.ordersNo = ordersNo;
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
//    public String getPayerChannelAccount() {
//        return payerChannelAccount;
//    }
//
//    public void setPayerChannelAccount(String payerChannelAccount) {
//        this.payerChannelAccount = payerChannelAccount;
//    }
//
//    public String getPayeeChannelAccount() {
//        return payeeChannelAccount;
//    }
//
//    public void setPayeeChannelAccount(String payeeChannelAccount) {
//        this.payeeChannelAccount = payeeChannelAccount;
//    }
//
//    public String getPayChannelDetails() {
//        return payChannelDetails;
//    }
//
//    public void setPayChannelDetails(String payChannelDetails) {
//        this.payChannelDetails = payChannelDetails;
//    }
//
//    public Member getPayerMember() {
//        return payerMember;
//    }
//
//    public void setPayerMember(Member payerMember) {
//        this.payerMember = payerMember;
//    }
//
//    public Member getPayeeMember() {
//        return payeeMember;
//    }
//
//    public void setPayeeMember(Member payeeMember) {
//        this.payeeMember = payeeMember;
//    }
//
//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }
//
//    public JSONObject getJson(){
//        JSONObject jo = new JSONObject();
//        jo.put("id",this.getId());
//
//        //支付订单号
//        jo.put("orderNo",this.getOrdersNo());
//        jo.put("amount",this.getAmount());
//
//        //支付渠道
//        jo.put("payChannel",this.getPayChannel());
//        jo.put("payChannelNo",this.getPayChannelNo());
//        //付款人渠道账号
//        jo.put("payerChannelAccount",this.getPayerChannelAccount());
//
//
//        //付款人id
//        jo.put("payerMemberId",this.payerMember.getId());
//        jo.put("payerMemberName",this.payerMember.getName());
//
//        //收款人id
//        jo.put("payeeMemberId",this.payeeMember.getId());
//        jo.put("payeeMemberName",this.payeeMember.getName());
//
//        //支付时间
//        jo.put("createTime",this.getCreateTime()!=null? DateUtil.dateTime2Str(this.getCreateTime()):"");
//
//        //订单支付状态{-1:失败,1:成功}
//        jo.put("status",this.getStatus());
//        return jo;
//
//    }
//}
