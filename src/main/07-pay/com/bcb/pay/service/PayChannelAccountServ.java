package com.bcb.pay.service;

import com.bcb.pay.entity.PayChannelAccount;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 支付渠道账户
 */
public interface PayChannelAccountServ {

    /**
     * 单位账户绑定支付渠道账号
     * 1、禁止更新账户其他信息
     * 2、一个会员单位只能有一个账户
     *
     * @param payChannelId          微信对应openid,支付宝对应16位的会员id
     * @param payChannelAccount     对应支付宝账号(支付宝登录账号包含邮箱号和手机号),微信对用mchid(公众号商户号|app商户号
     * @param payChannelAccountName 支付渠道账号名称
     * @param payChannel            aplipay:支付宝,wxpay:微信支付,xghpay:象过河支付
     * @author G/2018/6/29 12:18
     */
    int savePayChannelAccount(String payChannelId,
                              String payChannelAccount,
                              String payChannelAccountName,
                              String payChannel,
                              String memberId);


    /**
     * 保存一条收款信息
     * 1.更新收款账户余额及发生额
     * 2.保存收款账户记录
     *
     * @param unitId
     * @param payOrderNo
     * @param subject
     * @param payChannel
     * @param payChannelNo
     * @param amount
     * @return
     */
    int addPayAccount(String unitId, String payOrderNo, String subject, String payChannel, String payChannelNo, BigDecimal amount);

    //根据基本账户获取支付渠道账户信息
    PayChannelAccount findByAccountAndPayChannel(Long payAccountId, String payChannel);

    /**
     * 完成付款业务
     * 1.付款业务第二阶段处理
     * 2.删除解冻金额，
     * 3.成功则生成出账记录
     * 4.不成功则返还余额
     *
     * @param payOrderNo
     * @param subject
     * @param payChannelNo
     * @param amount
     * @return
     */
    boolean substract(PayChannelAccount payChannelAccount,String unitId,String payChannel,String payOrderNo, String subject, String payChannelNo, BigDecimal amount);


    /**
     * 冻结付款余额
     * 1.第一阶段账户预处理业务
     * 2.账户余额减少
     * 3.冻结金额增加
     */
    boolean freezeBalance(PayChannelAccount payChannelAccount, BigDecimal freezeAmount);

    /**
     * 解冻冻结金额
     * 1.删除冻结金额
     * 2.如果tag为true,余额增加。
     */
    boolean unFreezeBalance(PayChannelAccount payChannelAccount, BigDecimal freezeAmount, boolean tag);
}


