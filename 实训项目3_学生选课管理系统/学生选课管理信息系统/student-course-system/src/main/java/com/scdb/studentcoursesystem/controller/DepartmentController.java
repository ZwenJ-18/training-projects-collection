package com.scdb.studentcoursesystem.controller;

import com.scdb.studentcoursesystem.entity.Department;
import com.scdb.studentcoursesystem.entity.Result;
import com.scdb.studentcoursesystem.entity.Student;
import com.scdb.studentcoursesystem.service.DepartmentService;
import com.scdb.studentcoursesystem.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private StudentService studentService;

    @PostMapping("/add")
    public Result<?> add(@RequestBody Department department) {
        try {
            boolean flag = departmentService.addDepartment(department);
            if (flag) {
                return Result.success("添加院系成功"); // 补充消息
            } else {
                return Result.error("添加院系失败");
            }
        } catch (Exception e) {
            logger.error("添加院系异常", e);
            return Result.error("添加院系异常：" + e.getMessage());
        }
    }

    @GetMapping("/list")
    public Result<List<Department>> list() {
        try {
            List<Department> deptList = departmentService.getAllDepartments();
            return Result.success("查询院系成功", deptList); // 补充消息
        } catch (Exception e) {
            logger.error("查询院系异常", e);
            return Result.error("查询院系异常：" + e.getMessage());
        }
    }

    @GetMapping("/query/{dno}")
    public Result<Department> query(@PathVariable String dno) {
        try {
            Department dept = departmentService.getDepartmentByDno(dno);
            if (dept != null) {
                return Result.success("查询院系成功", dept); // 补充消息
            } else {
                return Result.error("未查询到编号为【" + dno + "】的院系");
            }
        } catch (Exception e) {
            logger.error("查询院系异常", e);
            return Result.error("查询院系异常：" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{dno}")
    public Result<?> delete(@PathVariable String dno) {
        try {
            int studentCount = studentService.countStudentByDept(dno);
            if (studentCount > 0) {
                return Result.error("该院系下有学生，无法删除！");
            }
            Department dept = departmentService.getDepartmentByDno(dno);
            if (dept == null) {
                return Result.error("院系不存在！");
            }
            boolean flag = departmentService.deleteDepartmentByDno(dno);
            return flag ? Result.success("删除院系成功") : Result.error("删除失败！"); // 补充消息
        } catch (Exception e) {
            logger.error("删除院系异常", e);
            return Result.error("该院系下有学生，无法删除！");
        }
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Department dept) {
        try {
            boolean flag = departmentService.updateDepartment(dept);
            if (flag) {
                return Result.success("修改院系成功"); // 补充消息
            } else {
                return Result.error("修改失败：院系不存在");
            }
        } catch (Exception e) {
            logger.error("修改院系异常", e);
            return Result.error("修改院系异常：" + e.getMessage());
        }
    }

    @GetMapping("/check/{dno}")
    public Result<Boolean> checkDno(@PathVariable String dno) {
        try {
            Department dept = departmentService.getDepartmentByDno(dno);
            return Result.success("校验院系编号成功", dept != null); // 补充消息
        } catch (Exception e) {
            logger.error("校验院系编号异常", e);
            return Result.error("校验院系编号异常：" + e.getMessage());
        }
    }

    @GetMapping("/checkHasStudent")
    public Result<?> checkHasStudent() {
        try {
            List<Department> depts = departmentService.getAllDepartments();
            List<Student> students = studentService.findAllStudents();

            List<Map<String, Object>> result = depts.stream().map(dept -> {
                Map<String, Object> map = new HashMap<>();
                boolean hasStudent = students.stream().anyMatch(s -> dept.getDno().equals(s.getSdept()));
                map.put("dno", dept.getDno());
                map.put("dname", dept.getDname());
                map.put("hasStudent", hasStudent);
                return map;
            }).toList();

            return Result.success("查询院系学生状态成功", result); // 补充消息
        } catch (Exception e) {
            logger.error("查询院系学生状态异常", e);
            return Result.error("查询失败！");
        }
    }
}