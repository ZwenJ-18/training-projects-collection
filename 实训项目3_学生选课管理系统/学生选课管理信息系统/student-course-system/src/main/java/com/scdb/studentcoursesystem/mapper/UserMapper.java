package com.scdb.studentcoursesystem.mapper;

import com.scdb.studentcoursesystem.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {
    @Insert("INSERT INTO sys_user (username, password, role, real_name, phone) " +
            "VALUES (#{username}, #{password}, #{role}, #{realName}, #{phone})")
    int addUser(User user);

    @Select("SELECT id, username, password, role, real_name AS realName, phone FROM sys_user WHERE username = #{username}")
    User findByUsername(String username);

    @Update("UPDATE sys_user SET password = #{password} WHERE username = #{username}")
    int updatePassword(User user);

    @Update("UPDATE sys_user SET real_name = #{realName}, phone = #{phone} WHERE username = #{username}")
    int updateUserInfo(User user);

    // 根据用户名删除sys_user表记录
    @Delete("DELETE FROM sys_user WHERE username = #{username}")
    int deleteByUsername(String username);
}