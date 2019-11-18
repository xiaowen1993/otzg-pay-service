package com.bcb.pay.service;

import com.bcb.base.Finder;
import com.bcb.pay.entity.Account;
import com.bcb.pay.entity.PayOrders;
import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付业务
 * <p>
 * 前置：
 * 1、会员账户状态正常
 * 2、付款业务金额不能大于余额
 * <p>
 * 操作：
 * 1、生成支付订单
 * 2、锁定账户(账户状态置为5)
 * 3、调起支付
 * <p>
 * 后置：
 * 1、支付成功 => 账户解锁 => 生成支付记录 => 生成账户记录
 * 2、支付失败 => 账户解锁 => 生成失败记录
 * 3、异常处理 => 账户解锁
 *
 * @author G/2018/6/30 11:29
 */

public interface PayOrdersServ {

    PayOrders findByMemberAndType(Long payerMemberId, Integer payType);

    /**
     * 判断是否有没执行的订单
     * @param payerMemberId
     * @param payType
     * @return
     */
    PayOrders findWaitByMemberAndType(Long payerMemberId,Integer payType);


    /**
     * 创建提现申请订单
     */
    Map getCashApply(String appid,
                     String subject,
                     BigDecimal amount,
                     String details,
                     Integer payerUserId,
                     Long payerMemberId,
                     Long payerAccountId,
                     Long payeeAccountId,
                     String payChannel);
    /**
     * 后台同意提现
     */
    JSONObject payCash(PayOrders po, Account ac, Integer userId);

    /**
     * 后台不同意提现
     */
    int payCashFail(PayOrders po, Integer id,String approvalComment);

    /**
     * 获取年费的到期时间
     * 上次交年费的日期+365天
     * @return
     */
    String findYearFeeEndDate(Long memberId);


    /**
     * 获取订单
     *
     * @param f
     * @author G/2018/6/30 11:43
     */
    JSONObject findByPayOrders(Finder f);

    /**
     * 获取会员的订单
     * @param f
     * @param memberId
     * @return
     */
    JSONObject findByPayOrders(Finder f,Long memberId);



    /**
     * 接收支付渠道异步通知支付结果
     * 用于微信或支付宝异步通知收款结果
     * 前置:
     * 1、根据channelsNo 判断此交易是否已经完成,完成返回已完成,未完成继续
     * 操作:
     * 1、保存渠道收款记录
     * 2、保存内部账户收款记录
     * 3、修改内部账户余额
     * 4、修改支付订单状态
     *
     * @param channels     支付渠道
     * @param channelsNo   支付渠道交易号
     * @param payeeId      收款方id
     * @param payeeAccount 收款方账号
     * @param payerId      付款方id
     * @param payerAccount 付款方账号
     * @param amount       支付金额
     * @param status       完成状态
     * @param payOrders    支付订单
     * @return 返回错误码
     * @author:G/2018/06/30
     */
    int doReceiptByPayChannel(String channels,
                              String channelsNo,
                              String payeeId,
                              String payeeAccount,
                              String payerId,
                              String payerAccount,
                              BigDecimal amount,
                              int status,
                              PayOrders payOrders);

    /**
     * 会员账户充值
     * 会员充值 : payType=0    自己的账户余额增加
     *
     * @param appid
     * @param subject
     * @param amount
     * @param details
     * @param payChannel
     * @param terminal   付款终端 app web
     * @author G/2018/7/10 11:41
     */
    JSONObject recharge(String appid,
                        String subject,
                        BigDecimal amount,
                        String details,
                        Long memberId,
                        Long payeeAccountId,
                        String payChannel,
                        String terminal,
                        String openid,
                        Integer payType);

    /**
     * 调起微信支付收款数据
     * 用于用户缴纳押金和充值
     * 前置:
     * 1、判断该笔订单未支付过(订单状态为0)
     * 操作:
     * 1、根据支付订单生成支付数据
     * 2、生成支付宝支付数据
     *
     * @param payOrders 支付订单id
     * @param
     * @return
     * @author:G/2018/06/30
     * @author:G/2018/06/30
     */
    JSONObject getWxpayAppReceipt(PayOrders payOrders, String terminal, String openid);


    /**
     * 调起微信或支付宝付款
     * 应用场景：用于用户提现和退押金
     * 关键:
     * 1、在一个实例里面，防止用户并发调用
     * 2、在多个实例里面，防止数据库并发操作
     * <p>
     * 前置:
     * 1、判断用户提现账户状态正常(账户状态正常，没锁定)
     * 2、平台账户余额大于提现金额
     * 操作:
     * 1、锁定付款账户
     * 2、根据支付订单生成支付数据
     * 3、调取支付接口
     * 后置:
     * 1、支付成功=>更新账户余额=>解锁账户
     * 2、支付失败=>解锁账户
     * 3、异常通知后台管理员
     *
     * @param payOrdersNo 支付订单id
     * @param uid         用户单位id
     * @return
     * @author:G/2018/06/30
     */
    JSONObject doPayment(String payOrdersNo, String uid);


    //=====================================系统自动执行业务===========================================//

    /**
     * 系统自动执行业务
     * 1、查询支付成功的支付知子系统支付结果
     * 2、查询退款业务是否成功
     * @author G/2018/7/10 16:43
     * @param
     */
    //void doAutoPayServ();

    //void doAutoStepShortTime();

    //=====================================后台管理业务===========================================//

    PayOrders findByOrderNo(String orderNo);

    /**
     * 根据业务单号获取支付订单
     * 应用场景:
     * 1:支付结果返回后=>根据业务单号调出支付订单=>更新
     * 2:创建支付单据时
     *
     * @param id
     * @param number
     * @param ordersNo
     * @author G/2018/6/30 9:27
     */
    PayOrders findPayOrders(Long id, String number, String ordersNo);

    /**
     * 根据业务订单号获取支付订单
     *
     * @param id
     * @param number
     * @param ordersNo
     * @author G/2018/6/30 9:28
     */
    JSONObject findPayOrdersJson(Long id, String number, String ordersNo);

    /**
     * 获取支付订单记录
     *
     * @param f
     * @author G/2018/6/30 11:44
     */
    JSONObject findByPayOrdersLog(Finder f);

    PayOrders findFirstByAppidOrderNo(String appid, String ordersNo);

    /**
     * 处理回调
     *
     * @param out_trade_no 交易订单号
     * @return
     */
    boolean handleNotify(String out_trade_no,String trade_no, String result_code, String buyerId,String appId);
}
