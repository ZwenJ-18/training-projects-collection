# 超市积分管理系统

面向超市顾客与管理员的 Web 积分管理平台，课程设计独立开发。

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-green)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-Educational-lightgrey)](./LICENSE)

## 📌 项目简介

覆盖积分查询、消费累计、积分兑换三大核心场景。前端使用 Thymeleaf 模板渲染，后端完整 REST API 设计。

### 核心功能

| 模块 | 功能 |
|------|------|
| 🏪 **商品管理** | 商品信息 CRUD、库存管理、库存预警（< 50 自动标红） |
| 💳 **积分管理** | 积分查询、消费自动累计积分、积分流水记录 |
| 🎁 **积分兑换** | 积分兑换商品、实时库存扣减、防超卖（唯一索引） |
| 👥 **用户管理** | 用户/管理员双角色登录、接口级权限控制 |
| 📊 **系统监控** | @Scheduled 每日库存扫描、Swagger API 文档 |

## 🛠 技术栈

- **后端框架：** Spring Boot 2.7 + MyBatis
- **安全认证：** Spring Security + @PreAuthorize 注解
- **数据库：** MySQL 8.0（5 张核心表，B+ 树索引优化）
- **前端模板：** Thymeleaf
- **构建工具：** Maven
- **API 文档：** springdoc-openapi
- **测试：** JUnit 5（15 条单元测试）+ JMeter 压测

## 🗄 数据库设计

```
用户表 (users)
  ├── user_id (PK)
  ├── username, password, role

商品表 (products)
  ├── product_id (PK)
  ├── name, points_required, stock

积分流水表 (points_flow)          ← B+ 树索引优化
  ├── flow_id (PK)
  ├── user_id (FK, INDEX)
  ├── product_id (FK, INDEX)
  ├── points_change, type, created_at

兑换记录表 (exchange_records)
  ├── record_id (PK)
  ├── user_id (FK), product_id (FK)
  ├── exchange_time, points_spent

管理员表 (admins)
  ├── admin_id (PK)
  ├── username, password, role
```

## 📊 性能优化

| 优化项 | 优化前 | 优化后 |
|--------|--------|--------|
| 积分流水查询 | ~800ms | **~60ms** |
| 兑换接口压测 | - | **200 并发 / 30s，P95 280ms，0 超卖** |

- **B+ 树索引：** 对 user_id、product_id 高频查询字段建索引
- **防超卖：** 数据库唯一索引 + 事务保证
- **定时任务：** @Scheduled 每日自动扫描低库存商品

## 🔒 安全设计

- Spring Security 实现用户/管理员双角色认证
- @PreAuthorize 注解做方法级权限区分
- 登录密码 BCrypt 加密存储

## 🚀 运行说明

### 环境要求
- JDK 8+
- MySQL 8.0
- Maven 3.6+

### 启动步骤

```bash
# 1. 导入数据库
mysql -u root -p < 超市积分管理系统.sql

# 2. 修改配置文件
# src/main/resources/application.properties
# 修改数据库连接信息

# 3. 启动项目
mvn spring-boot:run

# 4. 访问
# 前台：http://localhost:8080
# API 文档：http://localhost:8080/swagger-ui.html
```

## 📂 项目结构

```
超市积分管理系统/
├── src/main/java/com/supermarket/
│   ├── controller/    # REST API 控制器
│   ├── service/       # 业务逻辑层
│   ├── mapper/        # MyBatis 数据访问层
│   ├── entity/        # 实体类
│   ├── config/        # Spring Security 配置
│   └── dto/           # 数据传输对象
├── src/main/resources/
│   ├── templates/     # Thymeleaf 模板
│   └── application.properties
├── src/test/          # JUnit 5 单元测试（15 条）
└── pom.xml
```

---

*本项目为课程实训作业（2024.09 - 2024.12），仅用于学习交流。*
