package com.bcb.pay.util;

import com.alibaba.fastjson.JSONObject;

/**
 * 支付结果结构体
 * v1.0.1
 *
 * @Author G.
 * @Date 2020/1/3 0003 下午 2:04
 */
public class PayResult {
    //{-1:失败,0:等待,1:成功}
    int result = 0;
    Object body = null;

    public PayResult(int result) {
        this.result = result;
    }

    public PayResult(int result, Object data) {
        this.result = result;
        this.body = data;
    }

    public int getResult() {
        return result;
    }

    public Object getBody() {
        return body;
    }

    public JSONObject getJson() {
        JSONObject jo = new JSONObject();
        jo.put("result", result);
        jo.put("msg",(result == 0 ? "支付中" : result == 1 ? "成功" : "失败"));
        if (null != body)
            jo.put("body", body);
        return jo;
    }


}
