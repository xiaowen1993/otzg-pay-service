package com.bcb.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bcb.log.util.LogUtil;
import com.bcb.util.AesUtil;
import com.bcb.util.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 同步消息队列
 */
public abstract class SyncQue {

    @Autowired
    RedisTemplate<String, Object> redisTemplate; //操作0数据库

    /**
     * 子类必须实现发送消息
     *
     * @param ids
     */
    public abstract void send(String ids);

    /**
     * 子类必须实现接受消息类
     *
     * @param message
     */
    protected abstract void receiver(String message);





    //存放消息的频道信息
    final static ConcurrentHashMap<String, SyncQue> channelMap = new ConcurrentHashMap<>();
    protected static void addChannelMap(String channel, SyncQue syncQue) {
        channelMap.put(channel, syncQue);
    }

    public static Set<String> getChannelMap() {
        return channelMap.keySet();
    }




    //=====================发布订阅模式===============================

    /**
     * 发送实现方法
     *
     * @param message
     */
    protected final void sendPublish(String channel, String message) {
        JSONObject jo = new JSONObject();
        jo.put("channel",channel);
        jo.put("message",message);
        LogUtil.print("channel="+channel);
        redisTemplate.convertAndSend(channel, MessageEcoder.encode(jo.toString()));
    }

    //推送消息
    public void sendProduce(String channel,String message){
        JSONObject jo = new JSONObject();
        jo.put("channel",channel);
        jo.put("message",message);
        LogUtil.print("channel="+channel);
        redisTemplate.opsForList().leftPush(channel, MessageEcoder.encode(jo.toString()));
    }

    /**
     * 默认的回调接收消息方法
     *
     * @param message
     */
    public void handleMessage(String message) {
        JSONObject jo = JSON.parseObject(MessageEcoder.decode(message));
        if (!CheckUtil.isEmpty(jo)) {
            channelMap.get(jo.get("channel")).receiver(jo.getString("message"));
        }
    }


    //消息编解码类
    static class MessageEcoder{
        final static String MESSAGEHEADER = "BCB-MESSAGE-HEADER";
        public static String encode(String message){
            return MESSAGEHEADER+ AesUtil.getEnCode(message);
        }

        public static String decode(String message){
            return AesUtil.getDeCode(message.substring(message.indexOf(MESSAGEHEADER)+MESSAGEHEADER.length()));
        }
    }
}
