//package com.bcb.pay.service;
//
//import com.bcb.base.Finder;
//import com.bcb.member.entity.Member;
//import com.bcb.pay.entity.Account;
//import net.sf.json.JSONObject;
//
//import java.math.BigDecimal;
//import java.util.Map;
//
///**
// * @author G./2018/6/30 9:43
// */
//public interface AccountServ {
//
//    Account findById(Long id);
//
//    Map findByMemberId(Long memberId);
//
//    /**
//     * 获取账户信息(Account)
//     * @return
//     */
//    Account findByMember(Long memberId);
//
//    /**
//     * 获取平台账户信息(Account)
//     * @return
//     */
//    Account findPlatform();
//
//    Account saveAccount(Member member);
//
//    /**
//     * 获取统计
//     * @return
//     */
//    Map getStatistics(Long accountId);
//
//    /**
//     * 获取商户每天流水
//     * @param startDate
//     * @param endDate
//     * @param account
//     * @return
//     */
//    Map findUnitDayAmountByDateBetween(String startDate, String endDate, String account);
//
//    /**
//     * 获取商户流水统计
//     * @param startDate
//     * @param endDate
//     * @param account
//     * @return
//     */
//    Map findUnitProfitCountAndSumByDateBetween(String startDate, String endDate, String account);
//
//    /**
//     * 根据给定日期统计每一天的收益
//     * @param
//     * @return
//     */
//    Map findDayAmountByDateBetween(Long accountId,String startDate,String endDate);
//
//    Map getUnitStatistics(String account);
//
//    /**
//     * 单位账户绑定支付渠道账号
//     * 1、禁止更新账户其他信息
//     * 2、一个会员单位只能有一个账户
//     * @author G/2018/6/29 12:18
//     * @param payChannelId 微信对应openid,支付宝对应16位的会员id
//     * @param payChannelAccount 对应支付宝账号(支付宝登录账号包含邮箱号和手机号),微信对用mchid(公众号商户号|app商户号
//     * @param payChannelAccountName 支付渠道账号名称
//     * @param payChannel aplipay:支付宝,wxpay:微信支付,xghpay:象过河支付
//     * @param member 会员单位
//     */
//    int boundPayChannel(String payChannelId,
//                        String payChannelAccount,
//                        String payChannelAccountName,
//                        String payChannel,
//                        Member member);
//
//    /**
//     * 设置某个单位的支付密码
//     * @param password
//     * @param member
//     * @return
//     */
//    int savePayPassword(String password,Member member);
//
//    /**
//     * 验证支付密码
//     * @param password
//     * @param memberId
//     * @return
//     */
//    int checkPayPassword(String password, Long memberId);
//
//    //==========================支付过程====================================//
//    /**
//     * 判断付款单位收益账户余额是否够用
//     * @author G/2018/7/11 9:23
//     * @param payerMemberId
//     * @param profitAmount
//     */
//    boolean checkProfitAmount(Long payerMemberId, BigDecimal profitAmount);
//
//    /**
//     * 判断账户是否可用
//     * @author G/2018/7/11 9:51
//     * @param memberId
//     */
//    boolean checkAccount(Long memberId);
//
//    /**
//     * 查询账户流水
//     */
//    Map findAccountLog(Finder finder,Long accountId,String mobile);
//
//    /**
//     * 查询账户记录
//     * @param accountId
//     * @return
//     */
//    Map findProfitAccountLog(Long accountId,String profitType,Finder finder);
//
//    /**
//     * 标记当前用户的账户收益记录为已读
//     * @param mobile
//     * @param profitType
//     * @return
//     */
//    int clearReadMarkByProfitType(String mobile, Integer profitType);
//
//
//    //=============================管理端方法=======================================
//
//    //获取账户流水
//    Map findAccountLog(Finder finder);
//
//    //推广员总收益排行
//    Object[] findMemberProfitSort();
//
//    //商户总收益排行
//    Object[] findUnitProfitSort();
//
//    //全体推广员总收入及平均收入
//    Map findMemberProfitTotalAndAvg();
//    //全体商户总收入及平均收入
//    Map findUnitProfitTotalAndAvg();
//
//    //按输入的日期获取推广员收益笔数和总额
//    Map findMemberProfitCountAndSumByDateBetween(String startDate, String endDate, Long memberId);
//    //获取全体推广员一段时间内的日收入
//    Map findMemberDayAmountByDateBetween(String startDate, String endDate,Long memberId);
//    //获取全体商户一段时间内的日收入
//    Map findUnitDayAmountByDateBetween(String startDate, String endDate);
//
//    /**
//     * 推广员下属商户的区间收益分页
//     * 1、返回推广员姓名、区域、商户收益和、推广员注册日期
//     * @param finder
//     * @return
//     */
//    Map findUnitProfitByDateBetweenGroupByMember(Finder finder);
//
//
//    /**
//     * 商户数据统计
//     * @param finder
//     * @return
//     */
//    Map findUnitProfitByDateBetween(Finder finder);
//    /**
//     * 推广员的区间收益分页
//     * 1、返回推广员姓名、区域、收益和、推广员注册日期
//     * @param finder
//     * @return
//     */
//    Map findMemberProfitByDateBetween(Finder finder);
//
//    /**
//     * 获取平台收益流水
//     * @param finder
//     * @param accountId
//     * @return
//     */
//    JSONObject findPlatformAccountLog(Finder finder,Long accountId);
//
//
//
//
//    boolean testLock();
//
//    Map testWatch(Integer k);
//}
