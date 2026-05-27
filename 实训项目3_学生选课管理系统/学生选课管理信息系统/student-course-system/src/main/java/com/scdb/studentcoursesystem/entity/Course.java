package com.scdb.studentcoursesystem.entity;

public class Course {
    // 原有字段
    private String cno;        // 课程号
    private String cname;      // 课程名
    private String cpno;       // 先修课号
    private Integer ccredit;   // 学分
    // 新增字段
    private Integer ctime;     // 学时
    private String cdept;      // 开课院系
    private String teacher;    // 授课教师

    // 无参构造
    public Course() {}

    // 全参构造（包含新增字段）
    public Course(String cno, String cname, String cpno, Integer ccredit, Integer ctime, String cdept, String teacher) {
        this.cno = cno;
        this.cname = cname;
        this.cpno = cpno;
        this.ccredit = ccredit;
        this.ctime = ctime;
        this.cdept = cdept;
        this.teacher = teacher;
    }

    // 原有字段的getter/setter
    public String getCno() { return cno; }
    public void setCno(String cno) { this.cno = cno; }
    public String getCname() { return cname; }
    public void setCname(String cname) { this.cname = cname; }
    public String getCpno() { return cpno; }
    public void setCpno(String cpno) { this.cpno = cpno; }
    public Integer getCcredit() { return ccredit; }
    public void setCcredit(Integer ccredit) { this.ccredit = ccredit; }

    // 新增字段的getter/setter
    public Integer getCtime() { return ctime; }
    public void setCtime(Integer ctime) { this.ctime = ctime; }
    public String getCdept() { return cdept; }
    public void setCdept(String cdept) { this.cdept = cdept; }
    public String getTeacher() { return teacher; }
    public void setTeacher(String teacher) { this.teacher = teacher; }
}