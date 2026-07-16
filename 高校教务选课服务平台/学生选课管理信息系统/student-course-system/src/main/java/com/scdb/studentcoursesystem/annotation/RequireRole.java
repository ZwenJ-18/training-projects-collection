package com.scdb.studentcoursesystem.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义角色权限注解
 * 用于标记需要特定角色才能访问的方法，配合 RequireRoleAspect 切面使用
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {

    /**
     * 允许访问的角色列表
     * 支持: "STUDENT", "TEACHER", "ADMIN"
     */
    String[] value() default {};
}
