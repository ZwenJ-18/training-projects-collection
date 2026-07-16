# 零售会员积分运营平台

> 面向超市/零售场景的会员积分管理与营销系统，支持会员积分获取、积分兑换、商品管理、订单处理与双角色权限控制。

---

## 技术栈

- 后端：Spring Boot 3.2、Spring Security、MyBatis、Maven
- 数据库：MySQL 8.0
- 前端模板：Thymeleaf
- 其他：PageHelper、Lombok、SpringDoc OpenAPI、Docker Compose

---

## 核心功能

| 模块 | 功能 |
|------|------|
| 会员模块 | 注册、登录、积分查询、积分明细 |
| 商品模块 | 商品列表、分页展示、库存预警、图片上传 |
| 积分模块 | 消费赚积分、积分兑换商品、积分流水记录 |
| 订单模块 | 下单、支付、订单状态流转 |
| 权限模块 | ADMIN / USER 双角色，基于 Spring Security URL 级权限控制 |
| 接口文档 | 集成 SpringDoc OpenAPI，访问 `/swagger-ui.html` |

---

## 快速开始

### 方式一：本地启动

1. 安装 JDK 17、MySQL 8.0、Maven 3.9+
2. 创建数据库并执行 `init.sql`
   ```sql
   CREATE DATABASE supermarket_points_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. 修改 `application.yml` 中的数据库连接信息
4. 启动项目
   ```bash
   mvn spring-boot:run
   ```
5. 浏览器访问：http://localhost:8080
6. 接口文档：http://localhost:8080/swagger-ui.html

### 方式二：Docker Compose 启动

```bash
docker-compose up -d
```

访问：http://localhost:8080

---

## 项目结构

```
src/main/java/com/supermarket/points
├── config          # 安全配置、MyBatis 配置、OpenAPI 配置
├── controller      # 控制器层
├── service         # 业务接口
├── service/impl    # 业务实现
├── mapper          # 数据访问层
├── model           # 实体、DTO、VO
├── exception       # 全局异常处理
└── PointsApplication.java
```

---

## 数据库设计

核心表：
- `sys_user`：用户与会员信息
- `commodity`：商品信息
- `order_info`：订单主表
- `points_record`：积分记录
- `point_log` / `sys_points_log`：积分变动日志

---

## 接口文档

启动后访问：http://localhost:8080/swagger-ui.html

---

## 联系方式

- 作者：张文静
- 邮箱：cyzjdm11226@outlook.com
