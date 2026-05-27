package com.scdb.studentcoursesystem.entity;
import lombok.Data;

@Data
public class Major {
    private Integer id;         // 主键
    private String majorName;   // 专业名称
    private String deptName;    // 所属院系名称
}