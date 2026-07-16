package com.scdb.studentcoursesystem.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine 本地缓存配置
 * 用于缓存热门课程信息，减少数据库查询
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 配置 Caffeine 缓存管理器
     * 默认 TTL 5 分钟，最大缓存 1000 条
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .recordStats());
        return cacheManager;
    }
}
