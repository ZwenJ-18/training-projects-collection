package com.scdb.studentcoursesystem.entity;

import lombok.Data;

// 对应sct表（学生成绩表，兼容原sc表逻辑）
@Data
public class Score {
    // 学号
    private String sno;
    // 课程号
    private String cno;
    // 成绩（保留原有grade字段，兼容历史代码）
    private Integer grade;
    // 学期（新增字段，适配sct表）
    private String semester;

    // 兼容getScore()方法（适配Controller中score.getScore()调用）
    public Integer getScore() {
        return this.grade;
    }

    // 兼容setScore()方法
    public void setScore(Integer score) {
        this.grade = score;
    }
}