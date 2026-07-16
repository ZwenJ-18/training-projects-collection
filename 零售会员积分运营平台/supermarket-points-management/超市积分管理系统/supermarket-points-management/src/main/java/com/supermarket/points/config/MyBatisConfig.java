package com.supermarket.points.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.supermarket.points.mapper") // 扫描Mapper接口
public class MyBatisConfig {
}