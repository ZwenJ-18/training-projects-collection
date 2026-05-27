package com.scdb.studentcoursesystem.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 配置MyBatis驼峰命名映射
@Configuration
public class MyBatisConfig {
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setMapUnderscoreToCamelCase(true);
    }
}