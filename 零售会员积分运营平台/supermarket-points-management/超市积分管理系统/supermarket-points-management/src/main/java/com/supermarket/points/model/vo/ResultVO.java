package com.supermarket.points.model.vo;

import lombok.Data;

@Data
public class ResultVO<T> {
    private Integer code;       // 状态码（200-成功，500-失败）
    private String msg;         // 提示信息
    private T data;             // 响应数据

    // 成功响应（带数据）
    public static <T> ResultVO<T> success(T data) {
        ResultVO<T> vo = new ResultVO<>();
        vo.setCode(200);
        vo.setMsg("操作成功");
        vo.setData(data);
        return vo;
    }

    // 成功响应（无数据）
    public static <T> ResultVO<T> success() {
        return success(null);
    }

    // 失败响应
    public static <T> ResultVO<T> error(String msg) {
        ResultVO<T> vo = new ResultVO<>();
        vo.setCode(500);
        vo.setMsg(msg);
        return vo;
    }
}