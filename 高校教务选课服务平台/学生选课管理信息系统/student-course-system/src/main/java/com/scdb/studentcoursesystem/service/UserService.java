package com.scdb.studentcoursesystem.service;

import com.scdb.studentcoursesystem.entity.User;
import java.util.List;

public interface UserService {
    // 原有学生相关核心方法（保持不变）
    boolean addUser(User user);
    User findByUsername(String username);
    boolean updatePassword(String sno, String oldPwd, String newPwd);
    boolean updateUserInfo(User user);

    // 新增教师注册专用方法（与学生逻辑对齐）
    boolean addTeacher(User user);

    // 新增：根据用户名删除sys_user表记录
    boolean deleteByUsername(String username);
}