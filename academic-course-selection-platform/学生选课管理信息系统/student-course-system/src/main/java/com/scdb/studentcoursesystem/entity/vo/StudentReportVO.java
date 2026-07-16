package com.scdb.studentcoursesystem.entity.vo;

import lombok.Data;

/**
 * 学生信息报表VO
 * 关联院系信息，适配报表展示
 */
@Data
public class StudentReportVO {
    private String sno;       // 学号
    private String sname;     // 姓名
    private String ssex;      // 性别
    private Integer sage;     // 年龄
    private String sdept;     // 院系名称
    private String major;     // 专业
    private String className; // 班级
    private String dname;     // 院系全称（关联Department）
    private String dno;       // 院系编号
}