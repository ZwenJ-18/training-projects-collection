package com.scdb.studentcoursesystem.entity;

// 去掉@Data，手动写getter/setter
public class Sct {
    private String sno;      // 学号
    private String cno;      // 课程号
    private String semester; // 选课学期
    private Integer score;   // 成绩

    // 手动添加getter方法（关键）
    public String getSno() {
        return sno;
    }

    public String getCno() {
        return cno;
    }

    // 可选：添加setter方法（前端传参需要）
    public void setSno(String sno) {
        this.sno = sno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}