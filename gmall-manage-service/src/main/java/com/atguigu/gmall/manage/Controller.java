package com.atguigu.gmall.manage;


import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

//@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedissonClient redissonClient;

    @RequestMapping("testRedisson")
    @ResponseBody
    public String testRedisson() {
        Jedis jedis = redisUtil.getJedis();
//        RLock lock = redissonClient.getLock("lock");//声明锁
//        lock.lock();//上锁

//        try {

            String v = jedis.get("k");
            if (StringUtils.isBlank(v)) {
                v = "1";
            }
            System.out.println("-->" + v);
            jedis.set("k", (Integer.parseInt(v) + 1) + "");
////        } finally {
//            jedis.close();
//            lock.unlock();//解锁
//        }

        return "success";
    }




}
