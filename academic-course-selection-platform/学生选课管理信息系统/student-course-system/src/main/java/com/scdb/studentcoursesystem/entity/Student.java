package com.scdb.studentcoursesystem.entity;
import lombok.Data;

/**
 * 学生实体类（与数据库student表对应）
 */
@Data
public class Student {
    private String sno;      // 学号（主键）
    private String sname;    // 姓名
    private String ssex;     // 性别
    private Integer sage;    // 年龄
    private String sdept;    // 院系名称
    private String major;    // 专业名称
    private String className; // 班级名称（补充）
    private String dno;      // 院系编号（补充）
}