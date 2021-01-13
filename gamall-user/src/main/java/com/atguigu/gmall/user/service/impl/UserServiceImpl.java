//package com.atguigu.gmall.user.service.impl;
//
//
//import com.atguigu.gmall.bean.UmsMember;
//import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
//import com.atguigu.gmall.service.UserService;
//import com.atguigu.gmall.user.mapper.UmsMemberReceiveAddressMapper;
//import com.atguigu.gmall.user.mapper.UserMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//
//@Service
//public class UserServiceImpl implements UserService {
//    @Autowired
//    UserMapper userMapper;
//
//    @Autowired
//    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;
//
//    @Override
//    public List<UmsMember> getAllUser() {
//        List<UmsMember> umsMemberList = userMapper.selectAll();
//        return umsMemberList;
//    }
//
//    @Override
//    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
////        Example e = new Example(UmsMemberReceiveAddress.class);
////        e.createCriteria().andEqualTo("memberId",memberId);
////        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses= umsMemberReceiveAddressMapper.selectByExample(e);
//        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
//        umsMemberReceiveAddress.setMemberId(memberId);
//        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);
//
//        return umsMemberReceiveAddresses;
//    }
//
//    @Override
//    public UmsMember checkOauthUser(UmsMember umsCheck) {
//        return null;
//    }
//
//    @Override
//    public UmsMember addOauthUser(UmsMember umsMember) {
//        return null;
//    }
//
//    @Override
//    public void addUserToken(String token, String memberId) {
//
//    }
//
//    @Override
//    public UmsMember login(UmsMember umsMember) {
//        return null;
//    }
//}
