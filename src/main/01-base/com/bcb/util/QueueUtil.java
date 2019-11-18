package com.bcb.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueUtil {
    private static BlockingQueue blockingQueue = new LinkedBlockingQueue(20000000);

    public static boolean add(String message) {
        return blockingQueue.offer(message);
    }

    public static String take(){
        try {
            return (String) blockingQueue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }


    public static void main(String args[]){
        try {
            for(int i=0;i<10;i++){
                if(!add("yes")){
                    System.out.println("take="+take());
                }
                Thread.sleep(1000l);
                System.out.println("take="+take());
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
