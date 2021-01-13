package com.atguigu.gmall.conf;

import com.atguigu.gmall.util.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RedisConfig {
   // @Value("${spring.redis.host}")
    private String host = "192.168.147.133" ;
    //@Value("${spring.redis.port}")
    private int port = 6379;
    //@Value("${spring.redis.database}")
    private int database = 0 ;

    public RedisConfig() {
    }

    @Bean
    public RedisUtil getRedisUtil() {
//        if (this.host.equals("disabled")) {
//            return null;
//        } else {
            RedisUtil redisUtil = new RedisUtil();
            redisUtil.initPool(this.host, this.port, this.database);
            return redisUtil;
//        }
    }
}
