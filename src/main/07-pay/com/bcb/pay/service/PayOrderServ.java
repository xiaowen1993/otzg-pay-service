package com.bcb.pay.service;

import com.bcb.base.Finder;
import com.bcb.pay.dto.PayOrderDto;
import com.bcb.pay.entity.PayAccount;
import com.bcb.pay.entity.PayOrder;
import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 收单业务接口
 * 调用支付渠道进行收单业务，为两阶段事务。
 *
 * 前置：
 * 1、商户账户状态正常
 * 2、商户收款渠道账户是否正常，(如：微信收款是否已创建微信收款账户)
 *
 * 操作：
 * 1、生成支付支付单
 * 2、调起支付
 * 3、支付单状态置为未成功
 *
 * 后置：
 * 1.等待支付成功回调结果
 * 2.如果支付成功 支付单状态置为成功
 *
 * @author G/2018/6/30 11:29
 */

public interface PayOrderServ {

    /**
     * 前置：
     * 1.根据支付渠道账户获取的子商户支付渠道账户
     * 2.校验提交的参数
     *
     * 操作
     * 1.生成支付单号
     * 2.生成支付单(状态未支付status=0)
     * 3.调起支付渠道接口
     *
     * 返回：
     * 1.根据支付类型返回支付支付信息
     *
     * @param payChannelAccount 微信对应subMchId,支付宝对应pid
     * @param payOrderDto
     * @return
     */
    Map createPayOrderByUnit(String payChannelAccount,PayOrderDto payOrderDto);


    /**
     * 根据payType参数返回对应的收单业务
     * @param unitId
     * @param payType
     * @return
     */
    PayOrder findByUnitAndType(String unitId, String payType);

    /**
     * 根据payType参数返回对应的收单业务
     * @param unitId
     * @param payType
     * @return
     */
    PayOrder findWaitByUnitAndType(String unitId, String payType);

    /**
     * 支付单查询
     * @param unitId
     * @param payOrderNo
     * @return
     */
    PayOrder findByPayOrderNo(String unitId, String payOrderNo);

    /**
     * 根据子系统的支付业务单查询是否已经创建
     * @param unitId
     * @param orderNo
     * @return
     */
    boolean checkByOrderNo(String unitId,String orderNo);

    /**
     * 判断是否收款成功
     * @param orderNo
     * @return
     */
    PayOrder getSuccessByOrderNo(String orderNo);

    /**
     * 查询商户的支付单
     * @param f
     * @param unitId
     * @return
     */
    JSONObject findPayOrderByUnit(Finder f,String unitId);


    /**
     * 处理支付回调业务
     * 1.支付成功：更新支付单状态：payOrder.status=1
     * 2.回调子系统：支付成功。
     * @param outTradeNo
     * @param transactionId
     * @param resultCode
     * @param openId
     * @param mchId
     * @return
     */
    boolean handleNotify(String outTradeNo, String transactionId, String resultCode, String openId, String mchId);

}
