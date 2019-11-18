package com.bcb.pay.util;

import com.bcb.util.FuncUtil;

/**
 * 支付渠道{alipay:支付宝,wxpay:微信支付,ytpay:平台支付}
 * @author G./2018/5/28 10:45
 */
public enum PayChannelType {
    alipay,wxpay,ykpay,coinpay;

    //判断是否在枚举类型内的值
    public final static boolean hasNotFound(String value){
        try{
            PayChannelType.valueOf(value);
            return false;
        }catch (Exception e){
            return true;
        }
    }

    public final static String getName(String value){
        try{
            return PayChannelType.valueOf(value).name();
        }catch (Exception e){
            return null;
        }
    }

    public final static String getName() {
        String names="";
        for(PayChannelType ct:PayChannelType.values()){
            names += ","+ct.name();
        }
        return FuncUtil.CleanComma(names);
    }


//    public static void main(String args[]) {
//        LogUtil.print(PayChannelType.wxpay.name());
//    }

}
