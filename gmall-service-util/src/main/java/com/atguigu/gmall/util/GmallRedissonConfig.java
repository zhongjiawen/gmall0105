package com.atguigu.gmall.util;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GmallRedissonConfig {

    @Value("192.168.147.133")
    private String host;

    @Value("6379")
    private String port;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://"+host+":"+port);
        //单例模式  传入密码
        config.useSingleServer().setPassword("123456");
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}