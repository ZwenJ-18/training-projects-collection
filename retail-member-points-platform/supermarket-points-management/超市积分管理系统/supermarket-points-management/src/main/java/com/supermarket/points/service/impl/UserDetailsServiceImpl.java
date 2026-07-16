package com.supermarket.points.service.impl;

import com.supermarket.points.model.entity.SysUser;
import com.supermarket.points.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 查询用户
        SysUser user = userService.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        if (user.getStatus() == 0) {
            throw new UsernameNotFoundException("用户已禁用");
        }

        // 2. 从数据库获取用户真实角色（不再固定写死ADMIN）
        String userRole = user.getRole(); // 数据库中role字段是"admin"或"user"

        // 3. 返回对应角色的UserDetails
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(userRole.toUpperCase()) // 角色转大写（Spring Security要求）
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}