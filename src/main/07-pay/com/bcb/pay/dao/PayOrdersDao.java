package com.bcb.pay.dao;

import com.bcb.base.AbstractDao;
import com.bcb.pay.entity.PayOrders;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author G./2018/7/3 10:46
 */
public interface PayOrdersDao extends AbstractDao<PayOrders,Long> {

//    //查询支付成功的订单
//    @Query("select po from PayOrders po where po.ordersNo=?1 and po.status=1")
//    List<PayOrders> findSuccessByOrderNo(String orderNo);
//
//    @Query("select po from PayOrders po where  po.memberId=?1 and po.payType =?2 order by po.createTime desc")
//    List<PayOrders> findByMemberAndPayType(Long payerMemberId,Integer payType);
//
//    //查询用户未支付的同类型订单
//    @Query("select po from PayOrders po where  po.status=0 and po.memberId=?1 and po.payType =?2")
//    List<PayOrders> findWaitByMemberAndPayType(Long payerMemberId,Integer payType);
//
//    @Query("select po from PayOrders po where  po.status=1 and po.memberId=?1 and po.payType =?2 order by po.createTime desc")
//    List<PayOrders> findSuccessByMemberAndPayType(Long payerMemberId,Integer payType);
//
//    //查询支付成功的订单
//    @Query("select po from PayOrders po where po.ordersNo=?1")
//    PayOrders findByOrderNo(String orderNo);
//
//    PayOrders findFirstByAppidAndOrdersNo(String appid,String ordersNo);
//
//    //查询是否有退款成功订单
//    @Query("select po from PayOrders po where po.ordersNo=?1 and po.payType=10 and po.status=2")
//    PayOrders findByRefund(String orderNo);
}
