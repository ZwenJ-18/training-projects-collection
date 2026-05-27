package com.supermarket.points.service;

import com.supermarket.points.model.dto.UserAddDTO;
import com.supermarket.points.model.dto.UserEditDTO;
import com.supermarket.points.model.entity.SysUser;
import com.supermarket.points.model.vo.PageVO;

public interface UserService {
    SysUser getUserByUsername(String username);
    SysUser login(String username, String password);
    SysUser getUserById(Integer id);
    void updateUser(SysUser user);
    void updatePassword(Integer userId, String oldPassword, String newPassword);
    void deductPoints(String username, Integer points);
    void addPoints(String username, Integer points);
    boolean addUser(UserAddDTO dto);
    boolean editUser(UserEditDTO dto);
    boolean deleteUser(Integer id);
    PageVO<SysUser> getUserPage(Integer pageNum, Integer pageSize);
    boolean updateUserPoints(Integer userId, Integer points);
    void updateNickname(Integer userId, String nickname);

    // 👇 管理员重置密码
    void resetPassword(Integer userId, String newPassword);
}