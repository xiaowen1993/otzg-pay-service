//package com.bcb.base;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.redisson.Redisson;
//import org.redisson.config.Config;
//import org.springframework.cache.CacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
///**
// * @ClassName RedisConfig
// * @Auther john
// * @Date 2019-03-02  15:51
// * @Version
// **/
//
//@Configuration
//public class RedisConfig_b1 {
//
//    //缓存管理器
//    @Bean
//    public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
//        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
//        //设置缓存过期时间
//        cacheManager.setDefaultExpiration(10000);
//        return cacheManager;
//    }
//
//
//    /**
//     * redisTemplate 序列化使用的jdkSerializeable, 存储二进制字节码, 所以自定义序列化类
//     * @param redisConnectionFactory
//     * @return*/
//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//        JedisConnectionFactory jedisConnectionFactory= (JedisConnectionFactory) redisConnectionFactory;
//        jedisConnectionFactory.setDatabase(0);
//        redisTemplate.setConnectionFactory(jedisConnectionFactory);
//        // 设置value的序列化规则和 key的序列化规则
////        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        return redisTemplate;
//    }
//
//
//    /**
//     * 序列化定制
//     *
//     * @return
//     */
//    @Bean
//    public Jackson2JsonRedisSerializer<Object> jackson2JsonSerializer() {
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
//                Object.class);
//
//        // 初始化objectmapper
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(mapper);
//        return jackson2JsonRedisSerializer;
//    }
//
//    /**
//     * 操作模板
//     *
//     * @param connectionFactory
//     * @param jackson2JsonRedisSerializer
//     * @return
//     */
//    @Bean
//    public RedisTemplate<String, Object> redisJsonTemplate(JedisConnectionFactory connectionFactory,
//                                                           Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer) {
//
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(connectionFactory);
//
//        // 设置key/hashkey序列化
//        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringSerializer);
//        redisTemplate.setHashKeySerializer(stringSerializer);
//
//        // 设置值序列化
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.afterPropertiesSet();
//
//        return redisTemplate;
//    }
//
//
//    /**
//     * redis 加锁工具
//     * @return
//     */
//    @Bean
//    public Redisson redisson(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setPassword("g5ymm5").setDatabase(0);
//        return (Redisson) Redisson.create(config);
//    }
//}
