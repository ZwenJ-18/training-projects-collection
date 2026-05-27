package com.scdb.studentcoursesystem.entity.vo;

import lombok.Data;

/**
 * 成绩信息VO
 */
@Data
public class ScoreVO {
    private String sno;      // 学号
    private String cno;      // 课程号
    private Integer score;   // 分数
    private String semester; // 学期
    private String grade;    // 成绩等级
}