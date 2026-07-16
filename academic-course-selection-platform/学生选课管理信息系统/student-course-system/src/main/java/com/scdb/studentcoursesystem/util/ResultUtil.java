package com.scdb.studentcoursesystem.util;

/**
 * 统一返回结果
 */
public class ResultUtil<T> {
    private Integer code; // 200成功，500失败
    private String msg;   // 提示信息
    private T data;       // 数据

    // 手动添加setter方法（替代Lombok的@Data）
    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    // 成功（带数据）
    public static <T> ResultUtil<T> success(T data) {
        ResultUtil<T> result = new ResultUtil<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    // 成功（无数据）
    public static <T> ResultUtil<T> success() {
        ResultUtil<T> result = new ResultUtil<>();
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }

    // 失败
    public static <T> ResultUtil<T> error(String msg) {
        ResultUtil<T> result = new ResultUtil<>();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }
}