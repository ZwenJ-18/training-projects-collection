package com.scdb.studentcoursesystem.entity.vo;

import lombok.Data;

/**
 * 课程信息报表VO
 * 关联授课教师、院系信息
 */
@Data
public class CourseReportVO {
    private String cno;      // 课程号
    private String cname;    // 课程名称
    private String cpno;     // 先修课号
    private String cpname;   // 先修课名称
    private Integer ccredit; // 学分
    private Integer ctime;   // 学时
    private String cdept;    // 开课院系
    private String teacher;  // 授课教师姓名
}