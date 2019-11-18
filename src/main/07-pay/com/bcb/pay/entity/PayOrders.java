package com.bcb.pay.entity;

import com.bcb.pay.util.PayType;
import com.bcb.util.CheckUtil;
import com.bcb.util.DateUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * 支付订单类
 * 1、所有发生的支付业务生成支付订单
 * 2、每一个支付订单属性
 *         支付单号、
 *         支付项目、
 *         金额、
 *         业务单号、
 *         业务详情：{
 *             项目名称、
 *             数量
 *         }
 *         付款单位、
 *         付款人、
 *         收款单位
 *
 *
 * 关键属性
 *      单据类型 payType:{1:余额收入,2:余额支出,3:交押金,4:退押金,5:收益收入,6:收益支出,7:积分收入,8:积分消费}
 *      支付单号 number
 *      业务单号 ordersNo
 *      业务发生额 amount
 *      付款发起人 payerMember
 *      付款单位 payerUnit
 *      收款单位 payeeUnit
 *
 * @author G./2018/5/29 10:14
 *
 * 修改信息：
 *
 * 2019-05-11
 * 为了游客收款业务
 * 1、付款人信息、付款人单位可以为null
 *
 */

@Entity
@Table(name = "pay_orders")
public class PayOrders implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //支付项目
    @Column(name = "subject",nullable = false,length = 64)
    private String subject;

    //支付发生额
    @Column(name = "amount", precision = 8,scale = 2)
    private BigDecimal amount;

    //业务单号
    @Column(name = "orders_no",length = 64,nullable = false,unique = true)
    private String ordersNo;

    /**
     * 回调子系统通知接口
     * 如果支付业务需要通知子系统支付结果，则传递appid
     * @author G/2018/7/5 16:28
     */
    @Column(name = "appid",length = 64)
    private String appid;

    //通知回调接口成功,通知子系统支付结果{0:未收到,1:已收到,2回调失败}
    @Column(name = "pay_notify_status", length = 1)
    private Integer payNotifyStatus=0;

    //通知回调次数
    @Column(name = "pay_notify_times", length = 1)
    private Integer payNotifyTimes=0;

    //业务详情
    @Lob
    @Column(name = "details",columnDefinition="TEXT")
    private String details;

    //发起人id
    @Column(name="member_id")
    private Long memberId;

    //后台发起人id
    @Column(name="user_id")
    private Integer userId;

    //付款发起人账户
    @Column(name="payer_account_id")
    private Long payerAccountId;
    //收款人账户
    @Column(name="payee_account_id")
    private Long payeeAccountId;

    //创建时间
    @Column(name = "create_time", nullable = false, length = 19)
    private Date createTime;

    //修改时间
    @Column(name = "update_time", nullable = false, length = 19)
    private Date updateTime;

    //{0:充值,1:交年费,2:提现,10:绑定账户,11:余额返利}
    @Column(name = "pay_type", nullable = false, length = 1)
    private Integer payType;

    //如果是收年费，记录失效日期
    @Column(name = "expires_date", length = 19)
    private Date expiresDate;

    //返利规则
    @Column(name = "allocation_id",length = 8)
    private Integer allocationId;

    //交年费业务存放返利标识{-1:无须返利,0:未返利,1:已返利}
    @Column(name = "allocation_status",length = 1)
    private Integer allocationStatus;


    /**
     * 支付订单状态{-1:支付失败,0:未支付,1:支付成功}
     * 2、支付成功订单状态置为1，
     * 3、支付失败订单状态为-1，
     */
    @Column(name = "status",nullable = false,length = 1)
    private Integer status=0;

    /**
     * {1:待审批,3:审批成功,4:审批失败}
     */
    @Column(name = "approval_status",nullable = false,length = 1)
    private Integer approvalStatus=1;

    /**
     *
     * 需要每次更新审批结果后重置
     */
    @Column(name = "approval_comment",length = 128)
    private String approvalComment="";


    //支付宝或者微信
    @Column(name = "pay_channel")
    private String payChannel;

    public PayOrders() {
    }

    public PayOrders(Date createTime,
                     String appid,
                     String ordersNo,
                     Integer payType,
                     String subject,
                     BigDecimal amount,
                     String details,
                     Long memberId,
                     Integer userId,
                     Long payerAccountId,
                     Long payeeAccountId,
                     String payChannel) {

        this.setAppid(appid);
        this.setPayType(payType);
        this.setOrdersNo(ordersNo);
        this.setSubject(subject);
        this.setAmount(amount);
        this.setDetails(details);

        this.setUserId(userId);
        this.setMemberId(memberId);
        this.setPayerAccountId(payerAccountId);
        this.setPayeeAccountId(payeeAccountId);

        if(CheckUtil.isEmpty(createTime)){
            this.setCreateTime(DateUtil.now());
        }else{
            this.setCreateTime(createTime);
        }
        this.setUpdateTime(this.getCreateTime());
        this.setPayChannel(payChannel);
    }

    public PayOrders(String appid,
                     String ordersNo,
                     Integer payType,
                     String subject,
                     BigDecimal amount,
                     String details,
                     Long memberId,
                     Integer userId,
                     Long payerAccountId,
                     Long payeeAccountId,
                     String payChannel
                     ) {

        this.setAppid(appid);
        this.setPayType(payType);
        this.setOrdersNo(ordersNo);
        this.setSubject(subject);
        this.setAmount(amount);
        this.setDetails(details);

        this.setUserId(userId);
        this.setMemberId(memberId);
        this.setPayerAccountId(payerAccountId);
        this.setPayeeAccountId(payeeAccountId);

        this.setCreateTime(DateUtil.now());
        this.setUpdateTime(this.getCreateTime());
        this.setPayChannel(payChannel);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrdersNo() {
        return ordersNo;
    }

    public void setOrdersNo(String ordersNo) {
        this.ordersNo = ordersNo;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getPayerAccountId() {
        return payerAccountId;
    }

    public void setPayerAccountId(Long payerAccountId) {
        this.payerAccountId = payerAccountId;
    }

    public Long getPayeeAccountId() {
        return payeeAccountId;
    }

    public void setPayeeAccountId(Long payeeAccountId) {
        this.payeeAccountId = payeeAccountId;
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

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public Integer getPayNotifyStatus() {
        return payNotifyStatus;
    }

    public void setPayNotifyStatus(Integer payNotifyStatus) {
        this.payNotifyStatus = payNotifyStatus;
    }

    public Integer getPayNotifyTimes() {
        return payNotifyTimes;
    }

    public void setPayNotifyTimes(Integer payNotifyTimes) {
        this.payNotifyTimes = payNotifyTimes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Date getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
    }

    public Integer getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(Integer allocationId) {
        this.allocationId = allocationId;
    }

    public Integer getAllocationStatus() {
        return allocationStatus;
    }

    public void setAllocationStatus(Integer allocationStatus) {
        this.allocationStatus = allocationStatus;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalComment() {
        return approvalComment;
    }

    public void setApprovalComment(String approvalComment) {
        this.approvalComment = approvalComment;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Map<String,Object> getJson(){
        Map<String,Object> jo = new TreeMap<>();
        jo.put("id",this.getId());
        jo.put("subject",this.getSubject());
        jo.put("amount",this.getAmount());

        //业务信息
        jo.put("ordersNo",Optional.ofNullable(this.getOrdersNo()).orElse(""));
        jo.put("details",Optional.ofNullable(this.getDetails()).orElse(""));

        //付款人id
        if(!CheckUtil.isEmpty(this.getPayerAccountId())){
            jo.put("payerAccountId",this.getPayerAccountId());
        }
        //收款
        if(!CheckUtil.isEmpty(this.getPayeeAccountId())){
            jo.put("payeeAccountId",this.getPayeeAccountId());
        }

        //支付时间
        jo.put("createTime",DateUtil.dateTime2Str(this.getCreateTime()));
        jo.put("updateTime",DateUtil.dateTime2Str(this.getUpdateTime()));


        //支付类型
        jo.put("payType", this.payType);
        jo.put("payTypeTips", PayType.getName(this.payType));
        jo.put("expiresDate",Optional.ofNullable(this.getExpiresDate()).map(date -> DateUtil.date2Str(date)).orElse(""));

        //支付订单状态{-2:订单删除,-1:支付失败,0:正在支付,1:支付成功}
        jo.put("status",this.getStatus());

        //审批意见
        if(!CheckUtil.isEmpty(this.getApprovalStatus())){
            jo.put("approvalStatus",this.getApprovalStatus());
            jo.put("approvalComment",Optional.ofNullable(this.getApprovalComment()).orElse(""));
        }

        //返利状态
        if(!CheckUtil.isEmpty(this.getAllocationStatus())){
            jo.put("allocationStatus",this.getAllocationStatus());
        }

        /**
         * 回调付款通知
         * @author G/2018/7/5 16:05
         * @param
         */
        jo.put("appid",Optional.ofNullable(this.getAppid()).orElse(""));
        jo.put("payChannel",Optional.ofNullable(this.getPayChannel()).orElse(""));

        return jo;

    }

}
