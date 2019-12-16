package com.bcb.pay.api;

import com.bcb.base.Finder;
import com.bcb.pay.dto.RefundOrderDto;
import com.bcb.pay.entity.PayAccount;
import com.bcb.pay.entity.PayOrder;
import com.bcb.pay.entity.RefundOrder;
import com.bcb.pay.service.PayAccountServ;
import com.bcb.pay.service.PayChannelAccountServ;
import com.bcb.pay.service.PayOrderServ;
import com.bcb.pay.service.RefundOrderServ;
import com.bcb.util.CheckUtil;
import com.bcb.util.FuncUtil;
import com.bcb.util.LockUtil;
import com.bcb.util.RespTips;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Author G.
 * @Date 2019/11/27 0027 下午 1:59
 */

@RestController
public class RefundApi extends AbstractController {
    @Autowired
    private PayOrderServ payOrderServ;
    @Autowired
    private RefundOrderServ refundOrderServ;
    @Autowired
    private PayAccountServ payAccountServ;

    /**
     * 退款接口
     * @param refundOrderDto
     */
    @RequestMapping(value = "/pay/refund")
    public void refund(RefundOrderDto refundOrderDto) {
        if (CheckUtil.isEmpty(refundOrderDto)
                ||CheckUtil.isEmpty(refundOrderDto.getSubject())
                ||CheckUtil.isEmpty(refundOrderDto.getAmount())
                ||CheckUtil.isEmpty(refundOrderDto.getOrderNo())
                ||CheckUtil.isEmpty(refundOrderDto.getRefundOrderNo())
                ||CheckUtil.isEmpty(refundOrderDto.getUnitId())){

            sendParamError();
            return;
        }

        //判断是否有对应的而且成功的收款单号
        PayOrder payOrder = payOrderServ.getSuccessByOrderNo(refundOrderDto.getOrderNo());
        if(null == payOrder){
            sendJson(false, RespTips.PAYORDER_NOTFINISHED.code,RespTips.PAYORDER_NOTFINISHED.tips);
            return;
        }

        //退款金额不能大于订单收款金额
        if(FuncUtil.getBigDecimalScale(payOrder.getAmount()).compareTo(FuncUtil.getBigDecimalScale(new BigDecimal(refundOrderDto.getAmount())))<0){
            sendJson(false, RespTips.PAYACCOUNT_REFUND_LESS.code,RespTips.PAYACCOUNT_REFUND_LESS.tips);
            return;
        }

        //判断商户基本账户是否被禁用
        PayAccount payAccount = payAccountServ.findByUnitId(refundOrderDto.getUnitId());
        if(null==payAccount
                || !payAccount.isUseable()){
            sendJson(false,RespTips.PAYACCOUNT_IS_UNAVAILABLE.code,RespTips.PAYACCOUNT_IS_UNAVAILABLE.tips);
            return;
        }

        //获取退款结果立即返回,退款是否成功提供退款查询接口
        int r = refundOrderServ.refundByUnit(payAccount.getId(),payOrder, refundOrderDto);
        //如果没有对应的渠道
        if(r==0){
            //退款成功
            sendSuccess();
        }else if(r==-1){
            sendJson(false, RespTips.PAYCHANNLE_PAY_FINISHED.code,RespTips.PAYCHANNLE_PAY_FINISHED.tips);
        }else if(r==1){
            sendJson(false, RespTips.PAYORDER_FOUND.code,RespTips.PAYORDER_FOUND.tips);
        }else if(r==2){
            sendJson(false,RespTips.PAYORDER_LOCK_ERROR.code,RespTips.PAYORDER_LOCK_ERROR.tips);
        }else if(r>2){
            sendFail();
        }
    }

    @RequestMapping(value = "/pay/refund/query")
    public void query(String refundOrderNo){
        if(CheckUtil.isEmpty(refundOrderNo)){
            sendParamError();
            return;
        }
        //查找对应的退款单
        RefundOrder refundOrder = refundOrderServ.findByOrderNo(refundOrderNo);
        if(null==refundOrder){
            sendJson(false,RespTips.PAYORDER_NOTFOUND.code,RespTips.PAYORDER_NOTFOUND.tips);
            return;
        }

        //如果退款单的状态为1
        if(refundOrder.getStatus().equals(1)){
            //返回退款成功
            sendJson(true,RespTips.PAYORDER_FINISHED.code,RespTips.PAYORDER_FINISHED.tips);
            return;
        }

        //查询退款
        int r = refundOrderServ.queryRefundOrderByUnit(refundOrder);
        //如果返回失败
        if(r==1){
            sendJson(false,RespTips.PAYORDER_NOTFINISHED.code,RespTips.PAYORDER_NOTFINISHED.tips);
            return;
        }

        //返回退款成功
        sendJson(true,RespTips.PAYORDER_FINISHED.code,RespTips.PAYORDER_FINISHED.tips);
    }

    @RequestMapping(value = "/refundOrder/find")
    public void payOrderFind(String unitId, String payChannel, Finder finder) {
        if (CheckUtil.isEmpty(unitId)) {
            sendParamError();
            return;
        }
        //如果成功
        sendJson(refundOrderServ.findRefundOrderByUnit(finder,unitId,payChannel));

    }
}
