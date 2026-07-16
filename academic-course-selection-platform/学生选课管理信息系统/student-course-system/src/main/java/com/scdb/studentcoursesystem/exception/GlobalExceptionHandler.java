package com.scdb.studentcoursesystem.exception;

import com.scdb.studentcoursesystem.entity.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 统一处理 Controller 层抛出的异常，返回统一的 Result 响应体
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理未登录异常 (401)
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleUnauthorized(UnauthorizedException e) {
        return new Result<>(401, e.getMessage(), null);
    }

    /**
     * 处理无权限异常 (403)
     */
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Void> handleForbidden(ForbiddenException e) {
        return new Result<>(403, e.getMessage(), null);
    }

    /**
     * 处理参数校验异常 (400)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIllegalArgument(IllegalArgumentException e) {
        return new Result<>(400, "参数错误: " + e.getMessage(), null);
    }

    /**
     * 兜底处理所有未捕获异常 (500)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        return new Result<>(500, "服务器内部错误", null);
    }

    // ----- 自定义异常类 -----

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }

    public static class ForbiddenException extends RuntimeException {
        public ForbiddenException(String message) {
            super(message);
        }
    }
}
