package com.supermarket.points.exception;

import com.supermarket.points.model.vo.ResultVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 捕获自定义业务异常
    @ExceptionHandler(BusinessException.class)
    public ResultVO<?> handleBusinessException(BusinessException e) {
        return ResultVO.error(e.getMsg());
    }

    // 捕获其他异常
    @ExceptionHandler(Exception.class)
    public ResultVO<?> handleException(Exception e) {
        e.printStackTrace();
        return ResultVO.error("系统异常，请联系管理员");
    }
}