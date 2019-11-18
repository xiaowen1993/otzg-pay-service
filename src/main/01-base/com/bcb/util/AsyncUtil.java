//package com.bcb.util;
//
//import com.bcb.base.BaseBean;
//import com.bcb.log.util.LogUtil;
//import com.bcb.message.entity.IMessage;
//
//import javax.websocket.ClientEndpoint;
//import javax.websocket.ContainerProvider;
//import javax.websocket.Session;
//import javax.websocket.WebSocketContainer;import java.net.URI;
//
///**
// * 通过系统发送即时消息的方法
// * @author G/2018/3/29 16:01
// */
//@ClientEndpoint
//public class AsyncUtil extends BaseBean {
//
//    public static void broadcast(String content){
//        IMessage m=new IMessage("SYSTEM","BROADCAST", DateUtil.yearMonthDayTime(),content);
//        send(m);
//    }
//
//    public static void send(String to,String content){
//        IMessage m=new IMessage("SYSTEM",to, DateUtil.yearMonthDayTime(),content);
//        send(m);
//    }
//
//    public static synchronized void send(IMessage m){
//        try {
//            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//            String uri = "ws://"+LogUtil.getServUrl()+"/endPointServer/SYSTEM";
//            Session session = container.connectToServer(AsyncUtil.class,new URI(uri));
//            session.getBasicRemote().sendText(m.toString()); // 发送消息
//            session.close(); // 关闭连接
//        } catch (Exception e) {
//            e.printStackTrace();
//            P("websocket exception = "+ e);
//        }
//    }
//}
