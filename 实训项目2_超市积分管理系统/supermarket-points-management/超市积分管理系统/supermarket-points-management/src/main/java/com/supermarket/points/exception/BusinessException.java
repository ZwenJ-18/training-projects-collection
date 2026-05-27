package com.supermarket.points.exception;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException {
    private Integer code;
    private String msg;

    public BusinessException(String msg) {
        this.code = 500;
        this.msg = msg;
    }

    public BusinessException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}