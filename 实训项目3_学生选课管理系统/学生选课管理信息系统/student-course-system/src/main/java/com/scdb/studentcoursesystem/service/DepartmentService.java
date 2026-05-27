package com.scdb.studentcoursesystem.service;

import com.scdb.studentcoursesystem.entity.Department;
import java.util.List;

public interface DepartmentService {
    boolean addDepartment(Department department);
    List<Department> getAllDepartments();
    Department getDepartmentByDno(String dno);
    boolean deleteDepartmentByDno(String dno);
    // 新增：修改院系方法定义
    boolean updateDepartment(Department dept);
    // 新增：根据院系名称查询院系
    Department getDepartmentByDname(String dname);
}