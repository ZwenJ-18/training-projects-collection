package com.scdb.studentcoursesystem.controller;

import com.scdb.studentcoursesystem.entity.Course;
import com.scdb.studentcoursesystem.entity.Result;
import com.scdb.studentcoursesystem.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    // 新增课程（修复Result.success调用）
    @PostMapping("/add")
    public Result<?> add(@RequestBody Course course) {
        try {
            boolean flag = courseService.addCourse(course);
            if (flag) {
                return Result.success("添加课程成功"); // 替换为带消息的success
            } else {
                return Result.error("添加课程失败");
            }
        } catch (Exception e) {
            logger.error("添加课程异常", e);
            return Result.error("添加课程异常：" + e.getMessage());
        }
    }

    // 查询所有课程
    @GetMapping("/list")
    public Result<List<Course>> list() {
        try {
            List<Course> courseList = courseService.getAllCourses();
            return Result.success("查询课程成功", courseList); // 补充消息参数
        } catch (Exception e) {
            logger.error("查询课程异常", e);
            return Result.error("查询课程异常：" + e.getMessage());
        }
    }

    // 【核心修复】路径映射改为 /query （前端调用路径匹配），保留PathVariable兼容
    @GetMapping({"/query/{cno}", "/query"})
    public Result<Course> query(
            @PathVariable(required = false) String cno,
            @RequestParam(required = false) String cnoParam) {
        try {
            // 兼容两种传参方式：路径参数 / 普通参数
            String targetCno = cno != null ? cno : cnoParam;
            if (targetCno == null || targetCno.trim().isEmpty()) {
                return Result.error("课程号不能为空");
            }
            Course course = courseService.getCourseByCno(targetCno.trim());
            if (course != null) {
                return Result.success("查询课程成功", course);
            } else {
                return Result.error("未查询到编号为【" + targetCno + "】的课程");
            }
        } catch (Exception e) {
            logger.error("查询课程异常", e);
            return Result.error("查询课程异常：" + e.getMessage());
        }
    }

    // 根据课程号删除课程（修复Result.success调用）
    @DeleteMapping("/delete/{cno}")
    public Result<?> delete(@PathVariable String cno) {
        try {
            boolean flag = courseService.deleteCourseByCno(cno);
            if (flag) {
                return Result.success("删除课程成功"); // 替换为带消息的success
            } else {
                return Result.error("删除课程失败，未找到编号为【" + cno + "】的课程");
            }
        } catch (Exception e) {
            logger.error("删除课程异常", e);
            return Result.error("删除课程异常：" + e.getMessage());
        }
    }

    // 修改课程（修复Result.success调用）
    @PutMapping("/update")
    public Result<?> update(@RequestBody Course course) {
        try {
            boolean flag = courseService.updateCourse(course);
            if (flag) {
                return Result.success("修改课程成功"); // 替换为带消息的success
            } else {
                return Result.error("修改失败：课程不存在");
            }
        } catch (Exception e) {
            logger.error("修改课程异常", e);
            return Result.error("修改课程异常：" + e.getMessage());
        }
    }

    // 校验课程号是否存在
    @GetMapping("/check/{cno}")
    public Result<Boolean> checkCno(@PathVariable String cno) {
        try {
            Course course = courseService.getCourseByCno(cno);
            return Result.success("校验课程编号成功", course != null); // 补充消息参数
        } catch (Exception e) {
            logger.error("校验课程编号异常", e);
            return Result.error("校验课程编号异常：" + e.getMessage());
        }
    }
}