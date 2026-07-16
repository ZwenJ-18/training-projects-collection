# 高校教务选课服务平台

> 面向高校教务场景的选课服务系统，支持学生选课/退课、教师成绩录入、课程管理、统计报表与角色权限控制。

---

## 技术栈

- 后端：Spring Boot 3.2、Spring Security、MyBatis、Maven
- 数据库：MySQL 8.0
- 接口风格：RESTful API
- 其他：Lombok、SpringDoc OpenAPI、Docker Compose

---

## 核心功能

| 模块 | 功能 |
|------|------|
| 学生模块 | 选课、退课、查询个人选课、成绩查看 |
| 教师模块 | 授课查询、成绩录入 |
| 课程模块 | 课程管理、院系专业管理 |
| 统计报表 | 学生-课程-教师关联报表 |
| 权限控制 | 业务层 + 数据库联合主键双重防止重复选课 |
| 接口文档 | 集成 SpringDoc OpenAPI，访问 `/swagger-ui.html` |

---

## 快速开始

### 方式一：本地启动

1. 安装 JDK 17、MySQL 8.0、Maven 3.9+
2. 创建数据库并执行 `init.sql`
   ```sql
   CREATE DATABASE SCDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. 修改 `application.yml` 中的数据库连接信息
4. 启动项目
   ```bash
   mvn spring-boot:run
   ```
5. 接口文档：http://localhost:8080/swagger-ui.html

### 方式二：Docker Compose 启动

```bash
docker-compose up -d
```

访问：http://localhost:8081

---

## 项目结构

```
src/main/java/com/scdb/studentcoursesystem
├── config          # 安全配置、跨域配置、MyBatis 配置、OpenAPI 配置
├── controller      # RESTful 控制器
├── service         # 业务接口
├── service/impl    # 业务实现
├── mapper          # 数据访问层
├── entity          # 实体与 VO
├── exception       # 全局异常处理
└── StudentCourseSystemApplication.java
```

---

## 数据库设计

核心表：
- `student`：学生信息
- `course`：课程信息
- `teacher`：教师信息
- `department`：院系
- `major`：专业
- `sct`：学生选课关系表（联合主键 sno + cno）
- `sys_user`：登录用户表

---

## 接口文档

启动后访问：http://localhost:8080/swagger-ui.html

---

## 联系方式

- 作者：张文静
- 邮箱：cyzjdm11226@outlook.com
