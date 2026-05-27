package com.scdb.studentcoursesystem.entity.vo;

import lombok.Data;

/**
 * 教师信息报表VO
 * 关联主讲课程名称，适配报表展示
 */
@Data
public class TeacherReportVO {
    private String tno;       // 教工号
    private String tname;     // 姓名
    private String tsex;      // 性别
    private Integer tage;     // 年龄
    private String teb;       // 学历
    private String tpt;       // 职称
    private String cno1;      // 主讲课程1编号
    private String cname1;    // 主讲课程1名称
    private String cno2;      // 主讲课程2编号
    private String cname2;    // 主讲课程2名称
    private String cno3;      // 主讲课程3编号
    private String cname3;    // 主讲课程3名称
}