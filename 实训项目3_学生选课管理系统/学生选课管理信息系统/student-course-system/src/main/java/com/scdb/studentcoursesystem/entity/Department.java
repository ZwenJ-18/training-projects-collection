package com.scdb.studentcoursesystem.entity;

import lombok.Data; // 新增：加Lombok注解，和其他实体类统一

/**
 * 院系实体类
 */
@Data // 新增：自动生成get/set，替代手动写的getDno()等方法
public class Department {
    // 和数据库department表字段一一对应
    private String dno;       // 院系编号（001/002/003）
    private String dname;     // 院系名称（计算机系/电子信息系）
    private String dmanager;  // 院系负责人（T2024001/T2024002）

    // 移除：手动写的get/set（@Data已自动生成，避免重复）
}