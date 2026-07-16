package com.supermarket.points.mapper;

import com.supermarket.points.model.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper {
    // 仅保留方法声明，所有SQL逻辑移到XML文件，消除注解冲突
    SysUser selectById(Integer id);

    int updateUser(SysUser user);

    int deleteById(Integer id);

    List<SysUser> selectAll(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    Integer selectTotal();

    SysUser selectByUsername(String username);

    int insert(SysUser user);

    int updatePoints(@Param("userId") Integer userId, @Param("points") Integer points);
}