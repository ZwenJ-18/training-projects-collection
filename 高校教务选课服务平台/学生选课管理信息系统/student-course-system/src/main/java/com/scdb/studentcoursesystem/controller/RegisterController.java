package com.scdb.studentcoursesystem.controller;
import com.scdb.studentcoursesystem.entity.Result;
import com.scdb.studentcoursesystem.entity.Student;
import com.scdb.studentcoursesystem.entity.User;
import com.scdb.studentcoursesystem.service.StudentService;
import com.scdb.studentcoursesystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<?> register(
            @RequestParam("sno") String sno,
            @RequestParam("sname") String sname,
            @RequestParam("password") String password,
            @RequestParam("ssex") String ssex,
            @RequestParam("sage") Integer sage,
            @RequestParam("major") String major,
            @RequestParam("sdept") String sdept
    ) {
        try {
            // 1. 校验学号是否已注册
            User existUser = userService.findByUsername(sno);
            if (existUser != null) {
                return Result.error("注册失败：学号已存在");
            }

            // 2. 校验学号是否已被管理员添加（核心：关联管理员录入的学生信息）
            Student existStudent = studentService.findStudentBySno(sno);
            if (existStudent == null) {
                return Result.error("注册失败：该学号未被管理员录入系统");
            }
            // 校验姓名一致性
            if (!existStudent.getSname().equals(sname)) {
                return Result.error("注册失败：姓名与管理员录入的不一致");
            }

            // 3. 保存用户账号（仅新增账号，不重复添加学生信息）
            User user = new User();
            user.setUsername(sno);
            user.setPassword(password); // 明文存储（生产环境建议加密）
            user.setRole("student");
            user.setRealName(sname);
            boolean userSuccess = userService.addUser(user);

            return userSuccess ? Result.success("注册成功！请用学号和设置的密码登录") : Result.error("注册失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("注册失败：系统异常 - " + e.getMessage());
        }
    }
}