package com.bcb.util;

import java.util.Map;

/**
 * @Author G.
 * @Date 2019/11/30 0030 下午 1:42
 */
public abstract class CheckDtoUtil<T> {
    protected T t;
    protected boolean pass = false;
    protected String code = RespTips.ERROR_CODE.code;
    protected String msg = "";
    //构造函数
    public CheckDtoUtil(T t) {
        this.t = t;
        check();
    }

    //校验不成功返回null(创建支付单失败)
    public T get() {
        if (!pass) {
            return null;
        }
        return this.t;
    }
    //校验结果
    public boolean isPass() {
        return this.pass;
    }

    //返回参数校验结果
    public Map getMsg() {
        return FastJsonUtil.get(pass, code, msg);
    }

    /**
     * 校验收款业务单参数
     */
    protected abstract void check();

    /**
     * 校验参数
     *
     * @param param
     * @param length
     * @return
     */
    protected boolean checkParam(String param, int length) {
        return CheckUtil.checkParam(param, length);
    }

    /**
     * 校验金额
     *
     * @param amount
     * @return
     */
    protected boolean checkAmount(Double amount) {
        return CheckUtil.checkAmount(amount);
    }

}