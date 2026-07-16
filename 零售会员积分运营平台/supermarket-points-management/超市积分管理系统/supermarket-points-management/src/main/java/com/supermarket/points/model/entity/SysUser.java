package com.supermarket.points.model.entity;

import lombok.Data;
import java.util.Date;

@Data
public class SysUser {
    private Integer id;
    private String username;
    private String password;
    private String phone;
    private String realName;
    private Integer points;
    private Date createTime;
    private Date updateTime;
    private Integer status;
    private String role;
    private String nickname;
}