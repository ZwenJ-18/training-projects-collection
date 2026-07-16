package com.supermarket.points.controller;

import com.supermarket.points.model.entity.SysUser;
import com.supermarket.points.service.UserService; // 修正：导入UserService
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    // 修正：注入UserService（你的服务接口实际叫UserService）
    @Autowired
    private UserService userService;

    // 访问/login时跳转到登录页面
    @GetMapping("/login")
    public String toLoginPage() {
        return "login";
    }

    // 处理登录请求
    @PostMapping("/login")
    public String doLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model
    ) {
        // 调用UserService的login方法（已在UserServiceImpl中实现）
        SysUser loginUser = userService.login(username, password);
        if (loginUser == null) {
            model.addAttribute("errorMsg", "用户名或密码错误");
            return "login";
        }

        session.setAttribute("loginUser", loginUser);

        // 修正：确保SysUser实体类有getRole()方法（如果没有，需给SysUser添加role字段）
        if ("admin".equals(loginUser.getRole())) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/user/home";
        }
    }
}