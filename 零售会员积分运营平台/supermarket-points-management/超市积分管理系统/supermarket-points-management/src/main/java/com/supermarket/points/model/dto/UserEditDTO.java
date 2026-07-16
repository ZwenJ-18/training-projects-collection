package com.supermarket.points.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserEditDTO {
    @NotNull(message = "用户ID不能为空")
    private Integer id;
    @NotBlank(message = "用户名不能为空")
    private String username;
    private String phone;
    private String realName;
    private String nickname; // 已添加
    @NotNull(message = "积分不能为空")
    private Integer points;
    @NotNull(message = "状态不能为空")
    private Integer status;
}