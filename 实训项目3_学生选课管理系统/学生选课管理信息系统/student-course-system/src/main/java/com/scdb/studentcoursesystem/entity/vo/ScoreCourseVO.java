package com.scdb.studentcoursesystem.entity.vo;

import lombok.Data;

/**
 * 成绩+课程关联VO
 * 封装学生成绩与对应课程的关联数据
 */
@Data
public class ScoreCourseVO {
    // 学生信息
    private String sno;      // 学号
    private String sname;    // 学生姓名
    // 课程信息
    private String cno;      // 课程号
    private String cname;    // 课程名称
    private Integer ccredit; // 课程学分
    private String cdept;    // 开课院系
    private String teacher;  // 授课教师
    // 成绩信息
    private Integer score;   // 成绩分数
    private String semester; // 学期
    private String grade;    // 成绩等级（优/良/中/及格/不及格）
}