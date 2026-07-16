package com.scdb.studentcoursesystem.controller;

import com.scdb.studentcoursesystem.entity.Result;
import com.scdb.studentcoursesystem.entity.User;
import com.scdb.studentcoursesystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 原有：根据用户名查询用户信息（仅返回电话，隐藏密码）
    @GetMapping("/info")
    public Result<User> getUserInfoByUsername(@RequestParam String username) {
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                return Result.error("用户不存在");
            }
            // 隐藏敏感信息，仅保留电话
            User resUser = new User();
            resUser.setPhone(user.getPhone());
            return Result.success(resUser);
        } catch (Exception e) {
            return Result.error("查询联系电话失败：" + e.getMessage());
        }
    }

    // 新增：通用密码修改接口（兼容学生/教师）
    @PostMapping("/updatePassword")
    public Result<Boolean> updatePassword(@RequestBody Map<String, String> params) {
        try {
            // 获取前端传递的参数
            String username = params.get("username");
            String oldPwd = params.get("oldPwd");
            String newPwd = params.get("newPwd");

            // 参数校验
            if (username == null || username.trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (oldPwd == null || oldPwd.trim().isEmpty()) {
                return Result.error("原密码不能为空");
            }
            if (newPwd == null || newPwd.trim().isEmpty()) {
                return Result.error("新密码不能为空");
            }

            // 调用服务层修改密码
            boolean success = userService.updatePassword(username, oldPwd, newPwd);
            if (success) {
                return Result.success("修改密码成功", true);
            } else {
                return Result.error("原密码错误或用户不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("修改密码失败：" + e.getMessage());
        }
    }
}