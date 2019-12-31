//package com.bcb;
//
//import com.bcb.log.util.LogUtil;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class HttpAspect {
//    @AfterReturning(value="execution(* com.bcb.pay.service.PayOrderServ.findMember*(..))",returning = "object")
//    public void doAfterReturning(Object object) {
//        LogUtil.print(String.format("response=%s",object.toString()));
//    }
//}