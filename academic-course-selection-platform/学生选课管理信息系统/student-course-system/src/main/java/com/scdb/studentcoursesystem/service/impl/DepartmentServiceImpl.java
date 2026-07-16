// 路径：com/scdb/studentcoursesystem/service/impl/DepartmentServiceImpl.java
package com.scdb.studentcoursesystem.service.impl;

import com.scdb.studentcoursesystem.entity.Department;
import com.scdb.studentcoursesystem.mapper.DepartmentMapper;
import com.scdb.studentcoursesystem.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    // 原有方法
    @Override
    public boolean addDepartment(Department department) {
        return departmentMapper.insert(department) > 0;
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentMapper.selectAll();
    }

    // 新增：根据编号查询单条院系
    @Override
    public Department getDepartmentByDno(String dno) {
        return departmentMapper.selectByDno(dno);
    }

    // 新增：根据名称查询院系
    @Override
    public Department getDepartmentByDname(String dname) {
        return departmentMapper.selectByDname(dname);
    }

    // 新增：根据编号删除院系
    @Override
    public boolean deleteDepartmentByDno(String dno) {
        return departmentMapper.deleteByDno(dno) > 0;
    }
    // 新增：实现修改院系的方法
    @Override
    public boolean updateDepartment(Department dept) {
        // 先校验院系是否存在
        Department existing = getDepartmentByDno(dept.getDno());
        if (existing == null) {
            return false;
        }
        // 执行更新操作
        return departmentMapper.update(dept) > 0;
    }
}