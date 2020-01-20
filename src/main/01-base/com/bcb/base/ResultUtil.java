package com.bcb.base;

import com.alibaba.fastjson.JSONObject;
import com.bcb.util.CheckUtil;

/**
 * 返回前端结果数据
 * v1.10
 * @Author G.
 * @Date 2020/1/4 0004 下午 5:17
 */
public class ResultUtil {


    public final static JSONObject getJson(boolean success, String code, String msg) {
        return getJson(success, code, msg, null);
    }

    public final static JSONObject getJson(boolean success, String code, String msg, Object result) {
        JSONObject jo = new JSONObject();
        jo.put("success",success);
        jo.put("code",code);
        jo.put("msg",msg);
        if (!CheckUtil.isEmpty(result)){
            jo.put("result",result);
        }
        return jo;
    }

    public final static JSONObject getPageJson(Long totalPage, Long totalElement, Object content) {
        JSONObject jo = new JSONObject();
        jo.put("totalPage",totalPage);
        jo.put("totalElement",totalElement);
        jo.put("content",content);
        return jo;
    }

}
