package com.scdb.studentcoursesystem.entity;

import lombok.Data;

/**
 * 通用返回结果类
 * 统一接口响应格式，支持成功/失败场景的快速构建
 */
@Data
public class Result<T> {
    // 响应码常量（增强可读性，避免魔法值）
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ERROR = 500;

    private int code;        // 响应码：200成功，500失败
    private String msg;      // 响应消息
    private T data;          // 响应数据

    // 空参构造（必须，保证JSON反序列化/框架反射创建实例成功）
    public Result() {}

    // 全参构造（用于特殊场景自定义响应）
    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功响应（带数据+自定义消息）
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(CODE_SUCCESS, msg, data);
    }

    // 成功响应（仅自定义消息）
    public static <T> Result<T> success(String msg) {
        return success(msg, null);
    }

    // 成功响应（仅数据，默认消息“操作成功”）
    public static <T> Result<T> success(T data) {
        return success("操作成功", data);
    }

    // 成功响应（无数据+默认消息）
    public static <T> Result<T> success() {
        return success("操作成功");
    }

    // 失败响应（自定义消息）
    public static <T> Result<T> error(String msg) {
        return new Result<>(CODE_ERROR, msg, null);
    }

    // 失败响应（带异常详情，方便调试）
    public static <T> Result<T> error(String msg, Throwable e) {
        // 生产环境可隐藏异常信息，仅保留msg
        String detailMsg = msg + "（详情：" + e.getMessage() + "）";
        return new Result<>(CODE_ERROR, detailMsg, null);
    }
}