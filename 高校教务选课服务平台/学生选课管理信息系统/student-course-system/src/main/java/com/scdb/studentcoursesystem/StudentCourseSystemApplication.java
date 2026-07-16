package com.scdb.studentcoursesystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 学生选课系统启动类（核心入口）
 * 加@MapperScan扫描mapper包，解决Mapper接口注入失败问题
 */
@SpringBootApplication
@MapperScan("com.scdb.studentcoursesystem.mapper") // 扫描所有Mapper接口
public class StudentCourseSystemApplication {
    public static void main(String[] args) {
        // 启动SpringBoot项目
        SpringApplication.run(StudentCourseSystemApplication.class, args);
    }
}