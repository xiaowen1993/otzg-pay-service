//package com.bcb.log.controller;
//
//
//import com.bcb.base.BaseBean;
//import com.bcb.member.entity.Member;
//import com.bcb.member.service.MemberServ;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class JunitTest extends BaseBean {
//
//    @Autowired
//    private MemberServ memberServ;
//
//    @Test
//    public void test1(){
//        memberServ.findMemberById(1l);
//    }
//    @Test
//    public void test2(){
//        Member m=memberServ.findMember("13703957387","123456");
//        P("member.json="+m.getNameJson());
//    }
//}
