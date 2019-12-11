package com.bcb.pay.util;

import com.bcb.pay.dto.PayOrderDto;
import com.bcb.util.RespTips;

import java.util.Map;

/**
 * 微信收款单校验工具
 *
 * @Author G.
 * @Date 2019/11/23 0023 下午 3:28
 */
public class PayOrderDtoWxpayCheck extends PayOrderDtoCheck{

    public PayOrderDtoWxpayCheck(PayOrderDto t) {
        super(t);
    }

    @Override
    public Map getMsg() {
        return super.getMsg();
    }

    @Override
    public void check() {
        super.check();

        if (!TradeType.has(t.getPayType())) {
            msg = "微信支付类型(payType)参数错误";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }

        if (t.getPayType().equals(TradeType.JSAPI.name())) {  //微信及小程序
            jsapiPayCheck();
        } else if (t.getPayType().equals(TradeType.APP.name())) {   //app
            appPayCheck();
        } else if (t.getPayType().equals(TradeType.MWEB.name())) {   //h5收款校验
            ipAddressCheck();
            notifyCheck();
        } else if (t.getPayType().equals(TradeType.MICROPAY.name())) {   //刷脸及扫码收款校验
            microPayCheck();
        } else if (t.getPayType().equals(TradeType.NATIVE.name())) {     //主扫
            nativePayCheck();
        }
    }

    void nativePayCheck() {
        if (checkParam(t.getProductId(), 32)) {
            msg = "商品id(productId)参数错误";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }
        notifyCheck();
    }

    void microPayCheck() {
        if (checkParam(t.getAuthCode(), 128)) {
            msg = "商户应用id(appId)参数错误";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }
        ipAddressCheck();
    }

    void appPayCheck() {
        if (checkParam(t.getAppId(), 32)) {
            msg = "商户应用id(appId)参数错误";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }
        notifyCheck();
    }

    /**
     * jsapi参数校验
     */
    void jsapiPayCheck() {
        if (checkParam(t.getMemberId(), 32)) {
            msg = "付款人openid(memberId)参数错误";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }
        ipAddressCheck();
        notifyCheck();
    }

    void notifyCheck() {
        if (checkParam(t.getPayNotify(), 255)) {
            msg = "回调地址(payNotify)参数错误";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }
    }

    void ipAddressCheck() {
        if (checkParam(t.getIpAddress(), 64)) {
            msg = "终端ip地址(ipAddress)参数错误";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }
    }

    public enum TradeType {
        JSAPI,APP,MWEB,MICROPAY,NATIVE;

        public static boolean has(String name) {
            try {
                TradeType.valueOf(name);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
