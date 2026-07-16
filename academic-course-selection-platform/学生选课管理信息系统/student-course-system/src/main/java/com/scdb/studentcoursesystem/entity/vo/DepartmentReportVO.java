package com.scdb.studentcoursesystem.entity.vo;

import lombok.Data;

/**
 * 院系信息报表VO
 * 统计院系下学生、教师数量
 */
@Data
public class DepartmentReportVO {
    private String dno;        // 院系编号
    private String dname;      // 院系名称
    private String dmanager;   // 院系负责人
    private Integer studentNum;// 院系学生数量
    private Integer teacherNum;// 院系教师数量
    private Integer courseNum; // 院系开课数量


}
