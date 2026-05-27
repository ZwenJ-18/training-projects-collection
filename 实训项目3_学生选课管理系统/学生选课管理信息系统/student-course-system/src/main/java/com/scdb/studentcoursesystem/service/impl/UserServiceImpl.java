package com.scdb.studentcoursesystem.service.impl;

import com.scdb.studentcoursesystem.entity.User;
import com.scdb.studentcoursesystem.mapper.UserMapper;
import com.scdb.studentcoursesystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    // 原有学生注册方法（完全保留）
    @Override
    public boolean addUser(User user) {
        return userMapper.addUser(user) > 0;
    }

    // 原有根据用户名查询方法（完全保留）
    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    // 修复：重构为通用密码修改方法（兼容学生+教师）
    @Override
    public boolean updatePassword(String username, String oldPwd, String newPwd) {
        try {
            // 1. 查询用户（兼容学生学号/教师工号）
            User user = userMapper.findByUsername(username);
            if (user == null) {
                return false; // 用户不存在
            }
            // 2. 直接对比前端加密后的密码（教师密码前端已MD5加密，无需二次加密）
            if (!oldPwd.equals(user.getPassword())) {
                return false; // 原密码错误
            }
            // 3. 更新新密码（前端已加密，直接存储）
            user.setPassword(newPwd);
            return userMapper.updatePassword(user) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 原有学生信息修改方法（完全保留）
    @Override
    public boolean updateUserInfo(User user) {
        try {
            User existing = userMapper.findByUsername(user.getUsername());
            if (existing == null) {
                return false;
            }
            existing.setRealName(user.getRealName());
            existing.setPhone(user.getPhone());
            return userMapper.updateUserInfo(existing) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 新增：教师注册方法（完全保留）
    @Override
    public boolean addTeacher(User user) {
        user.setRole("teacher");
        return userMapper.addUser(user) > 0;
    }

    // 新增：根据用户名删除用户（完全保留）
    @Override
    public boolean deleteByUsername(String username) {
        return userMapper.deleteByUsername(username) > 0;
    }
}