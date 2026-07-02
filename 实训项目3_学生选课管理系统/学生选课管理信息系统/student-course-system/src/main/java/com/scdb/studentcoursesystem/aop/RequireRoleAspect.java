package com.scdb.studentcoursesystem.aop;

import com.scdb.studentcoursesystem.annotation.RequireRole;
import com.scdb.studentcoursesystem.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

/**
 * RequireRole 注解的 AOP 切面
 * 拦截带 @RequireRole 的方法，校验 JWT 中的角色是否匹配
 */
@Aspect
@Component
public class RequireRoleAspect {

    private final JwtUtil jwtUtil;

    public RequireRoleAspect(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Around("@annotation(com.scdb.studentcoursesystem.annotation.RequireRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取当前 HTTP 请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new RuntimeException("无法获取请求上下文");
        }
        HttpServletRequest request = attributes.getRequest();

        // 提取 JWT token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("未登录或 token 无效 (401)");
        }
        String token = authHeader.substring(7);

        // 解析用户角色
        String userRole = jwtUtil.getRoleFromToken(token);

        // 获取注解中声明的允许角色
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequireRole requireRole = signature.getMethod().getAnnotation(RequireRole.class);
        String[] allowedRoles = requireRole.value();

        // 校验角色
        boolean hasPermission = Arrays.asList(allowedRoles).contains(userRole);
        if (!hasPermission) {
            throw new RuntimeException("无权限访问该接口，需要角色: " + Arrays.toString(allowedRoles) + " (403)");
        }

        // 通过校验，执行原方法
        return joinPoint.proceed();
    }
}
