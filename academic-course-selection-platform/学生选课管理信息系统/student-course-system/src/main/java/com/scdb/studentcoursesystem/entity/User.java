package com.scdb.studentcoursesystem.entity;

import lombok.Data;

@Data
public class User {
    // 补充：匹配sys_user表的id字段（自增主键）
    private Integer id;
    private String username;
    private String password;
    private String role;
    private String realName;
    private String phone;
}