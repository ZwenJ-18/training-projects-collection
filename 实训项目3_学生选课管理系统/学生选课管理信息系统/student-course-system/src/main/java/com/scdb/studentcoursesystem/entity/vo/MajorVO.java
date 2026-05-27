package com.scdb.studentcoursesystem.entity.vo;

import lombok.Data;

/**
 * 专业信息VO
 */
@Data
public class MajorVO {
    private String majorId;    // 专业编号
    private String majorName;  // 专业名称
    private String dno;        // 所属院系编号
    private String dname;      // 所属院系名称
}