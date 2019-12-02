package com.bcb.pay.dao;

import com.bcb.base.AbstractDao;
import com.bcb.pay.entity.PayOrder;

import java.util.Optional;

/**
 * @author G./2018/7/3 10:46
 */
public interface PayOrderDao extends AbstractDao<PayOrder,Long> {
    //根据支付业务单查询
    Optional<PayOrder> findByPayOrderNo(String payOrderNo);
    //根据子系统收款单查询
    Optional<PayOrder> findByOrderNo(String orderNo);

    Optional<PayOrder> findByUnitIdAndOrderNo(String unitId,String orderNo);
    Optional<PayOrder> findByUnitIdAndPayOrderNo(String unitId,String payOrderNo);
}
