package com.scdb.studentcoursesystem.entity;
import lombok.Data;

/**
 * 教师实体类（与数据库teacher表字段一一对应）
 */
@Data
public class Teacher {
    private String tno;      // 教工号（T+7位数字，如T2024004）
    private String tname;    // 姓名
    private String tsex;     // 性别
    private Integer tage;    // 年龄
    private String teb;      // 学历
    private String tpt;      // 职称
    private String cno1;     // 主讲课程一（课程编号，如C002）
    private String cno2;     // 主讲课程二
    private String cno3;     // 主讲课程三
    private String password; // 注册密码（仅注册时使用）

    // 关联课程名称（从course表查询，非数据库字段）
    private String cname1;   // 课程一名称
    private String cname2;   // 课程二名称
    private String cname3;   // 课程三名称
}