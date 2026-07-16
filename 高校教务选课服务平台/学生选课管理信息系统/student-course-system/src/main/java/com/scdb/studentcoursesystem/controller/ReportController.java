package com.scdb.studentcoursesystem.controller;

import com.scdb.studentcoursesystem.entity.*;
import com.scdb.studentcoursesystem.entity.vo.*;
import com.scdb.studentcoursesystem.mapper.*;
import com.scdb.studentcoursesystem.service.*;
import com.scdb.studentcoursesystem.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/report")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    // 原有Service注入
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final DepartmentService departmentService;
    private final SctService sctService;

    // 新增注入报表所需Mapper
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private DepartmentMapper departmentMapper; // 新增：注入DepartmentMapper

    // 构造器注入（保留原有）
    @Autowired
    public ReportController(StudentService studentService,
                            TeacherService teacherService,
                            CourseService courseService,
                            DepartmentService departmentService,
                            SctService sctService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.departmentService = departmentService;
        this.sctService = sctService;
    }

    // ---------------------- 新增：报表页面跳转接口（前端访问入口） ----------------------
    @GetMapping("/student")
    public ModelAndView toStudentReport() { return new ModelAndView("report/studentReport"); }

    @GetMapping("/teacher")
    public ModelAndView toTeacherReport() { return new ModelAndView("report/teacherReport"); }

    @GetMapping("/course")
    public ModelAndView toCourseReport() { return new ModelAndView("report/courseReport"); }

    @GetMapping("/department")
    public ModelAndView toDeptReport() { return new ModelAndView("report/deptReport"); }

    @GetMapping("/sct")
    public ModelAndView toSctReport() { return new ModelAndView("report/sctReport"); }

    @GetMapping("/score")
    public ModelAndView toScoreReport() { return new ModelAndView("report/scoreReport"); }

    // ---------------------- 原有：数据概览报表（完全保留） ----------------------
    @GetMapping("/overview")
    public Result<Map<String, Object>> overview() {
        try {
            Map<String, Object> overview = new HashMap<>();
            // 基础统计
            overview.put("studentTotal", studentService.findAllStudents().size());
            overview.put("teacherTotal", teacherService.findAllTeachers().size());
            overview.put("courseTotal", courseService.getAllCourses().size());
            overview.put("deptTotal", departmentService.getAllDepartments().size());
            overview.put("sctTotal", sctService.findAllSct().size());

            // 院系学生分布
            List<Department> depts = departmentService.getAllDepartments();
            Map<String, Integer> deptStudentMap = new HashMap<>();
            for (Department dept : depts) {
                deptStudentMap.put(dept.getDname(), studentService.countStudentByDept(dept.getDno()));
            }
            overview.put("deptStudentDist", deptStudentMap);

            // 课程选课人数分布
            List<Course> courses = courseService.getAllCourses();
            Map<String, Integer> courseSctMap = new HashMap<>();
            for (Course course : courses) {
                courseSctMap.put(course.getCname(), sctService.countSctByCno(course.getCno()));
            }
            overview.put("courseSctDist", courseSctMap);

            return Result.success(overview);
        } catch (Exception e) {
            logger.error("生成概览报表异常", e);
            return Result.error("生成报表失败：" + e.getMessage());
        }
    }

    // ---------------------- 原有：明细报表（完全保留） ----------------------
    @GetMapping("/studentDetail")
    public Result<List<Student>> studentDetail() {
        try {
            return Result.success(studentService.findAllStudents());
        } catch (Exception e) {
            logger.error("生成学生明细报表异常", e);
            return Result.error("生成学生报表失败：" + e.getMessage());
        }
    }

    @GetMapping("/teacherDetail")
    public Result<List<Teacher>> teacherDetail() {
        try {
            return Result.success(teacherService.findAllTeachers());
        } catch (Exception e) {
            logger.error("生成教师明细报表异常", e);
            return Result.error("生成教师报表失败：" + e.getMessage());
        }
    }

    @GetMapping("/courseDetail")
    public Result<List<Course>> courseDetail() {
        try {
            return Result.success(courseService.getAllCourses());
        } catch (Exception e) {
            logger.error("生成课程明细报表异常", e);
            return Result.error("生成课程报表失败：" + e.getMessage());
        }
    }

    @GetMapping("/sctDetail")
    public Result<List<Sct>> sctDetail() {
        try {
            return Result.success(sctService.findAllSct());
        } catch (Exception e) {
            logger.error("生成选课明细报表异常", e);
            return Result.error("生成选课报表失败：" + e.getMessage());
        }
    }

    // ---------------------- 新增：多条件筛选报表接口（核心功能） ----------------------
    // 1. 学生报表（支持学号/姓名模糊、院系精确筛选）- 修复院系全称显示问题
    @GetMapping("/student/data")
    public Result<Map<String, Object>> getStudentReportData(
            @RequestParam(required = false) String sno,
            @RequestParam(required = false) String sname,
            @RequestParam(required = false) String sdept
    ) {
        try {
            List<Student> studentList = studentService.findAllStudents();
            // 多条件过滤 + 转换为VO
            List<StudentReportVO> voList = studentList.stream()
                    .filter(student -> (sno == null || sno.isEmpty() || student.getSno().contains(sno)))
                    .filter(student -> (sname == null || sname.isEmpty() || student.getSname().contains(sname)))
                    .filter(student -> (sdept == null || sdept.isEmpty() || student.getSdept().equals(sdept)))
                    .map(student -> {
                        StudentReportVO vo = new StudentReportVO();
                        vo.setSno(student.getSno());
                        vo.setSname(student.getSname());
                        vo.setSsex(student.getSsex());
                        vo.setSage(student.getSage());
                        vo.setSdept(student.getSdept());
                        vo.setMajor(student.getMajor());
                        vo.setClassName(student.getClassName());
                        vo.setDno(student.getDno());
                        // 修复：根据学生当前的sdept（院系名称）查询院系全称，而非旧的dno
                        Department dept = null;
                        try {
                            // 先通过名称查院系
                            dept = departmentMapper.selectByDname(student.getSdept());
                            // 兜底：名称查不到时用dno查
                            if (dept == null) {
                                dept = departmentService.getDepartmentByDno(student.getDno());
                            }
                        } catch (Exception e) {
                            logger.warn("查询学生{}的院系名称失败", student.getSno(), e);
                        }
                        vo.setDname(dept != null ? dept.getDname() : "未知院系");
                        return vo;
                    }).collect(Collectors.toList());

            return Result.success(buildReportData(voList, voList.size()));
        } catch (Exception e) {
            logger.error("学生报表查询失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    // 2. 教师报表（支持教工号/姓名模糊、职称精确筛选）
    @GetMapping("/teacher/data")
    public Result<Map<String, Object>> getTeacherReportData(
            @RequestParam(required = false) String tno,
            @RequestParam(required = false) String tname,
            @RequestParam(required = false) String tpt
    ) {
        try {
            List<Teacher> teacherList = teacherService.findAllTeachers();
            List<TeacherReportVO> voList = teacherList.stream()
                    .filter(teacher -> (tno == null || tno.isEmpty() || teacher.getTno().contains(tno)))
                    .filter(teacher -> (tname == null || tname.isEmpty() || teacher.getTname().contains(tname)))
                    .filter(teacher -> (tpt == null || tpt.isEmpty() || teacher.getTpt().equals(tpt)))
                    .map(teacher -> {
                        TeacherReportVO vo = new TeacherReportVO();
                        vo.setTno(teacher.getTno());
                        vo.setTname(teacher.getTname());
                        vo.setTsex(teacher.getTsex());
                        vo.setTage(teacher.getTage());
                        vo.setTeb(teacher.getTeb());
                        vo.setTpt(teacher.getTpt());
                        vo.setCno1(teacher.getCno1());
                        vo.setCno2(teacher.getCno2());
                        vo.setCno3(teacher.getCno3());
                        // 补充课程名称
                        vo.setCname1(teacher.getCno1() != null ? courseService.getCourseByCno(teacher.getCno1()).getCname() : "");
                        vo.setCname2(teacher.getCno2() != null ? courseService.getCourseByCno(teacher.getCno2()).getCname() : "");
                        vo.setCname3(teacher.getCno3() != null ? courseService.getCourseByCno(teacher.getCno3()).getCname() : "");
                        return vo;
                    }).collect(Collectors.toList());

            return Result.success(buildReportData(voList, voList.size()));
        } catch (Exception e) {
            logger.error("教师报表查询失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    // 3. 课程报表（支持课程号/名称模糊、院系精确筛选）
    @GetMapping("/course/data")
    public Result<Map<String, Object>> getCourseReportData(
            @RequestParam(required = false) String cno,
            @RequestParam(required = false) String cname,
            @RequestParam(required = false) String cdept
    ) {
        try {
            List<Course> courseList = courseService.getAllCourses();
            List<CourseReportVO> voList = courseList.stream()
                    .filter(course -> (cno == null || cno.isEmpty() || course.getCno().contains(cno)))
                    .filter(course -> (cname == null || cname.isEmpty() || course.getCname().contains(cname)))
                    .filter(course -> (cdept == null || cdept.isEmpty() || course.getCdept().equals(cdept)))
                    .map(course -> {
                        CourseReportVO vo = new CourseReportVO();
                        vo.setCno(course.getCno());
                        vo.setCname(course.getCname());
                        vo.setCpno(course.getCpno());
                        vo.setCcredit(course.getCcredit());
                        vo.setCtime(course.getCtime());
                        vo.setCdept(course.getCdept());
                        vo.setTeacher(course.getTeacher());
                        // 补充先修课名称
                        vo.setCpname(course.getCpno() != null ?
                                (courseService.getCourseByCno(course.getCpno()) != null ?
                                        courseService.getCourseByCno(course.getCpno()).getCname() : "无") : "无");
                        return vo;
                    }).collect(Collectors.toList());

            return Result.success(buildReportData(voList, voList.size()));
        } catch (Exception e) {
            logger.error("课程报表查询失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    // 4. 院系报表（解决学生数为0问题：改用联表统计）
    @GetMapping("/department/data")
    public Result<Map<String, Object>> getDeptReportData(
            @RequestParam(required = false) String dno,
            @RequestParam(required = false) String dname
    ) {
        try {
            // 调用联表统计方法，直接获取带学生数、课程数的院系数据
            List<DepartmentReportVO> voList = departmentMapper.selectDeptReportWithCount();

            // 保留原筛选逻辑（按dno/名称模糊筛选）
            if (dno != null && !dno.isEmpty()) {
                voList = voList.stream().filter(dept -> dept.getDno().contains(dno)).collect(Collectors.toList());
            }
            if (dname != null && !dname.isEmpty()) {
                voList = voList.stream().filter(dept -> dept.getDname().contains(dname)).collect(Collectors.toList());
            }

            return Result.success(buildReportData(voList, voList.size()));
        } catch (Exception e) {
            logger.error("院系报表查询失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    // 5. 选课报表（支持学号模糊、课程号/学期精确筛选）
    @GetMapping("/sct/data")
    public Result<Map<String, Object>> getSctReportData(
            @RequestParam(required = false) String sno,
            @RequestParam(required = false) String cno,
            @RequestParam(required = false) String semester
    ) {
        try {
            List<Sct> sctList = sctService.findAllSct();
            List<SctReportVO> voList = sctList.stream()
                    .filter(sct -> (sno == null || sno.isEmpty() || sct.getSno().contains(sno)))
                    .filter(sct -> (cno == null || cno.isEmpty() || sct.getCno().equals(cno)))
                    .filter(sct -> (semester == null || semester.isEmpty() || sct.getSemester().equals(semester)))
                    .map(sct -> {
                        SctReportVO vo = new SctReportVO();
                        vo.setSno(sct.getSno());
                        vo.setSname(studentService.findStudentBySno(sct.getSno()).getSname());
                        vo.setCno(sct.getCno());
                        vo.setCname(courseService.getCourseByCno(sct.getCno()).getCname());
                        vo.setSemester(sct.getSemester());
                        vo.setScore(sct.getScore() == null ? "未录入" : sct.getScore().toString());
                        vo.setTeacher(courseService.getCourseByCno(sct.getCno()).getTeacher());
                        vo.setCcredit(courseService.getCourseByCno(sct.getCno()).getCcredit());
                        return vo;
                    }).collect(Collectors.toList());

            return Result.success(buildReportData(voList, voList.size()));
        } catch (Exception e) {
            logger.error("选课报表查询失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    // 6. 成绩报表（复用ScoreMapper，支持学号/课程号/学期筛选）
    @GetMapping("/score/data")
    public Result<Map<String, Object>> getScoreReportData(
            @RequestParam(required = false) String sno,
            @RequestParam(required = false) String cno,
            @RequestParam(required = false) String semester
    ) {
        try {
            List<ScoreCourseVO> voList;
            // 按学号查
            if (sno != null && !sno.isEmpty()) {
                voList = scoreMapper.selectScoreWithCourseBySno(sno);
            }
            // 按课程号查
            else if (cno != null && !cno.isEmpty()) {
                List<Map<String, Object>> courseScores = scoreMapper.selectScoresByCourse(cno);
                voList = courseScores.stream().map(item -> {
                    ScoreCourseVO vo = new ScoreCourseVO();
                    vo.setSno(item.get("sno").toString());
                    vo.setSname(item.get("sname").toString());
                    vo.setCno(cno);
                    vo.setScore(item.get("score").toString().isEmpty() ? 0 : Integer.parseInt(item.get("score").toString()));
                    Course course = courseService.getCourseByCno(cno);
                    if (course != null) {
                        vo.setCname(course.getCname());
                        vo.setCcredit(course.getCcredit());
                    }
                    return vo;
                }).collect(Collectors.toList());
            }
            // 查所有成绩
            else {
                List<Score> allScore = scoreMapper.findAll();
                voList = allScore.stream().map(score -> {
                    ScoreCourseVO vo = new ScoreCourseVO();
                    vo.setSno(score.getSno());
                    vo.setCno(score.getCno());
                    vo.setScore(score.getScore() == null ? 0 : score.getScore());
                    vo.setSemester(score.getSemester() == null ? "未指定" : score.getSemester());
                    // 补充课程/学生名称
                    Course course = courseService.getCourseByCno(score.getCno());
                    vo.setCname(course == null ? "未知课程" : course.getCname());
                    Student student = studentService.findStudentBySno(score.getSno());
                    vo.setSname(student == null ? "未知学生" : student.getSname());
                    return vo;
                }).collect(Collectors.toList());
            }

            return Result.success(buildReportData(voList, voList.size()));
        } catch (Exception e) {
            logger.error("成绩报表查询失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    // ---------------------- 公共方法：封装报表返回数据 ----------------------
    private Map<String, Object> buildReportData(List<?> list, Integer total) {
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);       // 报表数据列表
        data.put("total", total);     // 总记录数
        data.put("queryTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // 查询时间
        return data;
    }
}