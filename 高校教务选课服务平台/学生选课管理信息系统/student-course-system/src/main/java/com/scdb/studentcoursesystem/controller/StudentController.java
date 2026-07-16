package com.scdb.studentcoursesystem.controller;

import com.scdb.studentcoursesystem.entity.Result;
import com.scdb.studentcoursesystem.entity.Student;
import com.scdb.studentcoursesystem.entity.User;
import com.scdb.studentcoursesystem.service.StudentService;
import com.scdb.studentcoursesystem.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    // 院系名称 -> 院系编号 映射表（与数据库一致）
    private static final Map<String, String> DEPT_NAME_TO_DNO = new HashMap<>();
    static {
        DEPT_NAME_TO_DNO.put("计算机科学与技术学院", "001");
        DEPT_NAME_TO_DNO.put("电子信息系", "002");
        DEPT_NAME_TO_DNO.put("数学系", "003");
        DEPT_NAME_TO_DNO.put("外国语学院", "011");
    }

    private final StudentService studentService;
    @Autowired
    private UserService userService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/list")
    public Result<List<Student>> findAll() {
        try {
            List<Student> students = studentService.findAllStudents();
            return Result.success("查询所有学生成功", students);
        } catch (Exception e) {
            logger.error("查询所有学生失败", e);
            return Result.error("查询所有学生失败：" + e.getMessage());
        }
    }

    @GetMapping("/{sno}")
    public Result<Student> findBySno(@PathVariable String sno) {
        try {
            if (sno == null || sno.trim().isEmpty()) {
                return Result.error("学号不能为空！");
            }
            Student student = studentService.findStudentBySno(sno.trim());
            if (student == null) {
                return Result.error("未查询到学号【" + sno + "】对应的学生信息！");
            }
            return Result.success("查询学生成功", student);
        } catch (Exception e) {
            logger.error("查询学生失败", e);
            return Result.error("查询学生失败：" + e.getMessage());
        }
    }

    @PostMapping("/add")
    public Result<Boolean> addStudent(@RequestBody Student student) {
        try {
            // 1. 全字段非空/有效性校验
            if (student.getSno() == null || student.getSno().trim().isEmpty()) {
                return Result.error("学号不能为空！");
            }
            String sno = student.getSno().trim();
            if (student.getSname() == null || student.getSname().trim().isEmpty()) {
                return Result.error("姓名不能为空！");
            }
            if (student.getSsex() == null || student.getSsex().trim().isEmpty()) {
                return Result.error("性别不能为空！");
            }
            if (student.getSage() == null || student.getSage() < 14 || student.getSage() > 24) {
                return Result.error("年龄必须在14-24之间！");
            }
            String deptName = student.getSdept();
            if (deptName == null || deptName.trim().isEmpty()) {
                return Result.error("院系不能为空！");
            }
            if (student.getMajor() == null || student.getMajor().trim().isEmpty()) {
                return Result.error("专业不能为空！");
            }
            if (student.getClassName() == null || student.getClassName().trim().isEmpty()) {
                return Result.error("班级不能为空！");
            }

            // 2. 院系名称转dno
            String dno = DEPT_NAME_TO_DNO.get(deptName.trim());
            if (dno == null) {
                return Result.error("添加学生失败：未知院系【" + deptName + "】，请选择正确院系！");
            }
            student.setDno(dno);

            // 3. 学号重复校验
            Student existStudent = studentService.findStudentBySno(sno);
            if (existStudent != null) {
                return Result.error("添加学生失败：学号【" + sno + "】已存在");
            }

            // 4. 执行添加
            boolean flag = studentService.addStudent(student);
            if (flag) {
                return Result.success("添加学生成功", true);
            } else {
                return Result.error("添加学生失败：数据库插入异常");
            }
        } catch (Exception e) {
            // 捕获所有异常，同时打印SQL相关信息（若有）
            logger.error("添加学生失败", e);
            String errorMsg = "添加学生失败：" + e.getMessage();
            // 若异常包含SQL信息，补充提示
            if (e.getMessage().contains("SQL") || e.getMessage().contains("database")) {
                errorMsg += "（数据库异常，请检查字段是否匹配）";
            }
            return Result.error(errorMsg);
        }
    }

    // 核心修复：updateStudent方法
    @PutMapping("/update")
    public Result<Boolean> updateStudent(@RequestBody Student student) {
        try {
            // 1. 校验学号非空
            if (student.getSno() == null || student.getSno().trim().isEmpty()) {
                return Result.error("学号不能为空！");
            }
            String sno = student.getSno().trim();

            // 2. 提前校验学号是否存在
            Student existStudent = studentService.findStudentBySno(sno);
            if (existStudent == null) {
                return Result.error("修改学生失败：学号【" + sno + "】不存在");
            }

            // 3. 院系名称转dno（强制校验，避免dno为空）
            String deptName = student.getSdept();
            if (deptName == null || deptName.trim().isEmpty()) {
                return Result.error("修改学生失败：院系不能为空！");
            }
            String dno = DEPT_NAME_TO_DNO.get(deptName.trim());
            if (dno == null) {
                return Result.error("修改学生失败：未知院系【" + deptName + "】，请选择正确院系！");
            }
            student.setDno(dno);

            // 4. 补充默认值（避免前端未传的字段为空）
            if (student.getSname() == null || student.getSname().trim().isEmpty()) {
                student.setSname(existStudent.getSname());
            }
            if (student.getSsex() == null || student.getSsex().trim().isEmpty()) {
                student.setSsex(existStudent.getSsex());
            }
            if (student.getSage() == null || student.getSage() < 14 || student.getSage() > 24) {
                student.setSage(existStudent.getSage());
            }
            // 补充专业、班级默认值（避免update时字段为空）
            if (student.getMajor() == null || student.getMajor().trim().isEmpty()) {
                student.setMajor(existStudent.getMajor());
            }
            if (student.getClassName() == null || student.getClassName().trim().isEmpty()) {
                student.setClassName(existStudent.getClassName());
            }

            // 5. 执行修改
            boolean flag = studentService.updateStudent(student);
            return flag ? Result.success("修改学生成功", true) : Result.error("修改学生失败：数据库操作异常");
        } catch (Exception e) {
            logger.error("修改学生失败", e);
            return Result.error("修改学生失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{sno}")
    public Result<Boolean> deleteStudent(@PathVariable String sno) {
        try {
            if (sno == null || sno.trim().isEmpty()) {
                return Result.error("学号不能为空！");
            }
            Student student = studentService.findStudentBySno(sno.trim());
            if (student == null) {
                return Result.error("删除失败：学号【" + sno + "】不存在！");
            }
            boolean flag = studentService.deleteStudent(sno.trim());
            return flag ? Result.success("删除学生成功", true) : Result.error("删除学生失败");
        } catch (Exception e) {
            logger.error("删除学生失败", e);
            return Result.error("删除学生失败：" + e.getMessage());
        }
    }

    @GetMapping("/checkDeptHasStudent/{dno}")
    public Result<Boolean> checkDeptHasStudent(@PathVariable String dno) {
        try {
            if (dno == null || dno.trim().isEmpty()) {
                return Result.error("院系编号不能为空！");
            }
            int count = studentService.countStudentByDept(dno.trim());
            return Result.success("校验院系学生数量成功", count > 0);
        } catch (Exception e) {
            logger.error("校验院系学生数量失败", e);
            return Result.error("校验院系学生数量失败：" + e.getMessage());
        }
    }

    @GetMapping("/admin/list")
    public Result<List<Student>> getAdminStudentList() {
        return findAll();
    }

    @PostMapping("/register")
    public Result<?> studentRegister(@RequestBody Map<String, Object> registerData) {
        try {
            String sno = (String) registerData.get("sno");
            String sname = (String) registerData.get("sname");
            String password = (String) registerData.get("password");
            String ssex = (String) registerData.get("ssex");
            Integer sage = (Integer) registerData.get("sage");

            if (sno == null || sno.trim().isEmpty()) {
                return Result.error("学号不能为空！");
            }
            if (password == null || password.trim().isEmpty()) {
                return Result.error("密码不能为空！");
            }
            if (password.length() < 6) {
                return Result.error("密码长度不能少于6位！");
            }

            Student existStudent = studentService.findStudentBySno(sno.trim());
            if (existStudent == null) {
                return Result.error("学号【" + sno + "】未录入系统，请联系管理员！");
            }

            if (!existStudent.getSname().equals(sname)) {
                return Result.error("姓名与学号绑定的信息不一致！");
            }

            User existingUser = userService.findByUsername(sno.trim());
            if (existingUser != null) {
                return Result.error("该学号已注册，请直接登录！");
            }

            User user = new User();
            user.setUsername(sno.trim());
            user.setPassword(password);
            user.setRole("student");
            user.setRealName(sname);
            boolean isAdded = userService.addUser(user);
            if (!isAdded) {
                return Result.error("注册失败，请重试！");
            }

            existStudent.setSsex(ssex);
            existStudent.setSage(sage);
            studentService.updateStudent(existStudent);

            return Result.success("注册成功！请使用学号和密码登录");

        } catch (Exception e) {
            logger.error("学生注册失败", e);
            return Result.error("注册失败：" + e.getMessage());
        }
    }

    @GetMapping("/course/{cno}")
    public Result<List<Student>> getStudentsByCourse(@PathVariable String cno) {
        try {
            if (cno == null || cno.trim().isEmpty()) {
                return Result.error("课程号不能为空！");
            }
            List<Student> students = studentService.findStudentsByCourse(cno.trim());
            return Result.success("查询选课学生成功", students);
        } catch (Exception e) {
            logger.error("查询选课学生失败", e);
            return Result.error("查询选课学生失败：" + e.getMessage());
        }
    }
}