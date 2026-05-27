package com.scdb.studentcoursesystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 保留原有放行规则，不影响学生/管理员
                        .requestMatchers("/**/*.html", "/css/**", "/js/**", "/modules/**").permitAll()
                        .requestMatchers("/login", "/logout", "/getLoginUser").permitAll()
                        // 新增：确保教师接口能正常访问（不拦截）
                        .requestMatchers("/teacher/**").permitAll()
                        .anyRequest().permitAll()
                )
                // 保持原有配置，不改动登录/退出逻辑（避免影响其他角色）
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }
}