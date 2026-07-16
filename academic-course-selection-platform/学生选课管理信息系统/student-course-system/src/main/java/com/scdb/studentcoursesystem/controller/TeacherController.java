package com.scdb.studentcoursesystem.controller;

import com.scdb.studentcoursesystem.entity.Result;
import com.scdb.studentcoursesystem.entity.Teacher;
import com.scdb.studentcoursesystem.entity.User;
import com.scdb.studentcoursesystem.service.TeacherService;
import com.scdb.studentcoursesystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    private final TeacherService teacherService;
    private final UserService userService;

    @Autowired
    public TeacherController(TeacherService teacherService, UserService userService) {
        this.teacherService = teacherService;
        this.userService = userService;
    }

    // 修复：获取登录教师信息接口（增强Session读取）
    @PostMapping("/getLoginTeacher")
    public Result<Teacher> getLoginTeacher(HttpServletRequest request) {
        try {
            // 强制获取Session，即使不存在也创建（避免空指针）
            HttpSession session = request.getSession(false);
            if (session == null) {
                return Result.error("未登录：Session不存在");
            }
            // 从Session取登录用户
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null) {
                return Result.error("未登录：Session中无用户信息");
            }
            if (!"teacher".equals(loginUser.getRole())) {
                return Result.error("登录角色不是教师：" + loginUser.getRole());
            }
            // 查询教师信息
            Teacher teacher = teacherService.findTeacherByTno(loginUser.getUsername());
            if (teacher == null) {
                return Result.error("教师信息不存在：" + loginUser.getUsername());
            }
            return Result.success("获取教师信息成功", teacher);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取教师信息失败：" + e.getMessage());
        }
    }

    // 教师列表查询（原有代码，未改动）
    @GetMapping("/list")
    public Result<List<Teacher>> findAll() {
        try {
            List<Teacher> teachers = teacherService.findAllTeachers();
            return Result.success("查询成功", teachers);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    // 单个教师查询（原有代码，未改动）
    @GetMapping("/{tno}")
    public Result<Teacher> findByTno(@PathVariable String tno) {
        try {
            Teacher teacher = teacherService.findTeacherByTno(tno);
            return Result.success("查询成功", teacher);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    // 管理员新增教师基础信息（原有代码，未改动）
    @PostMapping("/add")
    public Result<Boolean> addTeacher(@RequestBody Teacher teacher) {
        try {
            // 1. 教工号校验：T开头+8位
            if (teacher.getTno() == null || !teacher.getTno().startsWith("T") || teacher.getTno().length() != 8) {
                return Result.error("教工号格式错误！示例：T2024004（T+7位数字）");
            }
            // 2. 非空校验
            if (teacher.getTname() == null || teacher.getTname().trim().isEmpty()) {
                return Result.error("姓名不能为空！");
            }
            if (teacher.getTage() == null || teacher.getTage() < 24 || teacher.getTage() > 60) {
                return Result.error("年龄必须在24-60之间！");
            }
            // 3. 课程编号兼容
            if (teacher.getCno1() == null || teacher.getCno1().contains("语言")) {
                teacher.setCno1("C002");
            }
            // 4. 执行添加（仅添加到teacher表，不创建登录账号）
            boolean flag = teacherService.addTeacher(teacher);
            if (flag) {
                return Result.success("教师基础信息添加成功", true);
            } else {
                return Result.error("教工号已存在！请换一个编号");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加失败：" + e.getMessage());
        }
    }

    // 核心修改：支持更新3个主讲课程（cno1/cno2/cno3）
    @PutMapping("/update")
    public Result<Boolean> updateTeacher(@RequestBody Teacher teacher) {
        try {
            // 1. 查询数据库中现有教师信息
            Teacher existing = teacherService.findTeacherByTno(teacher.getTno());
            if (existing == null) {
                return Result.error("教师不存在");
            }

            // 2. 更新基础信息（保留原有逻辑）
            existing.setTname(teacher.getTname() != null ? teacher.getTname() : existing.getTname());
            existing.setTsex(teacher.getTsex() != null ? teacher.getTsex() : existing.getTsex());
            existing.setTage(teacher.getTage() != null ? teacher.getTage() : existing.getTage());
            existing.setTeb(existing.getTeb()); // 学历保留原值
            existing.setTpt(existing.getTpt()); // 职称保留原值

            // 3. 新增：更新3个主讲课程（支持空值）
            if (teacher.getCno1() != null && !teacher.getCno1().isEmpty()) {
                existing.setCno1(teacher.getCno1());
            }
            if (teacher.getCno2() != null) {
                existing.setCno2(teacher.getCno2().isEmpty() ? null : teacher.getCno2());
            }
            if (teacher.getCno3() != null) {
                existing.setCno3(teacher.getCno3().isEmpty() ? null : teacher.getCno3());
            }

            // 4. 执行更新
            boolean success = teacherService.updateTeacher(existing);
            return success ? Result.success("修改成功（含3个课程更新）", true) : Result.error("修改失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("修改失败：" + e.getMessage());
        }
    }

    // 教师信息删除（原有代码，未改动）
    @DeleteMapping("/{tno}")
    public Result<Boolean> deleteTeacher(@PathVariable String tno) {
        try {
            // 1. 先查询sys_user表是否有该账号，有则删除
            User existUser = userService.findByUsername(tno);
            if (existUser != null) {
                userService.deleteByUsername(tno); // 调用新增的删除方法
            }
            // 2. 再删除teacher表中的教师信息
            boolean flag = teacherService.deleteTeacher(tno);
            return flag ? Result.success("删除成功", true) : Result.error("删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    // 教师注册（仅删除后端MD5加密，其他逻辑完全保留）
    @PostMapping("/register")
    public Result<Boolean> teacherRegister(@RequestBody Teacher teacher) {
        try {
            // 1. 新增：校验是否已注册（避免重复注册报错）
            User existUser = userService.findByUsername(teacher.getTno());
            if (existUser != null) {
                return Result.error("该教工号已完成注册，无需重复注册！");
            }

            // 2. 校验管理员是否已新增基础信息
            Teacher existTeacher = teacherService.findTeacherByTno(teacher.getTno());
            if (existTeacher == null) {
                return Result.error("教工号未在系统中注册，请联系管理员添加基础信息");
            }

            // 3. 姓名一致性校验
            if (!existTeacher.getTname().trim().equals(teacher.getTname().trim())) {
                return Result.error("姓名与管理员录入的姓名（" + existTeacher.getTname() + "）不一致");
            }

            // 4. 密码非空校验
            if (teacher.getPassword() == null || teacher.getPassword().trim().isEmpty()) {
                return Result.error("登录密码不能为空！");
            }

            // 直接用前端加密后的密码
            String encryptPwd = teacher.getPassword().trim();

            // 6. 同步到sys_user表，创建登录账号
            User teacherUser = new User();
            teacherUser.setUsername(teacher.getTno());
            teacherUser.setPassword(encryptPwd); // 存储前端加密后的密码
            teacherUser.setRole("teacher");      // 标记为教师角色
            teacherUser.setRealName(teacher.getTname().trim());
            boolean userSaveFlag = userService.addUser(teacherUser);

            if (userSaveFlag) {
                // 7. 补充更新教师的性别、年龄等信息
                existTeacher.setTsex(teacher.getTsex());
                existTeacher.setTage(teacher.getTage());
                teacherService.updateTeacher(existTeacher);
                return Result.success("注册成功，请使用教工号和密码登录", true);
            } else {
                return Result.error("用户账号创建失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("注册失败：" + e.getMessage());
        }
    }

    // 教师课程查询（原有代码，未改动）
    @GetMapping("/course/list")
    public Result<Teacher> getTeacherCourse(@RequestParam String tno) {
        try {
            Teacher teacher = teacherService.findTeacherWithCourses(tno);
            return Result.success("课程查询成功", teacher);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("课程查询失败：" + e.getMessage());
        }
    }
}