package com.bcb.pay.dao;

import com.bcb.base.AbstractDao;
import com.bcb.pay.entity.RefundOrder;

import java.util.List;
import java.util.Optional;

/**
 * @Author G.
 * @Date 2019/11/27 0027 上午 11:32
 */
public interface RefundOrderDao extends AbstractDao<RefundOrder,Long> {

    List<RefundOrder> findByPayOrderNo(String payOrderNo);
    Optional<RefundOrder> findByRefundOrderNo(String refundOrderNo);
}
