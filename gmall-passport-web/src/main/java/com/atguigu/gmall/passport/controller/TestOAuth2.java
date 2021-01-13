package com.atguigu.gmall.passport.controller;

import com.alibaba.fastjson.JSON;

import util.HttpclientUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @autor huihui
 * @date 2020/11/9 - 22:02
 */

////3228765466
////b7b28960038ce2929f347eb7b5848db8
public class TestOAuth2 {

    public static String getCode() {//111111
        //获得授权码
        //  963302049  http://passport.gmall.com:8085/vlogin
        String s1 = HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id=3228765466&response_type=code&redirect_uri=http://passport.gmall.com:8085/vlogin");
        System.out.println(s1);
        //用户点击授权（用户操作）

        //返回授权码到回调地址
        //code=9650bf87dd80652396f012bbe91f63e3
     //  String s2 = "http://passport.gmall.com:8085/vlogin?code=9650bf87dd80652396f012bbe91f63e3";
        return null;

    }
    public static String getAccess_token(){//222222
        //        String s2 = "http://passport.gmall.com:8085/vlogin?code=d67e1ffd26eb53d596491ca47986d4e7";
        return null;

    }
    public static String getUser_token(){//333333
        //通过授权码结合个人登录信息（安全）交换access_token(必须POST请求，保证安全)
        // 963302049   f23646803fc8f215773fbc8e348af542   http://passport.gmall.com:8085/vlogin
        String s3 = "https://api.weibo.com/oauth2/access_token";//client_id=3228765466&client_secret=f23646803fc8f215773fbc8e348af542&grant_type=authorization_code&redirect_uri=http://passport.gmall.com:8085/vlogin&code=d67e1ffd26eb53d596491ca47986d4e7";
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "3228765466");
        map.put("client_secret", "b7b28960038ce2929f347eb7b5848db8");
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", "http://passport.gmall.com:8085/vlogin");
        map.put("code", "cdbd23b3f192c68c4fda954fa5e957a0");//授权码在有效期内使用，每新生成一次授权码，说明
        //用户对第三方重新授权，之前的access_token不可用了，重新申请授权码

        String access_token_json = HttpclientUtil.doPost(s3, map);
        Map<String,String> access_map = JSON.parseObject(access_token_json, Map.class);
        System.out.println(access_map.get("access_token"));//  2.005Y7aPG35ZVWDcb037b0dd5d3qMhD
        System.out.println(access_map.get("uid"));//     5727123916
        return access_map.get("access_token");//

    }
    public static Map<String, String> getUser_info(){//2.005Y7aPG35ZVWDcb037b0dd5d3qMhD 5727123916
        //用access_token查询用户信息
        String s4 = "https://api.weibo.com/2/users/show.json?access_token=2.005Y7aPG35ZVWDcb037b0dd5d3qMhD&uid=1";
        String user_json = HttpclientUtil.doGet(s4);
        Map<String, String> user_map = JSON.parseObject(user_json, Map.class);
        System.out.println(user_map.get("1"));
        return user_map;

    }
    public static void main(String[] args) {//静态方法调用的方法必须是静态的
        // getUser_token();
      getUser_info();
    }
}
