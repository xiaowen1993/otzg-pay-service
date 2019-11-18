//package com.bcb.pay.api;
//
//import com.bcb.base.Finder;
//import com.bcb.pay.entity.Account;
//import com.bcb.pay.service.AccountServ;
//import com.bcb.util.CheckUtil;
//import com.bcb.util.DateUtil;
//import com.bcb.util.FuncUtil;
//import com.bcb.util.RespTips;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController("accountController")
//public class AccountApi extends AbstractController {
//
//    @Autowired
//    private AccountServ accountServ;
//
////    //获取个人账户
////    @RequestMapping("/member/account/get")
////    public void getAccount(String token) {
////        Map jo = accountServ.findByMemberId(getMember(token).getId());
////        if (CheckUtil.isEmpty(jo)) {
////            sendDataNull();
////            return;
////        }
////        sendSuccess(jo);
////    }
////
////
////    //=======================================支付密码======================================================/
////
////    //通过手机短信，设置账户支付密码
////    @RequestMapping("/member/account/password/save")
////    public void setAccountPayPassword(String token,
////                                      String mobilePhone,
////                                      String smsCode,
////                                      String password) {
////        if (!CheckUtil.isMobile(mobilePhone)
////                || CheckUtil.isEmpty(smsCode)
////                || !CheckUtil.isPassword(password)) {
////            sendParamError();
////            return;
////        }
////
////        //验证手机短信
////        if (!SmsSend.isSmsCode(mobilePhone, mobilePhone, smsCode)) {
////            sendJson(false, RespTips.SMSCODE_ERROR.code, RespTips.SMSCODE_ERROR.tips);
////            return;
////        }
////
////        //验证通过，删除短信验证码
////        SmsSend.delSmsCode(mobilePhone);
////
////        Member member = getMember(token);
////        //验证会员手机是否相同
////        if (!mobilePhone.equals(member.getMobilePhone())) {
////            sendJson(false, RespTips.DATA_CANT_OPTION.code, "手机号与认证过的不符");
////            return;
////        }
////
////        //修改支付密码
////        int r = accountServ.savePayPassword(password, getMember(token));
////        if (r == 1) {
////            sendJson(false, RespTips.ACCOUNT_IS_UNAVAILABLE.code, RespTips.ACCOUNT_IS_UNAVAILABLE.tips);
////            return;
////        }
////
////        sendSuccess();
////    }
////
////    //校验账户支付密码
////    @RequestMapping("/member/account/password/check")
////    public void checkAccountPayPassword(String token,
////                                        String password) {
////        if (!CheckUtil.isPassword(password)) {
////            sendParamError();
////            return;
////        }
////
////        int r = accountServ.checkPayPassword(password, getMember(token).getId());
////        if (r == 0) {
////            sendJson(true, RespTips.ACCOUNT_PASSWORD_IS_RIGHT.code,RespTips.ACCOUNT_PASSWORD_IS_RIGHT.tips);
////            return;
////        } else {
////            sendJson(false, RespTips.ACCOUNT_PASSWORD_IS_WRONG.code, RespTips.ACCOUNT_PASSWORD_IS_WRONG.tips);
////            return;
////        }
////    }
////
////    /**
////     * 修改支付密码
////     *
////     * @param token
////     * @param usedPassword 旧密码
////     * @param newPassword  新密码
////     */
////    @RequestMapping(value = "/member/account/password/update")
////    public void accountUpdatePassword(String token,
////                                      String usedPassword,
////                                      String newPassword) {
////        if (CheckUtil.isEmpty(usedPassword) ||
////                CheckUtil.isEmpty(newPassword)) {
////            sendParamError();
////            return;
////        }
////
////        int r = accountServ.checkPayPassword(usedPassword, getMember(token).getId());
////        if (r == 1) {
////            sendJson(false, RespTips.ACCOUNT_PASSWORD_IS_WRONG.code, RespTips.ACCOUNT_PASSWORD_IS_WRONG.tips);
////            return;
////        }
////        int i = accountServ.savePayPassword(newPassword, getMember(token));
////        if (i == 1) {
////            sendJson(false, RespTips.ACCOUNT_IS_UNAVAILABLE.code, RespTips.ACCOUNT_IS_UNAVAILABLE.tips);
////            return;
////        }
////        sendSuccess();
////    }
////
////    /**
////     * 判断是否存在支付密码
////     *
////     * @param token
////     */
////    @RequestMapping(value = "/member/account/password/find")
////    public final void findAccountPassword(String token) {
////        Account account = accountServ.findByMember(getMember(token).getId());
////        if (CheckUtil.isEmpty(account)) {
////            sendJson(false, RespTips.DATA_NULL.code, "账户信息不存在!");
////            return;
////        } else if (CheckUtil.isEmpty(account.getPassword())) {
////            sendJson(false, RespTips.DATA_NULL.code, "支付密码不存在!");
////            return;
////        } else {
////            sendJson(false, RespTips.SUCCESS_CODE.code, "支付密码已存在!");
////            return;
////        }
////    }
////
////
////    //============================消息相关====================================
////
////    /**
////     * 标记当前用户的账户收益记录为已读
////     *
////     * @param token
////     * @param profitType 标记对应的收益类型
////     */
////    @RequestMapping("/member/account/read")
////    public void clearReadMarkByLevel(String token, Integer profitType) {
////        if (CheckUtil.isEmpty(profitType)) {
////            sendParamError();
////            return;
////        }
////
////        int r = accountServ.clearReadMarkByProfitType(getMember(token).getAccount(), profitType);
////        if (r == 0) {
////            sendSuccess();
////        }
////
////    }
////
////
////    //============================记录相关====================================
////    //获取会员(推广员及商户)账户流水
////    @RequestMapping("/member/account/find")
////    public void getAccountByAdmin(String token, Finder finder) {
////        Account account = accountServ.findByMember(getMember(token).getId());
////        if (CheckUtil.isEmpty(account)) {
////            sendDataNull();
////            return;
////        }
////        sendJson(accountServ.findAccountLog(finder, account.getId(), getMember(token).getAccount()));
////    }
////
////    //获取下级商户账户流水
////    @RequestMapping("/member/subPromotion/account/find")
////    public void getSubPromotionAccount(String token,
////                                       Finder finder,
////                                       Long memberId) {
////        if (CheckUtil.isEmpty(memberId)) {
////            sendParamError();
////            return;
////        }
////
////        //获取当前用户的下属商户id
////        String subPromotionMemberIds = promotionServ.findFirstMemberListByMemberId(getMember(token).getId());
////        //如果查询的商户账户id属于当前用户
////        if (FuncUtil.CountSubStr(memberId.toString(), subPromotionMemberIds) > 0) {
////            sendJson(accountServ.findAccountLog(finder, null, getMember(token).getAccount()));
////        } else {
////            sendAccessFail();
////        }
////    }
////
////    /**
////     * 查询用户收益
////     *
////     * @param token
////     * @param profitType 收益类型(0全部1一级收益2二级收益3三级收益)
////     * @param finder     分页类
////     * @param type       ={1:个人账户，2:账户}
////     */
////    @RequestMapping(value = "/member/account/findProfitAccountLog")
////    public void findProfitAccountLog(String token,
////                                     String profitType,
////                                     Finder finder,
////                                     Integer type) {
////        if (CheckUtil.isEmpty(token) ||
////                CheckUtil.isEmpty(type) ||
////                CheckUtil.isEmpty(profitType)) {
////            sendParamError();
////            return;
////        }
////        //获取账户
////        Account account = accountServ.findByMember(getMember(token).getId());
////        if (CheckUtil.isEmpty(account)) {
////            sendJson(false, RespTips.ACCOUNT_IS_UNAVAILABLE.code, "账户不存在!");
////            return;
////        }
////        sendJson(accountServ.findProfitAccountLog(account.getId(), profitType, finder));
////    }
////
////    //按输入的日期获取推广员每天收益金额
////    @RequestMapping(value = "/member/profit/every_day")
////    public final void findMemberDayAmountByDateBetween(String token,
////                                                       String startDate,
////                                                       String endDate) {
////        if (!CheckUtil.isDate(startDate)) {
////            sendParamError();
////            return;
////        }
////
////        //设置结束日期
////        if (!CheckUtil.isDate(endDate)) {
////            endDate = DateUtil.yearMonthDay();
////        }
////        //返回结束日期的最后一秒
////        endDate = DateUtil.getEndTime(endDate);
////
////        sendJson(accountServ.findMemberDayAmountByDateBetween(startDate, endDate, getMember(token).getId()));
////    }
////
////    //按输入的日期获取商户每天收益金额
////    @RequestMapping(value = "/unit/profit/every_day")
////    public final void findUnitDayAmountByDateBetween(String token,
////                                                     String startDate,
////                                                     String endDate) {
////        if (!CheckUtil.isDate(startDate)) {
////            sendParamError();
////            return;
////        }
////
////        //设置结束日期
////        if (!CheckUtil.isDate(endDate)) {
////            endDate = DateUtil.yearMonthDay();
////        }
////        //返回结束日期的最后一秒
////        endDate = DateUtil.getEndTime(endDate);
////
////        sendJson(accountServ.findUnitDayAmountByDateBetween(startDate, endDate, getMember(token).getAccount()));
////    }
////
////    //按输入的日期获取推广员收益笔数和总额
////    @RequestMapping(value = "/member/profit/count/sum")
////    public final void findMemberProfitCountAndSumByDateBetween(String token,
////                                                               String startDate,
////                                                               String endDate) {
////        //设置结束日期
////        if (CheckUtil.isDate(startDate) && !CheckUtil.isDate(endDate)) {
////            endDate = DateUtil.yearMonthDay();
////        }
////
////        //如果设置了结束日期
////        if (CheckUtil.isDate(endDate)) {
////            //返回结束日期的最后一秒
////            endDate = DateUtil.getEndTime(endDate);
////        }
////
////        sendSuccess(accountServ.findMemberProfitCountAndSumByDateBetween(startDate, endDate, getMember(token).getId()));
////    }
////
////    //按输入的日期获取商户收益笔数和总额
////    @RequestMapping(value = "/unit/profit/count/sum")
////    public final void findUnitProfitCountAndSumByDateBetween(String token,
////                                                             String startDate,
////                                                             String endDate) {
////        //设置结束日期
////        if (CheckUtil.isDate(startDate) && !CheckUtil.isDate(endDate)) {
////            endDate = DateUtil.yearMonthDay();
////        }
////
////        //如果设置了结束日期
////        if (CheckUtil.isDate(endDate)) {
////            //返回结束日期的最后一秒
////            endDate = DateUtil.getEndTime(endDate);
////        }
////
////        sendSuccess(accountServ.findUnitProfitCountAndSumByDateBetween(startDate, endDate, getMember(token).getAccount()));
////    }
////
////
////    //===================================统计相关============================================
////    //会员收益统计{1天、7天、全部}
////    @RequestMapping(value = "/member/profit/statistics")
////    public final void findProfitStatistics(String token) {
////        //获取账户
////        Account account = accountServ.findByMember(getMember(token).getId());
////        if (CheckUtil.isEmpty(account)) {
////            sendJson(false, RespTips.ACCOUNT_IS_UNAVAILABLE.code, "账户不存在!");
////            return;
////        }
////
////        sendSuccess(accountServ.getStatistics(account.getId()));
////    }
////
////    //商户收益统计{1天、7天、全部}
////    @RequestMapping(value = "/unit/profit/statistics")
////    public final void findUnitProfitStatistics(String token) {
////        sendSuccess(accountServ.getUnitStatistics(getMember(token).getAccount()));
////    }
////
////    //按输入的日期获取到今天为止的每天收益金额
////    @RequestMapping(value = "/member/profit/lastDate")
////    public final void findDayAmountByLastDate(String token, String startDate, String endDate) {
////        if (!CheckUtil.isDate(startDate)) {
////            sendParamError();
////            return;
////        }
////        if (!CheckUtil.isDate(endDate)) {
////            endDate = DateUtil.yearMonthDay();
////        }
////
////        //获取账户
////        Account account = accountServ.findByMember(getMember(token).getId());
////        if (CheckUtil.isEmpty(account)) {
////            sendJson(false, RespTips.ACCOUNT_IS_UNAVAILABLE.code, "账户不存在!");
////            return;
////        }
////
////        sendJson(accountServ.findDayAmountByDateBetween(account.getId(), startDate, endDate));
////    }
////
////    @RequestMapping(value = "/unit/profit/lastDate")
////    public final void findUnitDayAmountByLastDate(String token, String startDate, String endDate) {
////        if (!CheckUtil.isDate(startDate)) {
////            sendParamError();
////            return;
////        }
////        if (!CheckUtil.isDate(endDate)) {
////            endDate = DateUtil.yearMonthDay();
////        }
////
////        sendJson(accountServ.findUnitDayAmountByDateBetween(startDate, endDate, getMember(token).getAccount()));
////    }
//
//
////    @RequestMapping(value = "/pay/account/save")
////    public void payOrderQuery(String token,
////                              String dateTime,
////                              BigDecimal amount,
////                              String payChannel) {
////        if (CheckUtil.isEmpty(dateTime)
////                ||!CheckUtil.isDatetime(dateTime)
////                || CheckUtil.isEmpty(amount)
////                || CheckUtil.isEmpty(payChannel)
////                ) {
////            sendParamError();
////            return;
////        }
////
////        //保存流水单号
////        int r = accountLogServ.saveAccountLog(getMember(token).getAccount(), dateTime,amount,payChannel);
////        if(r==0){
////            sendSuccess();
////        }else{
////            sendFail();
////        }
////    }
//}
