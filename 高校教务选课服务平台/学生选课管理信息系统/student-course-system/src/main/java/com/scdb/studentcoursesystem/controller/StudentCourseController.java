package com.scdb.studentcoursesystem.controller;

import com.scdb.studentcoursesystem.entity.Course;
import com.scdb.studentcoursesystem.entity.Result;
import com.scdb.studentcoursesystem.entity.Sct;
import com.scdb.studentcoursesystem.entity.User;
import com.scdb.studentcoursesystem.service.CourseService;
import com.scdb.studentcoursesystem.service.SctService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student/course")
public class StudentCourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private SctService sctService;

    /**
     * 查询所有可选课程
     */
    @GetMapping("/list")
    public Result<List<Course>> getCourseList() {
        try {
            List<Course> courses = courseService.getAllCourses();
            return Result.success("查询课程成功", courses);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("加载课程列表失败");
        }
    }

    /**
     * 学生选课（适配你的SctService.addSct）
     */
    @PostMapping("/select")
    public Result<?> selectCourse(@RequestBody Sct sct, HttpSession session) {
        try {
            // 修正：从Session获取User对象，再提取学号
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null) {
                return Result.error("未登录，请重新登录");
            }
            String loginSno = loginUser.getUsername();
            sct.setSno(loginSno);

            // 校验是否已选该课程
            Sct existing = sctService.getSctBySnoAndCno(loginSno, sct.getCno());
            if (existing != null) {
                return Result.error("已选该课程，无需重复选课");
            }

            boolean success = sctService.addSct(sct);
            return success ? Result.success("选课成功") : Result.error("选课失败，请重试");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("选课异常：" + e.getMessage());
        }
    }

    /**
     * 退选课程（适配你的SctService.deleteSctBySnoAndCno）
     */
    @PostMapping("/drop")
    public Result<?> dropCourse(@RequestParam String cno, HttpSession session) {
        try {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null) {
                return Result.error("未登录，请重新登录");
            }
            String loginSno = loginUser.getUsername();

            // 校验是否已选该课程
            Sct existing = sctService.getSctBySnoAndCno(loginSno, cno);
            if (existing == null) {
                return Result.error("未选该课程，无法退选");
            }

            boolean success = sctService.deleteSctBySnoAndCno(loginSno, cno);
            return success ? Result.success("退选成功") : Result.error("退选失败，请重试");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("退选异常：" + e.getMessage());
        }
    }

    /**
     * 查询已选课程（适配你的SctService.getAllScts）
     */
    @GetMapping("/selected")
    public Result<List<Sct>> getSelectedCourses(HttpSession session) {
        try {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null) {
                return Result.error("未登录，请重新登录");
            }
            String loginSno = loginUser.getUsername();

            List<Sct> allScts = sctService.getAllScts();
            // 过滤当前学生的选课记录
            List<Sct> selected = allScts.stream()
                    .filter(sct -> loginSno.equals(sct.getSno()))
                    .collect(Collectors.toList());
            return Result.success("查询已选课程成功", selected);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("加载已选课程失败：" + e.getMessage());
        }
    }

    /**
     * 查询课程成绩（适配你的SctService.getSctBySnoAndCno）
     */
    @GetMapping("/score")
    public Result<Integer> getScore(@RequestParam String cno, HttpSession session) {
        try {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null) {
                return Result.error("未登录，请重新登录");
            }
            String loginSno = loginUser.getUsername();

            Sct sct = sctService.getSctBySnoAndCno(loginSno, cno);
            if (sct == null) {
                return Result.error("暂无成绩（未选该课程）");
            }
            Integer score = sct.getScore();
            return score == null ? Result.error("暂无成绩") : Result.success("查询成绩成功", score);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询成绩失败：" + e.getMessage());
        }
    }

    /**
     * 新增：统计已选课程的总学分
     */
    @GetMapping("/totalCredit")
    public Result<Integer> getTotalCredit(HttpSession session) {
        try {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null) {
                return Result.error("未登录，请重新登录");
            }
            String loginSno = loginUser.getUsername();

            // 1. 获取当前学生已选课程
            List<Sct> selected = sctService.getAllScts().stream()
                    .filter(sct -> loginSno.equals(sct.getSno()))
                    .collect(Collectors.toList());

            // 2. 统计总学分（关联course表的ccredit字段）
            int totalCredit = 0;
            for (Sct sct : selected) {
                Course course = courseService.getCourseByCno(sct.getCno());
                if (course != null && course.getCcredit() != null) {
                    totalCredit += course.getCcredit();
                }
            }
            return Result.success("查询已选学分成功", totalCredit);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询学分失败：" + e.getMessage());
        }
    }
}