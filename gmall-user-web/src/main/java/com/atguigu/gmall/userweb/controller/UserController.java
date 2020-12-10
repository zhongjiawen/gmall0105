package com.atguigu.gmall.userweb.controller;//package com.atguigu.gmall.user.controller;

import com.atguigu.gmall.bean .UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    @DubboReference(interfaceClass = UserService.class,version = "${demo.service.version}" )
    UserService userService;
//
//    @RequestMapping("index")
//    @ResponseBody
//    public String index(){
//        return "hello user";
//    }
    @RequestMapping("getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser(){

      List<UmsMember> umsMembers= userService.getAllUser();

        return umsMembers;
    }
    @RequestMapping("getReceiveAddressByMemberId")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId){

        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses= userService.getReceiveAddressByMemberId(memberId);

        return umsMemberReceiveAddresses;
    }
}
