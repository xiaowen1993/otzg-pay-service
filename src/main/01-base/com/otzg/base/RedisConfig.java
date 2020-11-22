package com.otzg.base;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching  // 需要这个注解才能启用注解驱动的缓存管理功能
public class RedisConfig {
    private String clusterNodes;
    private String redisHost="127.0.0.1";
    private int redisPort=6379;
    private String redisPasswd="";
    private int timeOut = 2000;
    private int redirects = 8;


    @Primary
    @Bean // 实际使用的redisTemplate，可以直接注入到代码中，直接操作redis
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * redis 加锁工具
     * @return
     */
    @Bean
    public Redisson redisson(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://"+redisHost+":"+redisPort).setPassword(redisPasswd).setDatabase(0);
        return (Redisson) Redisson.create(config);
    }
}