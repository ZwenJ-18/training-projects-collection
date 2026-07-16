# 学生选课管理系统

支持高并发选课的业务系统，课程设计独立开发。

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-green)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-Educational-lightgrey)](./LICENSE)

## 📌 项目简介

前端原生 HTML + Bootstrap，后端 8 大模块（选课 / 退课 / 成绩 / 统计 / 权限等）完整 REST API。

### 核心功能

| 模块 | 功能 |
|------|------|
| 📚 **课程管理** | 课程 CRUD、热门课程 Caffeine 缓存加速 |
| ✏️ **选课/退课** | 高并发选课、容量控制、重复选课检测（唯一索引兜底） |
| 📝 **成绩管理** | 教师录入成绩、学生查询成绩、GPA 统计 |
| 👥 **权限控制** | 三角色（学生/教师/管理员）方法级 RBAC |
| 🔐 **认证授权** | JWT 无状态登录、自定义拦截器校验 token |
| 📊 **统一接口** | Result<T> 统一响应体 + 全局异常处理 |

## 🛠 技术栈

- **后端框架：** Spring Boot 2.7 + MyBatis
- **安全认证：** JWT 无状态认证 + Spring AOP 权限拦截
- **缓存：** Caffeine 进程内缓存（热门课程查询 200ms → 30ms）
- **数据库：** MySQL 8.0（7 张表，联合索引优化）
- **分页：** PageHelper 统一分页
- **构建工具：** Maven
- **API 文档：** springdoc-openapi
- **版本控制：** Git 三分支管理 + Pull Request 流程

## 🗄 数据库设计

```
学生表 (students)
  ├── student_id (PK)

课程表 (courses)
  ├── course_id (PK)

选课表 (course_selections)        ← 联合索引 (student_id, course_id)
  ├── selection_id (PK)
  ├── student_id (FK, INDEX)
  ├── course_id (FK, INDEX)
  └── selection_time

教师表 (teachers)
  ├── teacher_id (PK)

班级表 (classes)
  ├── class_id (PK)

成绩表 (grades)
  ├── grade_id (PK)
  ├── student_id (FK), course_id (FK)
  └── score

权限表 (permissions)
  ├── permission_id (PK)
  ├── user_id, role
```

## 📊 性能优化

| 优化项 | 优化前 | 优化后 | 提升 |
|--------|--------|--------|------|
| 选课列表查询 | ~1200ms | **~120ms** | **10×** |
| 热门课程查询 | ~200ms | **~30ms** | **6.7×** |

- **联合索引：** 对 (学号, 课程号) 建立复合索引，避免回表
- **Caffeine 本地缓存：** 热门课程信息缓存命中后无需查 DB，TTL 5 分钟自动刷新
- **压测数据：** 本机 JMeter 选课接口压测，无重复选课

## 🔒 安全设计

### JWT 无状态登录

```
用户登录 → 服务端签发 JWT → 客户端存储 token
     ↓
后续请求携带 token → 自定义拦截器校验
     ↓
校验通过 → 提取角色信息 → 放行 / 返回 401
```

### 方法级 RBAC

```java
@RequireRole("STUDENT")    // 自定义注解
@PostMapping("/select")
public Result<Void> selectCourse(...) { }

@RequireRole("ADMIN")
@DeleteMapping("/course/{id}")
public Result<Void> deleteCourse(...) { }
```

通过 `@RequireRole` 注解 + Spring AOP 切面，将鉴权逻辑从业务代码中解耦，减少 30% 重复鉴权代码。

### 统一异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    public Result<Void> handle401() { return Result.fail(401, "未登录"); }

    @ExceptionHandler(ForbiddenException.class)
    public Result<Void> handle403() { return Result.fail(403, "无权限"); }

    @ExceptionHandler(Exception.class)
    public Result<Void> handle500() { return Result.fail(500, "服务器错误"); }
}
```

## 🚀 运行说明

### 环境要求
- JDK 8+
- MySQL 8.0
- Maven 3.6+

### 启动步骤

```bash
# 1. 导入数据库
mysql -u root -p < src/main/resources/db/init.sql

# 2. 修改配置
# src/main/resources/application.yml
# 修改数据库连接信息

# 3. 启动项目
mvn spring-boot:run

# 4. 访问
# 前端：http://localhost:8080
# API 文档：http://localhost:8080/swagger-ui.html
```

## 📂 项目结构

```
学生选课管理信息系统/
├── src/main/java/com/example/
│   ├── controller/        # REST API 控制器
│   ├── service/           # 业务逻辑层
│   ├── mapper/            # MyBatis 数据访问层
│   ├── entity/            # 实体类
│   ├── config/            # 配置（CORS、JWT 拦截器）
│   ├── aop/               # @RequireRole 切面实现
│   ├── annotation/        # 自定义注解
│   ├── interceptor/       # JWT 拦截器
│   ├── dto/               # 数据传输对象
│   └── exception/         # 全局异常处理
├── src/main/resources/
│   ├── static/            # Bootstrap 静态资源
│   ├── templates/         # HTML 页面
│   └── application.yml
├── src/test/              # 单元测试
├── docs/                  # 数据库设计文档
└── pom.xml
```

## 📝 开发规范

- **Git 分支策略：** feature → dev → main，通过 Pull Request 合并
- **提交记录：** 累计 200+ 次提交，遵循 `feat: / fix: / docs:` 规范
- **代码评审：** PR 至少自查后合并

---

*本项目为课程实训作业（2025.05 - 2025.08），仅用于学习交流。*
