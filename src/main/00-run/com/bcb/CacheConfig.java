//package com.bcb;
//
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Memcache使用缓存先要在manager里面注册
// */
//@Configuration
//public class CacheConfig {
//
//    @Bean
//    public CacheManager cacheManager() {
//        return new ConcurrentMapCacheManager("orderStatisticsAllMap","orderStatisticsByToday");
//    }
//}