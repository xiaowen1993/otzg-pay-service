//package com.bcb.pay.service;
//
//import com.bcb.base.AbstractServ;
//import com.bcb.base.Finder;
//import com.bcb.base.Page;
//import com.bcb.log.util.LogUtil;
//import com.bcb.message.service.MessageServ;
//import com.bcb.pay.dao.AccountDao;
//import com.bcb.pay.dao.AccountLogDao;
//import com.bcb.pay.dao.PayOrdersDao;
//import com.bcb.pay.dao.PayOrdersLogDao;
//import com.bcb.pay.entity.Account;
//import com.bcb.pay.entity.AccountLog;
//import com.bcb.pay.entity.PayOrders;
//import com.bcb.pay.entity.PayOrdersLog;
//import com.bcb.pay.util.PayChannelType;
//import com.bcb.pay.util.PayType;
//import com.bcb.wxpay.util.WxpayConfig;
//import com.bcb.wxpay.util.WxpaySubmit;
//import com.bcb.promotion.entity.Allocation;
//import com.bcb.promotion.entity.AllocationPay;
//import com.bcb.promotion.service.AllocationServ;
//import com.bcb.promotion.service.PromotionServ;
//import com.bcb.util.*;
//import net.sf.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.interceptor.TransactionAspectSupport;
//
//import javax.transaction.Transactional;
//import java.math.BigDecimal;
//import java.util.*;
//
///**
// * @author G./2018/7/3 10:38
// */
//@Service("payOrdersServ")
//public class PayOrdersServImpl extends AbstractServ implements PayOrdersServ {
//
//    @Autowired
//    private PayOrdersDao payOrdersDao;
//
//    @Autowired
//    private PayOrdersLogDao payOrdersLogDao;
//
//    @Autowired
//    private AccountDao accountDao;
//
//    @Autowired
//    private AccountLogDao accountLogDao;
//    @Autowired
//    private MessageServ messageServ;
//
//    @Autowired
//    private LockUtil lockUtil;
//
//    @Autowired
//    private AllocationServ allocationServ;
//    @Autowired
//    private AccountServ accountServ;
//    @Autowired
//    private PromotionServ promotionServ;
//
//    @Override
//    public PayOrders findByMemberAndType(Long payerMemberId, Integer payType) {
//        List<PayOrders> payOrdersList = payOrdersDao.findByMemberAndPayType(payerMemberId, payType);
//        if (CheckUtil.isEmpty(payOrdersList)) {
//            return null;
//        }
//        return payOrdersList.get(0);
//    }
//
//    @Override
//    public PayOrders findWaitByMemberAndType(Long payerMemberId, Integer payType) {
//        List<PayOrders> payOrdersList = payOrdersDao.findWaitByMemberAndPayType(payerMemberId, payType);
//        if (CheckUtil.isEmpty(payOrdersList)) {
//            return null;
//        }
//        return payOrdersList.get(0);
//    }
//
//    @Override
//    public String findYearFeeEndDate(Long memberId) {
//        String sql ="select ifnull(DATE_FORMAT(u.expires_date,'%Y-%m-%d'),CURDATE()) as expiresDate from unit u where u.member_id = "+memberId;
//        List<Map<String,Object>> m =payOrdersDao.findMapBySql(sql);
//        if(CheckUtil.isEmpty(m)){
//            return DateUtil.yearMonthDay();
//        }
//
//        //返回剩余日期
//        return m.get(0).get("expiresDate").toString();
//    }
//
//    /**
//     * * 1、商户交年费业务金额充值到平台账户
//     * 2、按返利规则从平台账户扣钱(返利)=>推广员、平台
//     * <p>
//     * 生成交年费订单，并返回支付数据
//     *
//     * @return 支付宝交易码
//     */
//    @Override
//    @Transactional
//    public JSONObject payYearFee(Allocation allocation,
//                                 String appid,
//                                 String subject,
//                                 BigDecimal amount,
//                                 String details,
//                                 Long memberId,
//                                 Long payerAccountId,
//                                 Long payeeAccountId,
//                                 String payChannel,
//                                 String openid) {
//        //生成订单
//        String payOrdersNo = DateUtil.yearMonthDayTimeShort() + FuncUtil.getRandInt(111, 999) + payerAccountId.hashCode();
//
//        Date createTime = DateUtil.dateAfter(DateUtil.now(), 1);
//        //年费剩余有效期
//        String endDate = findYearFeeEndDate(memberId);
//        if (!CheckUtil.isEmpty(endDate)) {
//            //设置为下个年度
//            createTime = DateUtil.dateAfter(DateUtil.str2Date(endDate), 1);
//        }
//
//        //创建订单
//        PayOrders po = new PayOrders(createTime, appid, payOrdersNo, PayType.YEAR_FEE.index, subject, amount, details, memberId, null, payerAccountId, payeeAccountId, payChannel);
//        //年费返利规则
//        po.setAllocationId(allocation.getId());
//        //年费未返利(缴费后返利)
//        po.setAllocationStatus(0);
//        //年费失效日期
//        po.setExpiresDate(DateUtil.dateAfter(createTime, 365));
//        payOrdersDao.save(po);
//
//        JSONObject jo = new JSONObject();
//        if (PayChannelType.getName(payChannel).equals("alipay")) {
//            jo = getAlipayAppReceipt(po, "app");
//        } else if (PayChannelType.getName(payChannel).equals("wxpay")) {
//            jo = getWxpayAppReceipt(po, "app", openid);
//        }
//        return jo;
//    }
//
//    /**
//     * 调起支付宝收款参数
//     *
//     * @param payOrders
//     * @author G/2018/7/10 16:28
//     */
//    private JSONObject getAlipayAppReceipt(PayOrders payOrders, String terminal) {
//        JSONObject jo = new JSONObject();
//
////        //单位由元转换成分
////        BigDecimal amount = payOrders.getAmount();
////        if (terminal != null && terminal.equals("web")) {
////            String appPayString = AlipaySubmit.webPay(payOrders.getOrdersNo(), amount.toString(), payOrders.getSubject(), payOrders.getSubject());
////            jo.put("payOrdersNo", payOrders.getOrdersNo());
//////            jo.put("orderNo", payOrders.getOrdersNo());
////            jo.put("webPayString", appPayString);
////        } else if (terminal != null && terminal.equals("app")) {
////            String appPayString = AlipaySubmit.appPay(payOrders.getOrdersNo(), amount.toString(), payOrders.getSubject(), payOrders.getSubject());
////            jo.put("payOrdersNo", payOrders.getOrdersNo());
//////            jo.put("orderNo", payOrders.getOrdersNo());
////            jo.put("appPayString", appPayString);
/////*            jo.put("success",true);
////            jo.put("data",jsonObject);
////            jo.put("code",RespTips.SUCCESS_CODE.code);*/
////        } else if (terminal != null && terminal.equals("JSAPI")) {
////            String appPayString = AlipaySubmit.jsapiPay(payOrders.getOrdersNo(), amount.toString(), payOrders.getSubject(), payOrders.getSubject());
////            jo.put("payOrdersNo", payOrders.getOrdersNo());
//////            jo.put("orderNo", payOrders.getOrdersNo());
////            jo.put("jsapiPayString", appPayString);
////        } else {
////            String appPayString = AlipaySubmit.wapPay(payOrders.getOrdersNo(), amount.toString(), payOrders.getSubject(), payOrders.getSubject());
////            jo.put("payOrdersNo", payOrders.getOrdersNo());
//////            jo.put("orderNo", payOrders.getOrdersNo());
////            jo.put("webPayString", appPayString);
////        }
//        return jo;
//    }
//
//
//    @Override
//    @Transactional
//    public JSONObject recharge(String appid,
//                               String subject,
//                               BigDecimal amount,
//                               String details,
//                               Long memberId,
//                               Long payeeAccountId,
//                               String payChannel,
//                               String terminal,
//                               String openid,
//                               Integer payType) {
//
//        String ordersNo = String.valueOf(System.currentTimeMillis())
//                + FuncUtil.getRandInt(111, 999)
//                + Math.abs(memberId);
//
//        //创建收款订单
//        PayOrders po = new PayOrders(appid, ordersNo, payType, subject, amount, details, memberId, null, null, payeeAccountId, payChannel);
//        payOrdersDao.save(po);
//        JSONObject jo = new JSONObject();
//        if (PayChannelType.getName(payChannel).equals("alipay")) {
//            jo = getAlipayAppReceipt(po, terminal);
//        } else if (PayChannelType.getName(payChannel).equals("wxpay")) {
//            jo = getWxpayAppReceipt(po, terminal, openid);
//        }
//        return jo;
//    }
//
//    //提现申请
//    @Override
//    @Transactional
//    public Map getCashApply(String appid,
//                            String subject,
//                            BigDecimal amount,
//                            String details,
//                            Integer userId,
//                            Long memberId,
//                            Long payerAccountId,
//                            Long payeeAccountId,
//                            String payChannel) {
//
//        //生成订单
//        String payOrdersNo = DateUtil.yearMonthDayTimeShort() + FuncUtil.getRandInt(111, 999) + payerAccountId.hashCode();
//
//        //创建订单
//        PayOrders po = new PayOrders(appid, payOrdersNo, PayType.CASH_GET.index, subject, amount, details, memberId, userId, payerAccountId, payeeAccountId, payChannel);
//        //设置提现申请状态
//        po.setApprovalStatus(1);
//        payOrdersDao.save(po);
//        return po.getJson();
//    }
//
//    /**
//     * 同意提现
//     * 前置：
//     * 1、账户设置了渠道收款账户
//     * 2、账户余额大于支付金额
//     */
//    @Override
//    @Transactional
//    public synchronized JSONObject payCash(PayOrders po, Account ac, Integer userId) {
//        po.setUserId(userId);
//        po.setApprovalStatus(3);
//
//        //如果账户余额小于支出额返回
//        if (ac.getBalance().compareTo(po.getAmount()) < 0) {
//            return JsonUtil.get(false, RespTips.ACCOUNT_IS_INSUFFICIENT.code, RespTips.ACCOUNT_IS_INSUFFICIENT.tips);
//        }
//
//        //获取app对应订单编号
////        String orderNumber = AccessDecision.getOrderNumberMap(appid);
//
//        JSONObject jo = new JSONObject();
//
//        //如果会员绑定的支付渠道账户为支付宝
//        if (ac.getPayChannel().equals("alipay")) {
//            //ALIPAY_USERID 表示用支付宝id  ALIPAY_LOGONID表示用支付宝登录账号
//
//            //如果是支付宝账号
//            String aliPayType = (CheckUtil.isEmpty(ac.getPayChannelId())) ? "ALIPAY_LOGONID" : "ALIPAY_USERID";
//            String aliPayeeId = (CheckUtil.isEmpty(ac.getPayChannelId()) ? ac.getPayChannelAccount() : ac.getPayChannelId());
//            //提交付款到支付宝
////            String result = AlipaySubmit.transPay(po.getOrdersNo(), aliPayType, aliPayeeId, po.getAmount().toString(), "", "", po.getDetails());
////            JSONObject resultJo = JSONObject.fromObject(result);
//
//            //==========================模拟支付成功start===================================
////            JSONObject resultJo = new JSONObject();
////            resultJo.put("success", true);
////            resultJo.put("ordersId", "111111111111111111111111111111");
////            String aliPayeeId = "11111111111111111111111";
//            //==========================模拟支付成功end=====================================
//
//
//            //交易记录写盘
////            P("支付宝提现结果=" + resultJo.toString(), "payMentLog");
////            P("支付宝提现结果=" + resultJo.toString());
//
//            //支付成功返回支付宝付款订单号
////            if (resultJo.getBoolean("success")) {
////
////                //保存付款记录并更新订单状态
////                doPaySuccessByPayChannel(PayChannelType.alipay.name(),
////                        resultJo.getString("ordersId"),
////                        aliPayeeId,
////                        aliPayeeId,
////                        po);
////
////                jo.put("success", true);
////                jo.put("code", RespTips.SUCCESS_CODE.code);
////                jo.put("msg", "提现成功!");
////                jo.put("payOrdersNo", po.getOrdersNo());
////            } else {
////                //如果支付失败
////                doPayFailByPayChannel(PayChannelType.alipay.name(), resultJo.toString(), po);
////
////                jo.put("success", false);
////                jo.put("code", RespTips.PAYCHANNLE_PAY_ERROR.code);
////                jo.put("msg", RespTips.PAYCHANNLE_PAY_ERROR.tips);
////                jo.put("data", resultJo.get("body"));
////            }
//        } else
//            //如果默认提现账户是微信
//            if (ac.getPayChannel().equals("wxpay")) {
//                //微信付款数据
//                String xmlData = WxpaySubmit.transPay(po.getOrdersNo(), ac.getPayChannelId(), po.getAmount().doubleValue(), po.getDetails(), ac.getPayChannelAccount());
//                //调起微信付款
//                JSONObject rja = WxpaySubmit.doTransPay(xmlData, LogUtil.getFileSavePath(), ac.getPayChannelAccount());
//                P("提现");
//                if (rja != null && rja.get("result_code") != null && rja.getString("result_code").equals("SUCCESS")) {
////                    doPaySuccessByPayChannel(PayChannelType.wxpay.name(), rja.getString("payment_no"), ac.getPayChannelId(), ac.getPayChannelAccount(), po.getAmount(), po);
//                    jo.put("success", true);
//                    jo.put("code", RespTips.SUCCESS_CODE.code);
//                    jo.put("msg", "提现成功!");
//                    jo.put("payOrdersNo", po.getOrdersNo());
//                } else {
//                    //如果支付失败
//                    doPayFailByPayChannel(PayChannelType.wxpay.name(), rja.toString(), po);
//                    jo.put("success", false);
//                    jo.put("code", RespTips.PAYCHANNLE_PAY_ERROR.code);
//                    jo.put("msg", RespTips.PAYCHANNLE_PAY_ERROR.tips);
//                    jo.put("data", rja.get("err_code"));
//                }
//            }
//
//        /*jo.put("success",true);
//        jo.put("payOrdersNo",po.getOrdersNo());*/
//        return jo;
//    }
//
//    @Override
//    @Transactional
//    public int payCashFail(PayOrders po, Integer userId, String approvalComment) {
//        //失败状态
//        po.setAllocationStatus(4);
//        //失败原因
//        po.setApprovalComment(approvalComment);
//        po.setUpdateTime(DateUtil.now());
//        po.setUserId(userId);
//        //订单失效
//        po.setStatus(-1);
//        payOrdersDao.save(po);
//        return 0;
//    }
//
//    private int doPayOrdersSuccessByPayChannel(String payChannel,
//                                               String payChannelNo,
//                                               String payerChannelAccount,
//                                               String payeeChannelAccount,
//                                               PayOrders payOrders) {
//
//        //保存渠道付款记录
//        if (!doSavePayOrdersLogByChannel(payChannel, payChannelNo, payerChannelAccount, payeeChannelAccount, 1, payOrders)) {
//            return 4001;
//        }
//
//        //更新支付订单状态
//        payOrders.setUpdateTime(DateUtil.now());
//        payOrders.setStatus(1);
//        payOrdersDao.save(payOrders);
//        return 0;
//    }
//
//    private int doPaySuccessByPayChannel(String payChannel,
//                                         String payChannelNo,
//                                         String payerChannelAccount,
//                                         String payeeChannelAccount,
//                                         PayOrders payOrders) {
//
//        //修改账户及收款记录
//        if (!doSaveAccountByChannel(payChannelNo, payerChannelAccount, payeeChannelAccount, payChannel, payOrders)) {
//            //状态为保存记录失败,
//            return 3001;
//        }
//
//        //保存渠道付款记录
//        if (!doSavePayOrdersLogByChannel(payChannel, payChannelNo, payerChannelAccount, payeeChannelAccount, 1, payOrders)) {
//            return 4001;
//        }
//
//        //更新支付订单状态
//        payOrders.setUpdateTime(DateUtil.now());
//        payOrders.setStatus(1);
//        payOrdersDao.save(payOrders);
//        return 0;
//    }
//
//
//    /**
//     * 付款更新账户及账户记录
//     */
//    private boolean doSaveAccountByChannel(String payChannelId,
//                                           String payerChannelAccount,
//                                           String payeeChannelAccount,
//                                           String payChannel,
//                                           PayOrders payOrders) {
//
//        //根据账户记录判断是否已经完成
//        AccountLog accountLog = accountLogDao.getByPayOrdersNo(payOrders.getOrdersNo());
//        //表示已经生成过了不用再次更新账户
//        if (accountLog != null) {
//            return false;
//        }
//
//        //取出付款账户
//        Optional<Account> op = accountDao.findById(payOrders.getPayerAccountId());
//        if(!op.isPresent()){
//            return false;
//        }
//
//        Account ac = op.get();
//
//        //订单金额
//        BigDecimal amount = payOrders.getAmount();
//
//        //渠道收款,卖家账户收款 或者会员充值
//        if (payOrders.getPayType().equals(1)) {
//            //金额保留两位小树
//            amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
//            //充值amount为正值
//            ac.setBalance(ac.getBalance().add(amount));
//        } else if (payOrders.getPayType().equals(2)) {//提现业务
//            //金额保留两位小树
//            amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
//            //充值amount为正值
//            ac.setBalance(ac.getBalance().subtract(amount));
//        }
//
//        //更新时间
//        ac.setUpdateTime(DateUtil.now());
//        //更新到用户的收款账户
//        accountDao.save(ac);
//
//        //保存平台收款记录
//        doSaveAccountRecord(payChannelId, payeeChannelAccount, payChannel, ac, amount, ac.getBalance(), payOrders.getDetails(), payOrders.getOrdersNo(), payOrders.getPayType());
//        return true;
//    }
//
//    /**
//     * 保存平台账户收付款记录
//     *
//     * @param payChannelId      支付渠道付款人账号 提现付款人为象过河账号，交押金付款人为用户
//     * @param payChannelAccount
//     * @param payChannel        支付渠道:{'qxpay':平台账号,'alipay':支付宝,'wxpay':微信支付}
//     * @param amount            交易金额 (字符串表示):{-185:表示扣款,+185.26:表示收款}
//     * @param account           平台账户
//     * @param details           交易内容注释
//     * @param ordersNo          平台交易订单号
//     * @return
//     * @author:G/2017年9月28日
//     */
//    private void doSaveAccountRecord(String payChannelId,
//                                     String payChannelAccount,
//                                     String payChannel,
//                                     Account account,
//                                     BigDecimal amount,
//                                     BigDecimal balance,
//                                     String details,
//                                     String ordersNo,
//                                     Integer payType) {
//        //保存账户交易记录
//        AccountLog ar = new AccountLog();
//        ar.setCreateTime(DateUtil.now());
//        ar.setPayChannelId(payChannelId);
//        ar.setPayChannelAccount(payChannelAccount);
//        ar.setPayChannel(payChannel);
//
//        ar.setDetails(details);
//        ar.setPayOrdersNo(ordersNo);
//        ar.setProfitType(payType);
//
//        ar.setAmount(amount);
//        ar.setBalance(balance);
//        ar.setAccount(account);
//        accountLogDao.save(ar);
//    }
//
//    /**
//     * 保存渠道收款记录
//     * {1：成功，-1：失败}
//     *
//     * @param
//     * @return
//     * @author:G/2017年9月28日
//     */
//    private boolean doSavePayOrdersLogByChannel(String payChannel,
//                                                String payChannelNo,
//                                                String payerChannelAccount,
//                                                String payeeChannelAccount,
//                                                Integer status,
//                                                PayOrders payOrders) {
//
//        PayOrdersLog payOrdersLog = new PayOrdersLog();
//        payOrdersLog.setPayChannel(payChannel);
//        payOrdersLog.setPayChannelDetails(payOrders.getSubject());
//        payOrdersLog.setPayChannelNo(payChannelNo);
//        //收款方id
//        payOrdersLog.setPayeeChannelAccount(payeeChannelAccount);
//        //付款方id
//        payOrdersLog.setPayerChannelAccount(payerChannelAccount);
//        //金额
//        payOrdersLog.setAmount(payOrders.getAmount());
//        //1为收款成功,-1为失败
//        payOrdersLog.setStatus(status);
//
//        payOrdersLog.setCreateTime(DateUtil.now());
//        payOrdersLog.setOrdersNo(payOrders.getOrdersNo());
//        payOrdersLogDao.save(payOrdersLog);
//        return true;
//    }
//
//
//    /**
//     * @param ac         付款收款账号
//     * @param amount     金额
//     * @param details    业务详情
//     * @param ordersNo   单号
//     * @param type       0加金额 1减金额
//     * @param payChannel
//     */
//    private void doUpdateAccount(Account ac,
//                                 BigDecimal amount,
//                                 String details,
//                                 String ordersNo,
//                                 String type,
//                                 String payChannel,
//                                 Integer payType) {
//        P("修改账户信息" + ac.toString() + "," + amount + "," + details + "," + ordersNo + "," + type + "," + payChannel);
//
//        BigDecimal Balance = FuncUtil.getBigDecimalScale(amount);
//        if (type.equals("0")) {
//            ac.setBalance(ac.getBalance().add(Balance));
//        } else {
//            ac.setBalance(ac.getBalance().subtract(Balance));
//        }
//        //更新时间
//        ac.setUpdateTime(DateUtil.now());
//        accountDao.save(ac);
//        doSaveAccountRecord(null, null, payChannel, ac, amount, ac.getBalance(), details, ordersNo, payType);
//    }
//
//    @Override
//    @Transactional
//    public int doReceiptByPayChannel(String channels,
//                                     String channelsNo,
//                                     String payeeId,
//                                     String payeeAccount,
//                                     String payerId,
//                                     String payerAccount,
//                                     BigDecimal amount,
//                                     int status,
//                                     PayOrders payOrders) {
//        return 0;
//    }
//
//
//    /**
//     * 通过支付渠道付款失败的业务
//     * 付款失败以后,调取本方法
//     * 操作步骤
//     * 1、修改提现用户支付订单状态 PayOrders.Status
//     * 2、保存支付渠道付款记录 PayOrdersLog
//     *
//     * @param
//     * @return
//     * @author:G/2018年07月11日
//     */
//    private int doPayFailByPayChannel(String payChannel,
//                                      String details,
//                                      PayOrders payOrders) {
//
//        //保存渠道付款记录
////        if (!doSavePayOrdersLogByChannel(payChannel, details, null, null, null, payOrders.getPayerUnit(), null, null, -1, payOrders.getOrdersNo())) {
////            //状态为保存记录失败,
////            return 4001;
////        }
//
//        //更新支付订单状态
//        payOrders.setUpdateTime(DateUtil.now());
//        payOrders.setStatus(-1);
//        payOrdersDao.save(payOrders);
//        return 0;
//    }
//
//
//    /**
//     * 支付回调
//     * <p>
//     * 1、交年费业务
//     * 2、绑定账户
//     *
//     * @param out_trade_no 交易订单号
//     * @param result_code
//     * @return
//     */
//    @Override
//    @Transactional
//    public boolean handleNotify(String out_trade_no,
//                                String trade_no,
//                                String result_code,
//                                String buyerId,
//                                String appId) {
//        //boolean flag=true;
//        P("进入service方法" + out_trade_no + "," + result_code);
//        //获取订单
//        PayOrders payOrders = payOrdersDao.findByOrderNo(out_trade_no);
//        if(CheckUtil.isEmpty(payOrders)){
//            return false;
//        }
//
//        P("根据订单号查询订单" + payOrders.getOrdersNo());
//        if (payOrders.getStatus() == 0) {   //判断订单是否已经处理过
//            if (result_code.equals("SUCCESS")
//                    || result_code.equals("TRADE_SUCCESS")
//                    ) {
//                payOrders.setStatus(1);
//                payOrders.setUpdateTime(DateUtil.now());
//
//
//                Account ac = null;
//                if (payOrders.getPayType().equals(PayType.YEAR_FEE.index)) {           //如果是交年费业务
//                    ac = accountDao.findPlatform();                                  //收款为平台账户
//                } else if (payOrders.getPayType().equals(PayType.BIND_ACCOUNT.index)) { //如果是绑定用户业务
//                    ac = accountDao.findByMemberId(payOrders.getMemberId());          //收款账户为个人账户
//                } else if (payOrders.getPayType().equals(PayType.CHANNEL_PAY.index)) {  //如果是充值业务
//                    ac = accountDao.findByMemberId(payOrders.getMemberId());
//                }
//
//                //如果账户为空
//                if (CheckUtil.isEmpty(ac)) {
//                    return false;
//                }
//
//                P("账户=" + ac.getJson());
//
//                //更新账户记录
//                doUpdateAccount(ac, payOrders.getAmount(), payOrders.getDetails(), payOrders.getOrdersNo(), "0", payOrders.getPayChannel(), payOrders.getPayType());
//
//
//                P("查询订单" + payOrders.getJson());
//
//                //如果是绑定账户业务
//                if (payOrders.getPayType().equals(PayType.BIND_ACCOUNT.index)) {
//                    P("进入添加账户" + buyerId);
//                    ac.setPayChannelId(buyerId);
//                    ac.setPayChannelAccount(appId);
//                    accountDao.save(ac);
//                }
//
//
//                //更新订单状态
//                doPayOrdersSuccessByPayChannel(PayChannelType.alipay.name(), trade_no, buyerId, buyerId, payOrders);
//
//            } else {
//                payOrders.setStatus(-1);
//                P("支付失败");
//            }
//        }
//
//        return doSendPayNotify(payOrders);
//    }
//
//
//
//
//
//    /**
//     * 返利业务：
//     * 1、商户交年费充值到平台账户余额
//     * 2、根据年费返利业务调取本接口给推广员返利
//     * <p>
//     * 业务操作:
//     * A平台账户付款 => B推广员收益账户收款
//     * 1、获取payer账户扣钱
//     * 2、获取payee账户增加收款
//     * 3、更新账户余额，保存交易日志
//     * 4、更新订单状态，保存日志订单日志
//     *
//     * @return
//     */
//    @Override
//    @Transactional
//    public JSONObject doTransferAccount(PayOrders payOrders, AllocationPay allocationPay) {
//        try {
//
//            //{1:交年费,11:返利}
//            P("支付订单类型 payType=" + payOrders.getPayType());
//            if (!payOrders.getPayType().equals(1)
//                    && !payOrders.getPayType().equals(11)
//                    ) {
//                P("当前系统仅支持年费返利和余额返利 payType=" + payOrders.getPayType());
//                return JsonUtil.get(false, RespTips.PARAM_ERROR.code, "当前系统暂时不支持该业务");
//            }
//
//            //此处应该尝试给相关账户加锁
//            P("给相关账户加锁");
//            boolean lockResult = lockUtil.lockAccountByIds(allocationPay.getAccountIdNotNull());
//            if (!lockResult) {
//                return JsonUtil.get(false, RespTips.PAY_LOCK_ERROR.code, RespTips.PAY_LOCK_ERROR.tips);
//            }
//
//            P("===========================账户加锁成功==============================");
//
//            if(payOrders.getPayType().equals(1)){
//                //根据订单返利
//                subtractAccountBalance(allocationPay.getPayerAccountId(), allocationPay.getPayerAmount(), payOrders.getOrdersNo(),12,"年费返利支出");
//            }else if(payOrders.getPayType().equals(11)){
//                subtractAccountBalance(allocationPay.getPayerAccountId(), allocationPay.getPayerAmount(), payOrders.getOrdersNo(),11,"余额返利支出");
//            }
//
//            //{0:充值(+),1:年费收入(+),2:提现(-),3:返利收益(+),10:绑定账户(+),11:余额返利支出(-),12:年费返利支出(-),20:商户收益流水(+)}
//            if (payOrders.getPayType().equals(1)
//                    || payOrders.getPayType().equals(11)) {
//                //根据返利表返利
//                addAllocationProfit(payOrders, allocationPay);
//            }
//
//            P("支付成功，更新支付订单状态");
//            doPayOrdersSuccess(payOrders, PayChannelType.ykpay.name(), payOrders.getOrdersNo(), allocationPay.getMap().toString());
//
//            P("给相关账户解锁");
//            lockUtil.unLockAccountByIds(allocationPay.getAccountIdNotNull());
//            P("================================解锁成功=====================================");
//
//            //这里不立即给出支付结果
////            return JsonUtil.get(false, RespTips.PAY_LOCK_ERROR.code, RespTips.PAY_LOCK_ERROR.tips);
//            return JsonUtil.get(true, RespTips.SUCCESS_CODE.code, RespTips.SUCCESS_CODE.tips, payOrders.getJson());
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            P("账户转账错误" + e.toString(),"error");
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            P("转账账户回滚");
////            doPayOrdersFail(payOrders);
//            return JsonUtil.get(false, RespTips.SERVER_IS_BUSY.code, e.toString());
//        }
//    }
//
//    /**
//     * 直接收益人收款及三级收益人收款
//     *
//     * @param payOrders
//     * @param allocationPay
//     */
//    private void addAllocationProfit(PayOrders payOrders,
//                                     AllocationPay allocationPay) {
//        //直接收益人
//        //{0:充值(+),1:年费收入(+),2:提现(-),3:返利收益(+),10:绑定账户(+),11:余额返利支出(-),12:年费返利支出(-),20:商户收益流水(+)}
//        if (allocationPay.getPayeeAccountId() != null && allocationPay.getPayeeAmount() != null) {
//            addAccountProfit(allocationPay, payOrders, 3,"返利收益");
//        }
//
//        //平台收益
//        //{0:充值(+),1:年费收入(+),2:提现(-),3:返利收益(+),10:绑定账户(+),11:余额返利支出(-),12:年费返利支出(-),20:商户收益流水(+)}
//        if (allocationPay.getPlatformAccountId() != null && allocationPay.getPlatformAmount() != null) {
//            addAccountProfit(allocationPay.getPlatformAccountId(), allocationPay.getPlatformAmount(), payOrders.getOrdersNo(),"返利收益", 3);
//        }
//    }
//
//
//    /**
//     * 更新支付订单
//     * 1、更新支付订单状态
//     * 2、生成支付订单记录
//     *
//     * @param payOrders
//     * @param payChannelDetails
//     */
//    private void doPayOrdersSuccess(PayOrders payOrders,
//                                    String payChannel,
//                                    String payChannelNo,
//                                    String payChannelDetails) {
//        //更新支付订单状态
//        payOrders.setUpdateTime(DateUtil.now());
//        payOrders.setAllocationStatus(1);
//        payOrders.setStatus(1);
//        payOrdersDao.save(payOrders);
//        P("保存支付订单成功=" + payOrders.getOrdersNo());
//
//
//        //生成支付成功记录
//        PayOrdersLog payOrdersLog = new PayOrdersLog(payOrders, payChannel, payChannelNo, payChannelDetails, 1);
//        P("创建支付订单记录成功=" + payOrders.getOrdersNo());
//        payOrdersLogDao.save(payOrdersLog);
//        P("保存支付订单记录成功");
//    }
//
//    private void doPayOrdersFail(PayOrders payOrders) {
//        //更新支付订单状态
//        payOrders.setUpdateTime(DateUtil.now());
//        payOrders.setStatus(-1);
//        payOrdersDao.save(payOrders);
//    }
//
//
//    /**
//     * 平台账户付款业务
//     * 1、先从账户收益支付
//     * 2、如果不够付从余额支付
//     */
//    private void subtractAccountBalance(Long payerAccountId,
//                                        BigDecimal amount,
//                                        String ordersNo,
//                                        Integer profitType,
//                                        String details
//    ) {
//        //获取付款账户
//        Optional<Account> op = accountDao.findById(payerAccountId);
//        if(!op.isPresent()){
//            return;
//        }
//
//        Account payerAccount = op.get();
//        //订单需要支付的金额
//
//        P("需要支付的总金额=" + amount);
//
//        //如果账户收益可以支付
//        if (payerAccount.getProfitBalance().compareTo(amount) >= 0) {
//            P("收益余额大于支出额，从收益账户扣减金额=" + amount);
//            //直接从收益账户扣减
//            payerAccount.setProfitBalance(payerAccount.getProfitBalance().subtract(amount));
//            P("剩余金额=" + amount);
//            amount = FuncUtil.ZERO;
//        } else {
//            P("收益余额小于支出额，从收益账户扣减全部金额=" + payerAccount.getProfitBalance());
//            amount = amount.subtract(payerAccount.getProfitBalance());
//            P("账户收益余额清零");
//            payerAccount.setProfitBalance(FuncUtil.ZERO);
//        }
//
//        P("剩余金额，从余额账户扣减金额=" + amount);
//        if (FuncUtil.gtZero(amount)) {
//            payerAccount.setBalance(payerAccount.getBalance().subtract(amount));
//        }
//
//        //更新账户时间
//        payerAccount.setUpdateTime(DateUtil.now());
//        accountDao.save(payerAccount);
//        AccountLog accountLog = new AccountLog();
//        accountLog.setPayOrdersNo(ordersNo);
//        accountLog.setDetails(details);
//        accountLog.setAccount(payerAccount);
//        accountLog.setAmount(amount);
//        accountLog.setBalance(payerAccount.getBalance());
//
//        accountLog.setPayChannel(PayChannelType.ykpay.name());
//        accountLog.setCreateTime(DateUtil.now());
//        accountLog.setProfitType(profitType);
//        accountLogDao.save(accountLog);
//    }
//
//
//    //平台账户增加收益
//    private void addAccountProfit(Long payeeAccountId,
//                                  BigDecimal amount,
//                                  String ordersNo,
//                                  String details,
//                                  Integer profitType) {
//        //获取付款账户
//        Optional<Account> op = accountDao.findById(payeeAccountId);
//        if(!op.isPresent()){
//            return;
//        }
//
//        Account payeeAccount = op.get();
//        //增加钱包余额
//        payeeAccount.setBalance(payeeAccount.getBalance().add(amount));
//        //更新账户时间
//        payeeAccount.setUpdateTime(DateUtil.now());
//        accountDao.save(payeeAccount);
//
//        //创建日志
//        AccountLog accountLog = new AccountLog();
//        accountLog.setPayOrdersNo(ordersNo);
//        accountLog.setDetails(details);
//        accountLog.setAccount(payeeAccount);
//        accountLog.setAmount(FuncUtil.getBigDecimalScale(amount));
//        accountLog.setBalance(FuncUtil.getBigDecimalScale(payeeAccount.getBalance()));
//        //日志添加返利类型
//        accountLog.setProfitType(profitType);
//
//        accountLog.setPayChannel(PayChannelType.ykpay.name());
//        accountLog.setPayChannelId(payeeAccountId.toString());
//        accountLog.setCreateTime(DateUtil.now());
//        accountLogDao.save(accountLog);
//
//        //发送收益通知(如果是平台不用发送)
//        if (!CheckUtil.isEmpty(payeeAccount.getMember())) {
////            messageServ.saveMessage("", "收益到账通知", details, Message.Type.GETPROFIT.code, payeeAccount.getMember().getId());
//        }
//    }
//
//    private void addAccountProfit(AllocationPay allocationPay, PayOrders payOrders, Integer profitType,String details) {
//        //获取付款账户
//        Optional<Account> op = accountDao.findById(allocationPay.getPayeeAccountId());
//        if(!op.isPresent()){
//            return;
//        }
//
//        Account payeeAccount = op.get();
//
//
//        //增加钱包余额
//        BigDecimal amount = allocationPay.getPayeeAmount();
//        payeeAccount.setBalance(payeeAccount.getBalance().add(amount));
//
//        //更新账户时间
//        payeeAccount.setUpdateTime(DateUtil.now());
//        accountDao.save(payeeAccount);
//
//        //创建日志
//        AccountLog accountLog = new AccountLog();
//        accountLog.setPayOrdersNo(payOrders.getOrdersNo());
//        accountLog.setDetails(details);
//        accountLog.setAccount(payeeAccount);
//        accountLog.setAmount(FuncUtil.getBigDecimalScale(amount));
//        accountLog.setBalance(FuncUtil.getBigDecimalScale(payeeAccount.getBalance()));
//        //日志添加返利类型
//        accountLog.setProfitType(profitType);
//
//        accountLog.setPayChannel(PayChannelType.ykpay.name());
//        accountLog.setPayChannelId(payeeAccount.getId().toString());
//        accountLog.setCreateTime(DateUtil.now());
//        accountLogDao.save(accountLog);
//
//    }
//
//
//    /**
//     * 调起微信付款参数
//     *
//     * @param payOrders
//     * @author G/2018/7/10 16:29
//     */
//    @Override
//    public JSONObject getWxpayAppReceipt(PayOrders payOrders, String terminal, String openid) {
//        JSONObject jo = new JSONObject();
//        //单位由元转换成分
////        BigDecimal amount=payOrders.getAmount();
//        P("参数" + terminal);
//        if (!CheckUtil.isEmpty(openid)) {
//            P("参数" + openid);
//        } else {
//            P("没有参数");
//        }
//
//
//        //元转分
//        BigDecimal amount = payOrders.getAmount().multiply(new BigDecimal("100"));
//        String xmlData = "";
//        if (terminal.equals("app")) {
//            //获取对应的app绑定的微信支付信息
//            xmlData = WxpaySubmit.ReceiveAppData(payOrders.getSubject(), payOrders.getOrdersNo(), amount.doubleValue());
//        } else if (terminal.equals("web")) {
//            xmlData = WxpaySubmit.ReceiveNativeData(payOrders.getSubject(), payOrders.getOrdersNo(), amount.doubleValue());
//        } else if (terminal.equals("JSAPI")) {
//            xmlData = WxpaySubmit.ReceiveJsapiData(payOrders.getSubject(), payOrders.getOrdersNo(), amount.doubleValue(), openid);
//        }
//
//        P("微信生成提交数据" + xmlData);
//
//        //微信预支付
//        JSONObject rjo = WxpaySubmit.getReceive(xmlData);
//        if (rjo != null && rjo.get("prepay_id") != null) {
//            jo.put("payOrdersNo", payOrders.getOrdersNo());
//            jo.put("orderNo", payOrders.getOrdersNo());
//            if (terminal.equals("app")) {
//                //赋值微信支付信息
//                jo.put("appPayString", WxpaySubmit.ResignForApp(rjo.getString("prepay_id")));
//                //JSONObject jsonObject=WxpaySubmit.ResignForApp(rjo.getString("prepay_id"));
//                //jsonObject.put("appPayString",WxpaySubmit.ResignForApp(rjo.getString("prepay_id")));
//            } else if (terminal.equals("web")) {
//                if (rjo != null && rjo.get("return_code").equals("SUCCESS")) {
//                    jo.put("appPayString", rjo.getString("code_url"));
//                } else {
//                    jo.put("appPayString", rjo.getString("err_code"));
//                }
//            } else {
//                P("微信返回参数" + rjo.toString());
//                SortedMap<String, String> finalpackage = new TreeMap<String, String>();
//                String timeStamp = getTimeStamp();
//                String prepay_id2 = "prepay_id=" + rjo.getString("prepay_id");
//                finalpackage.put("appId", WxpayConfig.GZHAPPID);
//                finalpackage.put("timeStamp", getTimeStamp());
//                finalpackage.put("nonceStr", rjo.getString("nonce_str"));
//                finalpackage.put("package", prepay_id2);
//                finalpackage.put("signType", "MD5");
//                String finalsign = WxpaySubmit.createSign(finalpackage);
//                System.out.println(finalsign);
//                finalpackage.put("sign", finalsign);
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("appid", finalpackage.get("appId"));
//                jsonObject.put("nonce_str", finalpackage.get("nonceStr"));
//                jsonObject.put("package", finalpackage.get("package"));
//                jsonObject.put("sign", finalpackage.get("sign"));
//                jsonObject.put("timeStamp", timeStamp);
//                jsonObject.put("signType", "MD5");
//                jo.put("appPayString", jsonObject);
//                P("微信返回参数" + jo.toString());
//            }
//        } else {
//            jo.put("success", false);
//            jo.put("code", RespTips.PAYCHANNLE_PAY_ERROR);
//        }
//        return jo;
//    }
//
//    @Override
//    public JSONObject doPayment(String payOrdersNo, String uid) {
//        return null;
//    }
//
//
//    /**
//     * 系统自动读取数据库，获取订单时间为5分钟或十分钟，
//     * 由此产生的取消订单误差(倒计时到了没有取消订单)
//     * 解决方案读取数据库间隔内的订单不抛弃，而加入倒计时向量
//     * 此方法将接近完成的订单加入一个临时向量
//     *
//     * @author:G/2017年10月31日
//     * @param
//     * @return
//     */
//    private static TreeMap<Long, PayOrders> shortTimeOrders = new TreeMap<>();
//
//    private void addToShortTimeVector(PayOrders o) {
//        P("加入倒计时的订单id=" + o.getId());
//        shortTimeOrders.put(o.getId(), o);
//    }
//
//    /**
//     * 从倒计时订单向量删除订单
//     * 比如倒计时间隔,订单进行到下一步时,应该从倒计时向量删除
//     */
//    private void deleteShortTimeVector(Long oid) {
//        P("从倒计时删除的订单id=" + oid);
//        shortTimeOrders.remove(oid);
//    }
//
//    public static String getTimeStamp() {
//        return String.valueOf(System.currentTimeMillis() / 1000);
//    }
//
//
//    /**
//     * 发送支付通知到子系统
//     *
//     * @param po
//     * @author G/2018/7/13 16:39
//     */
//    private boolean doSendPayNotify(PayOrders po) {
//        P("进入修改订单信息" + po.getOrdersNo());
//        boolean flag = true;
//        P("进入修改订单业务完成" + flag);
//        return flag;
//    }
//
//
//    /**
//     * 保存异步通知结果
//     *
//     * @param po
//     * @param status
//     * @author G/2018/7/13 15:50
//     */
//    private void doPayRecivedByApplication(PayOrders po, int status) {
//        if (status == 1) {
//            po.setPayNotifyStatus(1);
//        } else {
//            po.setPayNotifyTimes(po.getPayNotifyTimes() + 1);
//        }
//        po.setUpdateTime(DateUtil.now());
//        payOrdersDao.save(po);
//    }
//
//    /**
//     * 自动查询申请的微信退款订单(微信退款没有自动通知，平台内需要定时查询)
//     * 1、退款提交到微信后，SuccessNum="0"。
//     * 2、系统定时查找TradeRefund中，支付渠道为payChannels="wxpay" 并且 SuccessNum="0"(退款成功未确认)的 notifyid(refund_id) OutTradeNo(平台单号)
//     * 3、根据refund_id 和 OutTradeNo 到微信财付通查询退款是否成功
//     * 4、如果成功则更新TradeRefund.SuccessNum="1";
//     * 5、修改订单状态
//     *
//     * @author G/2018/06/30
//     */
//    private void doAutoRefundQuery() {
//
//    }
//
//
//    @Override
//    public PayOrders findPayOrders(Long id, String number, String ordersNo) {
//        return null;
//    }
//
//    @Override
//    public JSONObject findPayOrdersJson(Long id, String number, String ordersNo) {
//        return null;
//    }
//
//    @Override
//    public JSONObject findByPayOrders(Finder f, Long memberId) {
//        Page page = findAll(f, memberId);
//        return JsonUtil.get(true,
//                RespTips.SUCCESS_CODE.code,
//                ((List<PayOrders>) page.getItems()).stream().map(PayOrders::getJson).toArray(),
//                page.getTotalCount()
//        );
//    }
//
//    @Override
//    public JSONObject findByPayOrders(Finder f) {
//        Page page = find(f);
//        List<Map<String, Object>> map = (List<Map<String, Object>>) page.getItems();
//        return JsonUtil.get(true, RespTips.SUCCESS_CODE.code, map.stream().toArray(), page.getTotalCount());
//    }
//
//    @Override
//    public JSONObject findByPayOrdersLog(Finder f) {
//        return null;
//    }
//
//    @Override
//    public PayOrders findByOrderNo(String orderNo) {
//        return payOrdersDao.findByOrderNo(orderNo);
//    }
//
//    @Override
//    public PayOrders findFirstByAppidOrderNo(String appid, String ordersNo) {
//        return payOrdersDao.findFirstByAppidAndOrdersNo(appid, ordersNo);
//    }
//
//    //平台查询
//    private Page find(Finder f) {
//        //统计推广员数量
//        StringJoiner countSql = new StringJoiner(" ");
//        countSql.add("select count(po.id)")
//                .add("from pay_orders po left join member m on po.member_id = m.id");
//
//
//        //数据集合
//        StringJoiner listSql = new StringJoiner(" ");
//        listSql.add("select po.id as id,")
//                .add("po.subject as subject,")
//                .add("po.amount as amount,")
//                .add("ac.balance as balance,")
//                .add("po.orders_no as ordersNo,")
//                .add("po.details as details,")
//                .add("DATE_FORMAT(po.create_time, '%Y-%m-%d %H:%i:%s') as createTime,")
//                .add("DATE_FORMAT(po.update_time, '%Y-%m-%d %H:%i:%s') as updateTime,")
//                .add("po.pay_type as payType,")
//                .add("po.status as status,")
//                .add("po.approval_status as approvalStatus,")
//                .add("po.approval_comment as approvalComment,")
//                .add("m.name as promoterName,")
//                .add("m.area_name as areaName")
//                .add("from pay_orders po ")
//                .add("left join member m on po.member_id = m.id")
//                .add("left join account ac on ac.member_id = m.id");
//
//        //查询条件
//        StringJoiner sql = new StringJoiner(" ");
//        sql.add("where 1=1 ");
//
//        if (!CheckUtil.isEmpty(f)) {
//            if (!CheckUtil.isEmpty(f.getStatus())) {
//                if (f.getStatus().equals(4)) {                //表示审核过的(等于-1:失败,2:通过并支付的)
//                    sql.add(" and (po.status != 0 )");
//                } else {
//                    sql.add(" and (po.status = " + f.getStatus() + ")");
//                }
//            }
//            if (!CheckUtil.isEmpty(f.getScop())) {
//                sql.add(" and (po.pay_type = " + f.getScop() + ")");
//            }
//
//            if (!CheckUtil.isEmpty(f.getStartTime())) {
//                sql.add(" and (po.update_time >= '" + f.getStartTime() + "') ");
//            }
//
//            if (!CheckUtil.isEmpty(f.getEndTime())) {
//                sql.add(" and (po.update_time <= '" + f.getEndTime() + "') ");
//            }
//
//            if (!CheckUtil.isEmpty(f.getKeyword())) {    //推广员、区域
//                sql.add(" and (" +
//                        "locate('" + f.getKeyword() + "',po.orders_no)>0 " +
//                        "or locate('" + f.getKeyword() + "',po.details)>0 " +
//                        "or locate('" + f.getKeyword() + "',m.name)>0 " +
//                        "or locate('" + f.getKeyword() + "',m.area_name)>0 " +
//                        "or locate('" + f.getKeyword() + "',po.subject)>0 " +
//                        ") ");
//            }
//        }
//
//
//        //统计总数量
//        Long totalCount = payOrdersDao.getCountBySql(countSql.merge(sql).toString());
//
//        sql.add(" order by po.update_time desc");
//
//        List<Map<String, Object>> items = payOrdersDao.findMapBySql(listSql.merge(sql).toString(), f.getPageSize(), f.getStartIndex());
//        //返回分页数据
//        return new Page(items, totalCount, f.getPageSize(), f.getStartIndex());
//    }
//
//    private Page findAll(Finder f, Long memberId) {
//        //统计数量
//        String countHql = "select count(distinct po) from PayOrders po";
//
//        //数据集合
//        String listHql = "select distinct po from PayOrders po ";
//
//        //查询条件
//        String hql = " where 1=1 ";
//        if (!CheckUtil.isEmpty(memberId)) {
//            hql = " where po.memberId = " + memberId;
//        }
//
//        if (!CheckUtil.isEmpty(f)) {
//            if (!CheckUtil.isEmpty(f.getStatus())) {
//                if (f.getStatus().equals(4)) {                //表示审核过的(等于-1:失败,2:通过并支付的)
//                    hql += " and (po.status != 0 )";
//                } else {
//                    hql += " and (po.status = " + f.getStatus() + ")";
//                }
//            }
//            if (!CheckUtil.isEmpty(f.getScop())) {
//                hql += " and (po.payType = " + f.getScop() + ")";
//            }
//
//            if (!CheckUtil.isEmpty(f.getStartTime())) {
//                hql += " and (po.updateTime >= '" + f.getStartTime() + "') ";
//            }
//
//            if (!CheckUtil.isEmpty(f.getEndTime())) {
//                hql += " and (po.updateTime <= '" + f.getEndTime() + "') ";
//            }
//
//            if (!CheckUtil.isEmpty(f.getKeyword())) {    //推广员、区域
//                hql += " and (" +
//                        "locate('" + f.getKeyword() + "',po.ordersNo)>0 " +
//                        "or locate('" + f.getKeyword() + "',po.details)>0 " +
//                        "or locate('" + f.getKeyword() + "',po.subject)>0 " +
//                        ") ";
//            }
//        }
//
//
//        //统计总数量
//        Long totalCount = payOrdersDao.getCountByHql(countHql + hql);
//
//        hql += " order by po.updateTime desc";
//
//        //获取集合
//        List<PayOrders> items = payOrdersDao.findListByHql(listHql + hql, f.getPageSize(), f.getStartIndex());
//
//        //返回分页数据
//        return new Page(items, totalCount, f.getPageSize(), f.getStartIndex());
//
//    }
//
//
//}
