package com.scdb.studentcoursesystem.controller;

import com.scdb.studentcoursesystem.entity.Result;
import com.scdb.studentcoursesystem.entity.User;
import com.scdb.studentcoursesystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    // 原有固定密码（完全保留）
    private static final String ADMIN_PWD = "123456";
    private static final String STUDENT_PWD = "666666";
    private static final String TEACHER_PWD = "888888";

    // 登录接口（修复：补充role参数接收，确保Session存储完整）
    @PostMapping("/login")
    public Result<User> login(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role, // 新增：接收前端传的role参数
            HttpSession session) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                role == null || role.trim().isEmpty()) { // 新增role非空校验
            return Result.error("账号、密码和角色不能为空！");
        }
        String user = username.trim();
        String pwd = password.trim();
        String userRole = role.trim();
        User loginUser = new User();

        // 优先校验注册用户
        User registeredUser = userService.findByUsername(user);
        if (registeredUser != null) {
            if (!registeredUser.getPassword().equals(pwd)) {
                return Result.error("密码错误！");
            }
            // 校验注册用户的角色是否匹配
            if (!registeredUser.getRole().equals(userRole)) {
                return Result.error("角色不匹配！该账号注册为" + registeredUser.getRole() + "角色");
            }
            loginUser.setId(registeredUser.getId());
            loginUser.setUsername(registeredUser.getUsername());
            loginUser.setRole(registeredUser.getRole());
            loginUser.setRealName(registeredUser.getRealName());
        } else {
            // 未注册用户逻辑（完全保留原有）
            if ("admin".equals(user)) {
                if (!ADMIN_PWD.equals(pwd) || !"admin".equals(userRole)) {
                    return Result.error("密码错误或角色不匹配！管理员密码为123456");
                }
                loginUser.setId(1);
                loginUser.setUsername(user);
                loginUser.setRole("admin");
                loginUser.setRealName("系统管理员");
            } else if (user.startsWith("2023") || user.startsWith("2024")) {
                if (!STUDENT_PWD.equals(pwd) || !"student".equals(userRole)) {
                    return Result.error("密码错误或角色不匹配！未注册学生默认密码为666666");
                }
                loginUser.setId(2);
                loginUser.setUsername(user);
                loginUser.setRole("student");
                loginUser.setRealName("学生用户");
            } else if (user.startsWith("T")) {
                if (!TEACHER_PWD.equals(pwd) || !"teacher".equals(userRole)) {
                    return Result.error("密码错误或角色不匹配！未注册教师默认密码为888888");
                }
                loginUser.setId(3);
                loginUser.setUsername(user);
                loginUser.setRole("teacher");
                loginUser.setRealName("教师用户");
            } else {
                return Result.error("账号不存在！请先注册或使用正确格式账号");
            }
        }

        // 强制刷新Session，确保存储完整
        session.removeAttribute("loginUser");
        session.setAttribute("loginUser", loginUser);
        loginUser.setPassword(null);
        return Result.success("登录成功", loginUser);
    }

    // 原有退出登录（完全保留）
    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.invalidate();
        return Result.success("退出登录成功");
    }

    // 原有获取登录用户（完全保留）
    @PostMapping("/getLoginUser")
    public Result<User> getLoginUser(HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            return Result.error("未登录");
        }
        user.setPassword(null);
        return Result.success("获取登录用户成功", user);
    }

    // 教师注册接口（完全保留）
    @PostMapping("/register/teacher")
    public Result<Void> registerTeacher(@RequestBody User user) {
        if (!user.getUsername().startsWith("T")) {
            return Result.error("教师工号必须以T开头（如T001）");
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            return Result.error("该教师工号已注册");
        }
        boolean success = userService.addTeacher(user);
        return success ? Result.success("教师注册成功") : Result.error("教师注册失败");
    }
}