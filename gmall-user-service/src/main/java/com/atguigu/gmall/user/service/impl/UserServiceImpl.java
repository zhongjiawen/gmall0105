package com.atguigu.gmall.user.service.impl;



import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.atguigu.gmall.user.mapper.UserMapper;
import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;


import java.util.List;

@Component
@DubboService(version = "999",interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;



    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<UmsMember> getAllUser() {
        List<UmsMember> umsMemberList =  userMapper.selectAll();
        return umsMemberList;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
//        Example e = new Example(UmsMemberReceiveAddress.class);
//        e.createCriteria().andEqualTo("memberId",memberId);
//        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses= umsMemberReceiveAddressMapper.selectByExample(e);
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses= umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);

        return umsMemberReceiveAddresses;
    }

    @Override
    public UmsMember checkOauthUser(UmsMember umsCheck) {
        UmsMember umsMember = userMapper.selectOne(umsCheck);
        return umsMember;

    }

    @Override
    public UmsMember addOauthUser(UmsMember umsMember){
        userMapper.insertSelective(umsMember);
        return umsMember;
    }

    @Override
    public UmsMemberReceiveAddress getReceiveAddressById(String receiveAddressId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setId(receiveAddressId);
        UmsMemberReceiveAddress address = umsMemberReceiveAddressMapper.selectOne(umsMemberReceiveAddress);
        return address;
    }



    @Override
    public void addUserToken(String token, String memberId) {
        Jedis jedis = redisUtil.getJedis();
        jedis.setex("user:"+memberId+":token",60*60*2,token);
        jedis.close();

    }

    @Override
    public UmsMember login(UmsMember umsMember) {
        System.out.println(umsMember.getPassword());
        Jedis jedis = null;
        try {

            jedis = redisUtil.getJedis();
            if (jedis != null) {//缓存存在

                String umsMemberStr = jedis.get("user:" + umsMember.getPassword() + umsMember.getUsername() + ":info");
                if (StringUtils.isNotBlank(umsMemberStr)) {
                    //密码正确
                    UmsMember umsMemberFromCache = JSON.parseObject(umsMemberStr, UmsMember.class);
                    return umsMemberFromCache;
                } else {
                    //密码错误
                    //缓存中没有
                    //开启数据库查询
                    UmsMember umsMemberFromDb = loginFromDb(umsMember);
                    if (umsMemberFromDb != null) {
                        //防止错误
                        jedis.setex("user:" + umsMember.getPassword() + umsMember.getUsername() + ":info", 60 * 60 * 24, JSON.toJSONString(umsMemberFromDb));
                    }
                    return umsMemberFromDb;
                }
            } else {//缓存宕机，连接redis失败，访问db
                UmsMember umsMemberFromDb = loginFromDb(umsMember);
                if (umsMemberFromDb != null) {
                    jedis.setex("user:" + umsMember.getPassword() + ":info", 60 * 60 * 24, JSON.toJSONString(umsMemberFromDb));
                }
                return umsMemberFromDb;
            }

        } finally {
            jedis.close();
        }
    }



    private UmsMember loginFromDb(UmsMember umsMember) {

        List<UmsMember> umsMembers = userMapper.select(umsMember);

        if (umsMembers != null)
            return umsMembers.get(0);

        return null;
    }
}
