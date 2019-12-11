package com.bcb.pay.util;

import com.bcb.pay.dto.PayOrderDto;
import com.bcb.util.RespTips;

import java.util.Map;

/**
 * @Author G.
 * @Date 2019/12/10 0010 上午 9:29
 */
public class PayOrderDtoAlipayCheck extends PayOrderDtoCheck{
    public PayOrderDtoAlipayCheck(PayOrderDto t) {
        super(t);
    }

    @Override
    public Map getMsg() {
        return super.getMsg();
    }

    @Override
    protected void check() {
        super.check();

        if (!PayOrderDtoAlipayCheck.TradeType.has(t.getPayType())) {
            msg = "支付宝支付类型(payType)参数错误";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }
        if (t.getPayType().equals(TradeType.BARCODE.name())             //支付宝扫码付
                || t.getPayType().equals(TradeType.FACE.name())) {      //支付宝刷脸付
            barCodePayCheck();
        }else if (t.getPayType().equals(TradeType.CREATE.name())) {      //支付宝线上与支付
            createPayCheck();
        }
    }

    void barCodePayCheck() {
        if (checkParam(t.getAuthCode(), 32)) {
            msg = "用户收款id(authCode)参数错误";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }
    }

    void createPayCheck() {
        if (checkParam(t.getMemberId(), 32)) {
            msg = "付款人buyerId(memberId)参数错误";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }
    }

    public enum TradeType {
        APP,BARCODE,FACE,CREATE,PRECREATE;

        public static boolean has(String name) {
            try {
                PayOrderDtoAlipayCheck.TradeType.valueOf(name);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

}
