package com.scdb.studentcoursesystem.entity.vo;

import lombok.Data;

/**
 * 选课成绩报表VO
 * 整合学生、课程、选课成绩信息
 */
@Data
public class SctReportVO {
    private String sno;        // 学号
    private String sname;      // 学生姓名
    private String cno;        // 课程号
    private String cname;      // 课程名称
    private String semester;   // 学期
    private String score;      // 成绩（未录入显示“未录入”）
    private String teacher;    // 授课教师
    private Integer ccredit;   // 课程学分
}