package com.scdb.studentcoursesystem.interceptor;

import com.scdb.studentcoursesystem.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 认证拦截器
 * 拦截所有请求，校验 token 有效性并将用户信息放入 request attribute
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行 OPTIONS 预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            return false;
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            response.setStatus(401);
            return false;
        }

        // 将用户信息放入 request，Controller 层可直接使用
        request.setAttribute("userId", jwtUtil.getUserIdFromToken(token));
        request.setAttribute("username", jwtUtil.getUsernameFromToken(token));
        request.setAttribute("role", jwtUtil.getRoleFromToken(token));

        return true;
    }
}
