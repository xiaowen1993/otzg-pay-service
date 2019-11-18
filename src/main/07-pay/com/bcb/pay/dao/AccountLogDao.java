//package com.bcb.pay.dao;
//
//import com.bcb.base.AbstractDao;
//import com.bcb.pay.entity.AccountLog;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//
///**
// * @author G./2018/7/11 15:10
// */
//public interface AccountLogDao extends AbstractDao<AccountLog, Long> {
//    @Query("select al from AccountLog al where al.payOrdersNo = ?1")
//    AccountLog getByPayOrdersNo(String number);
////
////    @Query(value = "select al from AccountLog al left join al.account a left join a.unit u where u.id= ?1 and al.profitType=?2")
////    List<AccountLog> findByNotReadByProfitType(Long unitId, Integer profitType);
//
//    @Modifying
//    @Transactional
//    @Query(value="update Account_Log al left join account a on al.account_id=a.id set al.is_Read=1 where a.member_account=?1 and al.profit_Type=?2",nativeQuery = true)
//    void clearReadMarkByProfitType(String account, Integer profitType);
//
//    //返回最后一条更新的商户交易流水时间
//    @Query(value="select max(al.createTime) from AccountLog al where al.memberAccount is not null")
//    Date findLastDateTimeByMemberAccount();
//}
