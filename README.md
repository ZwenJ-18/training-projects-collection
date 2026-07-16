# 张文静 · 项目作品集

> 个人技术项目集合，主要使用 Java / Spring Boot 技术栈，涵盖后端服务、Web 应用与移动端实践。  
> 欢迎访问：[https://github.com/ZwenJ-18/training-projects-collection](https://github.com/ZwenJ-18/training-projects-collection)

---

## 项目概览

| 序号 | 项目名称 | 技术栈 | 说明 |
|------|----------|--------|------|
| 1 | 零售会员积分运营平台 | Spring Boot · Spring Security · MyBatis · MySQL · Thymeleaf · SpringDoc | 面向超市/零售的会员积分管理与营销系统 |
| 2 | 高校教务选课服务平台 | Spring Boot · Spring Security · MyBatis · MySQL · JWT · Caffeine · SpringDoc | 高校选课、成绩、统计管理 |
| 3 | 校园智慧请假审批系统 | ASP.NET WebForm · SQL Server · Bootstrap | 多级请假审批流程 |
| 4 | 企业智能客服助手 | Android · Java · OkHttp · RecyclerView | 基于 Android 的聊天客户端 |

---

## 仓库结构

```
training-projects-collection/
├── 企业智能客服助手/
├── 校园智慧请假审批系统/
├── 零售会员积分运营平台/
├── 高校教务选课服务平台/
├── .gitignore
├── .gitattributes
├── CHANGELOG.md
└── README.md
```

---

## 项目详情

### 1. 零售会员积分运营平台

- **功能模块**：用户注册登录、商品管理、积分获取、积分兑换、订单管理、角色权限控制
- **技术亮点**：
  - 基于 Spring Security 实现 ADMIN / USER 双角色权限控制
  - 使用 MyBatis 完成多表关联查询
  - 使用 `@Transactional` 保证订单与积分操作的原子性
  - 商品图片上传校验与本地存储映射
  - 集成 SpringDoc 提供在线 API 文档（`/swagger-ui.html`）
  - 提供 Docker Compose 一键启动方案
- **运行方式**：见 `零售会员积分运营平台/supermarket-points-management/超市积分管理系统/supermarket-points-management/README.md`

### 2. 高校教务选课服务平台

- **功能模块**：学生选课/退课、教师成绩录入、课程管理、统计报表
- **技术亮点**：
  - 7 张关系型数据表设计，使用外键约束
  - 统一响应体 `Result<T>` + 全局异常处理
  - 防止重复选课：业务层 + 数据库联合主键双重校验
  - JWT + AOP 实现基于角色的接口权限控制
  - Caffeine 本地缓存优化热点数据访问
  - 集成 SpringDoc 提供在线 API 文档（`/swagger-ui.html`）
  - 提供 Docker Compose 一键启动方案
- **运行方式**：见 `高校教务选课服务平台/学生选课管理信息系统/student-course-system/README.md`

### 3. 校园智慧请假审批系统

- **功能模块**：学生请假申请、辅导员/管理员审批、历史记录查询、班级管理
- **技术亮点**：
  - ASP.NET WebForm + SQL Server 的 B/S 架构实践
  - Session 登录状态与角色权限控制

### 4. 企业智能客服助手

- **功能模块**：消息发送、机器人回复、聊天列表展示
- **技术亮点**：
  - Android RecyclerView 列表渲染
  - OkHttp 异步网络请求与 JSON 解析
  - Handler 子线程更新 UI

---

## 技术栈

- 后端：Java、Spring Boot、Spring Security、MyBatis
- 数据库：MySQL、SQL Server
- 前端：HTML、CSS、JavaScript、Bootstrap、Thymeleaf
- 移动：Android、Java
- 工程化：Maven、Git、SpringDoc、Docker Compose
- 工具：IntelliJ IDEA、Navicat、Postman

---

## 使用说明

1. **修改现有项目**
   - 从 `main` 分支新建功能分支进行开发
   - 完成后通过 Pull Request 合并回 `main`，保持主分支稳定
   - 提交信息规范：`feat: / fix: / docs: / test: / chore:`

2. **添加新项目**
   - 在仓库根目录新建业务化中文目录（如 `项目名称`）
   - 放入项目完整代码（确保已删除子目录中的 `.git` 文件夹）
   - 按照上述提交规范提交并推送

---

## 关于我

- 肇庆学院 · 物联网工程专业 · 本科在读
- 熟悉 Java Web 开发，热爱后端技术
- 学习能力强，实习时间稳定，可每周到岗 5 天
- 联系方式：cyzjdm11226@outlook.com

---

> 本仓库为个人学习项目集合，代码均为本人开发完成，仅供参考学习。
