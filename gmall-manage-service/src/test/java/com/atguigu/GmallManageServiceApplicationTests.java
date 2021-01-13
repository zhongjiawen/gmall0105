package com.atguigu;



import com.atguigu.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GmallManageServiceApplicationTests {

    @Autowired
    RedisUtil redisUtil;


    @Test
    public void contextLoads() {
        Jedis jedis = redisUtil.getJedis();
        System.out.println(jedis);

    }





}
