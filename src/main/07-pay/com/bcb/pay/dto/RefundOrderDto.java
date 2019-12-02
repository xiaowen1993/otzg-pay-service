package com.bcb.pay.dto;

import java.io.Serializable;

/**
 * @Author G.
 * @Date 2019/11/27 0027 上午 10:32
 */
public class RefundOrderDto implements Serializable {
    /**
     * 子系统单位(商户)id
     *
     * 必填
     * length(32)
     */
    String unitId;

    /**
     * 子系统会员(个人)id
     * 业务发起人id
     *
     * length(32)
     */
    String memberId;

    /**
     * 退款金额
     */
    Double amount;

    /**
     * 子系统收款时的业务单号
     * 1.收款业务单号作为退款的依据
     * 2.多次退款可以多次传递
     * 必填
     * length(32)
     */
    String orderNo;

    /**
     * 子系统退款业务单号
     * 1.每个退款单号只能发起一次
     * 2.每个支付单可以多次退款
     * 必填
     * length(32)
     */
    String refundOrderNo;

    /**
     * 退款项目名称
     *
     * 必填
     * length(128)
     */
    String subject;

    private RefundOrderDto() {
    }

    public RefundOrderDto(String unitId, String memberId, Double amount, String orderNo, String refundOrderNo, String subject) {
        this.unitId = unitId;
        this.memberId = memberId;
        this.amount = amount;
        this.orderNo = orderNo;
        this.refundOrderNo = refundOrderNo;
        this.subject = subject;
    }

    public String getUnitId() {
        return unitId;
    }

    public String getMemberId() {
        return memberId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getSubject() {
        return subject;
    }

    public String getRefundOrderNo() {
        return refundOrderNo;
    }
}
