package com.supermarket.points.model.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
public class UserAddDTO {
    // 用户名：非空 + 长度4-20位
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度4-20位")
    private String username;

    // 密码：非空 + 长度6-20位
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20位")
    private String password;

    // 手机号：非空 + 正则校验11位手机号（比单纯长度更严谨）
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phone;

    // 真实姓名：非空
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    // 昵称
    private String nickname;

    // 补充积分字段：非空 + 必须是数字（前端传0也可以，不能传空）
    @NotNull(message = "积分不能为空")
    private Integer points;

    // 补充状态字段：非空（1=启用，0=禁用）
    @NotNull(message = "用户状态不能为空")
    private Integer status;
}