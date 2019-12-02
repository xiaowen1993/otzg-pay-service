package com.bcb.base;

import com.bcb.log.util.LogUtil;

public abstract class BaseBean {

    //保存日志
    protected final static void P(Object o){
        P(o,"info");
    }

    /**
     * 保存业务层日志
     * @author G/2018/2/28 14:37
     * @param level DEBUG,INFO,WARN,ERROR,FATAL
     */
    protected final static void P(Object o,String level){
        LogUtil.addSysErrorLog(o,level);
    }

    protected final static void PT(String str){
        LogUtil.saveTradeLog(str);
    }

}
