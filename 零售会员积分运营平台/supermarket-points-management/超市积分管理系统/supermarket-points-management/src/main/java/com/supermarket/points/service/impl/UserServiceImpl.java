package com.supermarket.points.service.impl;

import com.supermarket.points.mapper.SysUserMapper;
import com.supermarket.points.model.dto.UserAddDTO;
import com.supermarket.points.model.dto.UserEditDTO;
import com.supermarket.points.model.entity.SysUser;
import com.supermarket.points.model.vo.PageVO;
import com.supermarket.points.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SysUser getUserById(Integer id) {
        return id == null ? null : sysUserMapper.selectById(id);
    }

    @Override
    public SysUser login(String username, String password) {
        if (username == null || password == null) return null;
        SysUser user = sysUserMapper.selectByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) return null;
        return user.getStatus() == 1 ? user : null;
    }

    @Override
    public SysUser getUserByUsername(String username) {
        return username == null ? null : sysUserMapper.selectByUsername(username);
    }

    @Override
    @Transactional
    public boolean addUser(UserAddDTO dto) {
        if (dto == null || getUserByUsername(dto.getUsername()) != null) return false;
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setRealName(dto.getRealName());

        String nickname = (dto.getNickname() == null || dto.getNickname().trim().isEmpty())
                ? dto.getRealName()
                : dto.getNickname().trim();
        user.setNickname(nickname);

        user.setPoints(dto.getPoints() == null ? 0 : dto.getPoints());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setStatus(dto.getStatus() == null ? 1 : dto.getStatus());
        user.setRole("user");
        return sysUserMapper.insert(user) > 0;
    }

    @Override
    public void deductPoints(String username, Integer points) {
        if (username == null || points == null || points < 0) return;
        SysUser user = getUserByUsername(username);
        if (user != null) {
            user.setPoints(Math.max(0, user.getPoints() - points));
            user.setUpdateTime(new Date());
            sysUserMapper.updateUser(user);
        }
    }

    @Override
    public PageVO<SysUser> getUserPage(Integer pageNum, Integer pageSize) {
        PageVO<SysUser> pageVO = new PageVO<>();
        try {
            pageNum = Math.max(1, pageNum);
            pageSize = Math.max(1, Math.min(100, pageSize));
            Integer offset = (pageNum - 1) * pageSize;
            List<SysUser> list = sysUserMapper.selectAll(offset, pageSize);
            Integer total = sysUserMapper.selectTotal() == null ? 0 : sysUserMapper.selectTotal();

            pageVO.setList(list);
            pageVO.setTotal(total);
            pageVO.setPageNum(pageNum);
            pageVO.setPageSize(pageSize);
            pageVO.setTotalPage(total == 0 ? 1 : (total + pageSize - 1) / pageSize);
        } catch (Exception e) {
            log.error("分页查询失败", e);
        }
        return pageVO;
    }

    @Override
    public void addPoints(String username, Integer points) {
        if (username == null || points == null || points < 0) return;
        SysUser user = getUserByUsername(username);
        if (user != null) {
            user.setPoints(user.getPoints() + points);
            user.setUpdateTime(new Date());
            sysUserMapper.updateUser(user);
        }
    }

    @Override
    public void updateUser(SysUser user) {
        if (user == null || user.getId() == null) return;
        user.setUpdateTime(new Date());
        sysUserMapper.updateUser(user);
    }

    @Override
    @Transactional
    public boolean updateUserPoints(Integer userId, Integer points) {
        if (userId == null || points == null) return false;
        SysUser user = new SysUser();
        user.setId(userId);
        user.setPoints(points);
        user.setUpdateTime(new Date());
        return sysUserMapper.updateUser(user) > 0;
    }

    @Override
    @Transactional
    public boolean editUser(UserEditDTO dto) {
        if (dto == null || dto.getId() == null) return false;
        SysUser user = sysUserMapper.selectById(dto.getId());
        if (user == null) return false;

        user.setPhone(dto.getPhone());
        user.setRealName(dto.getRealName());
        user.setNickname(dto.getNickname());
        user.setPoints(dto.getPoints() == null ? user.getPoints() : dto.getPoints());
        user.setStatus(dto.getStatus() == null ? user.getStatus() : dto.getStatus());
        user.setUpdateTime(new Date());
        return sysUserMapper.updateUser(user) > 0;
    }

    @Override
    @Transactional
    public boolean deleteUser(Integer id) {
        if (id == null) return false;
        SysUser user = sysUserMapper.selectById(id);
        if (user == null || "admin".equals(user.getRole())) return false;
        return sysUserMapper.deleteById(id) > 0;
    }

    @Override
    public void updateNickname(Integer userId, String nickname) {
        if (userId == null || nickname == null) return;
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) return;

        nickname = nickname.trim();
        if (nickname.isEmpty() || nickname.length() > 20) {
            throw new RuntimeException("昵称不能为空，且长度不能超过20字");
        }

        user.setNickname(nickname);
        user.setUpdateTime(new Date());
        sysUserMapper.updateUser(user);
    }

    @Override
    public void updatePassword(Integer userId, String oldPassword, String newPassword) {
        if (userId == null || oldPassword == null || newPassword == null) {
            throw new RuntimeException("参数不能为空");
        }

        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("旧密码不正确");
        }

        newPassword = newPassword.trim();
        if (newPassword.length() < 6 || newPassword.length() > 20) {
            throw new RuntimeException("新密码长度必须在 6-20 位");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(new Date());
        sysUserMapper.updateUser(user);
    }

    // 👇 管理员重置密码实现
    @Override
    @Transactional
    public void resetPassword(Integer userId, String newPassword) {
        if (userId == null || newPassword == null) {
            throw new RuntimeException("参数不能为空");
        }
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(new Date());
        sysUserMapper.updateUser(user);
    }
}