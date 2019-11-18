//package com.bcb;
//
//import com.bcb.log.util.LogUtil;
//import com.bcb.member.entity.Member;
//import org.apache.catalina.servlet4preview.http.HttpServletRequest;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.text.MessageFormat;
//import java.util.Arrays;
//
//@Aspect
//@Component
//public class HttpAspect {
//    @Before("execution(* com.bcb.member.service.MemberServ.findMember(..))")
//    public void doBefore(JoinPoint joinPoint) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        javax.servlet.http.HttpServletRequest request = attributes.getRequest();
//        LogUtil.print("执行事务之前。。。");
//        LogUtil.print(String.format("url=%s",request.getRequestURI().toString()));
//        LogUtil.print(String.format("method=%s",request.getMethod()));
//        LogUtil.print(String.format("ip=%s",request.getRemoteAddr()));
//        LogUtil.print(String.format("class-method=%s",joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()));
//        Arrays.stream(joinPoint.getArgs()).forEach(arg->LogUtil.print(String.format("args=%s" ,arg)));
//    }
//
//    @After("execution(* com.bcb.member.service.MemberServ.findMember(..))")
//    public void doAfter(JoinPoint joinPoint) {
//        LogUtil.print("执行事务之后。。。"+joinPoint.getTarget());
//    }
//
//    @AfterReturning(value="execution(* com.bcb.member.service.MemberServ.findMember*(..))",returning = "object")
//    public void doAfterReturning(Object object) {
//        LogUtil.print(String.format("response=%s",object.toString()));
//    }
//
//    @AfterThrowing(value="execution(* com.bcb.member.service.MemberServ.findMember(..))", throwing = "e")
//    public void afterThrowing(JoinPoint joinPoint, Exception e) {
//        String methodName = joinPoint.getSignature().getName();
//        LogUtil.print(String.format("The method=%s,occurs excetion=%s" , methodName ,e));
//    }
//
//}