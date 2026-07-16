package com.scdb.studentcoursesystem.controller;
import com.scdb.studentcoursesystem.entity.Result;
import com.scdb.studentcoursesystem.entity.Student;
import com.scdb.studentcoursesystem.entity.User;
import com.scdb.studentcoursesystem.service.StudentService;
import com.scdb.studentcoursesystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("/student/info")
public class StudentInfoController {
    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;
    // 密码修改方法：保持不变（已匹配修复后的updatePassword）
    @PostMapping("/updatePwd")
    public Result<?> updatePassword(
            @RequestParam String oldPwd,
            @RequestParam String newPwd,
            @RequestParam String confirmPwd,
            HttpSession session) {
        try {
            if (!newPwd.equals(confirmPwd)) {
                return Result.error("两次新密码不一致");
            }
            if (newPwd.length() < 6) {
                return Result.error("新密码长度不能少于6位");
            }
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null) {
                return Result.error("未登录，请重新登录");
            }
            // 调用修复后的3参数updatePassword
            boolean success = userService.updatePassword(loginUser.getUsername(), oldPwd, newPwd);
            return success ? Result.success("密码修改成功，请重新登录") : Result.error("原密码错误，修改失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("修改密码异常：" + e.getMessage());
        }
    }
    // 获取个人信息方法：保持不变
    @GetMapping("/getInfo")
    public Result<User> getUserInfo(HttpSession session) {
        try {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null) {
                return Result.error("未登录，请重新登录");
            }
            User user = userService.findByUsername(loginUser.getUsername());
            if (user == null) {
                return Result.error("用户不存在");
            }
            user.setPassword(null); // 隐藏密码
            return Result.success("获取个人信息成功", user);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取信息失败：" + e.getMessage());
        }
    }
    // 5. 修复：参数转换异常（Map转Integer时加判断，避免空指针）
    @PostMapping("/updateInfo")
    public Result<?> updateUserInfo(@RequestBody Map<String, Object> params, HttpSession session) {
        try {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null) {
                return Result.error("未登录，请重新登录");
            }
            String sno = loginUser.getUsername();
            if (sno == null || sno.isEmpty()) {
                return Result.error("学号不能为空");
            }
            // 解析参数：加非空判断，避免强转异常
            String realName = (String) params.get("realName");
            String phone = (String) params.get("phone");
            String sname = (String) params.get("sname");
            String ssex = (String) params.get("ssex");
            // 修复：Integer类型参数加空判断，避免NullPointerException
            Integer sage = params.get("sage") != null ? (Integer) params.get("sage") : null;
            String className = (String) params.get("className");
            // 更新User表：调用修复后的updateUserInfo
            User user = new User();
            user.setUsername(sno);
            user.setRealName(realName);
            user.setPhone(phone);
            boolean userSuccess = userService.updateUserInfo(user);
            // 更新Student表：保持不变
            Student student = studentService.findStudentBySno(sno);
            if (student != null) {
                student.setSname(sname);
                student.setSsex(ssex);
                student.setSage(sage);
                student.setClassName(className);
                studentService.updateStudent(student);
            } else {
                return Result.error("学生信息不存在，无法更新");
            }
            return userSuccess ? Result.success("个人信息修改成功") : Result.error("信息修改失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("信息修改异常：" + e.getMessage());
        }
    }
}