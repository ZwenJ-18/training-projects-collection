# 高校智慧请假管理云平台

> ASP.NET WebForms + C# + SQL Server 高校请假审批系统，支持学生申请、辅导员审批、院长终审、数据统计与移动端适配。

## 项目亮点

- **多端适配**：基于 Bootstrap 5 响应式布局，兼容 PC、平板与手机端
- **玻璃态 UI**：统一设计系统，柔和渐变、圆角卡片、毛玻璃效果
- **角色权限**：学生 / 辅导员 / 院长（领导）三种角色，按请假天数自动分级审批
- **自动初始化**：`Global.asax` 启动时自动创建数据库、表结构与默认数据，按 F5 即可运行
- **数据统计**：班级维度、学生维度请假统计，助力学工决策

## 技术栈

| 层级 | 技术 |
|---|---|
| 前端 | ASP.NET WebForms、Bootstrap 5、Bootstrap Icons、SweetAlert2 |
| 后端 | C#、ASP.NET WebForms、ADO.NET |
| 数据库 | SQL Server (LocalDB) |
| 工具 | Visual Studio / Visual Studio Code、IIS Express |

## 功能模块

### 1. 学生端（Student.aspx）
- 提交请假申请（开始日期、结束日期、请假天数、请假原因）
- 查看个人请假历史与审批状态
- 移动端友好的表单与卡片布局

### 2. 辅导员端（Teacher.aspx）
- 审批 3 天及以内的学生请假申请
- 查看已审批历史
- 班级与学生维度请假统计

### 3. 院长/领导端（Admin.aspx）
- 审批 3 天以上的学生请假申请
- 查看全校审批历史
- 用户管理：添加学生、辅导员账户，新建班级
- 修改密码、数据统计看板

## 界面预览

项目运行截图位于 `运行截图/` 目录，包含 PC 端与手机端效果：

- 登录页、学生请假页、辅导员审批页、院长统计页
- 移动端自适应布局效果

> 可将截图替换为实际部署后的高清图片，展示效果更好。

## 快速启动

### 方式一：Visual Studio 一键运行
1. 使用 Visual Studio 打开 `WebLab5.slnx` 或直接打开项目根目录
2. 确认 `src/Web.config` 中连接字符串指向可用的 SQL Server / LocalDB 实例
3. 按 `F5` 运行，系统自动完成数据库初始化

### 方式二：SQL 脚本初始化
```bash
# 使用 SQL Server Management Studio 或 sqlcmd 执行
SQLQuery3.sql
```

### 默认账号

| 角色 | 账号 | 密码 |
|---|---|---|
| 领导（院长） | 张文静 | 82843695 |
| 辅导员 | 刘老师 | 123456 |
| 学生 | 张三 | 123456 |

## 项目结构

```
高校智慧请假管理云平台/
├── src/                             # ASP.NET WebForms 主项目
│   ├── Admin.aspx                   # 院长/领导审批台
│   ├── Teacher.aspx                # 辅导员审批台
│   ├── Student.aspx                # 学生请假申请
│   ├── Login.aspx                  # 统一登录页
│   ├── Global.asax.cs              # 应用启动与数据库自动初始化
│   ├── Web.config                  # 连接字符串与配置
│   └── Properties/AssemblyInfo.cs  # 程序集信息
├── SQLQuery3.sql                   # 完整数据库初始化脚本（含测试数据）
├── init.sql                        # 精简版初始化脚本
├── init_db.py                      # Python + pyodbc 数据库初始化辅助脚本
└── README.md                       # 本文件
```

## 注意事项

- 本项目使用 **明文密码** 存储默认账号，用于教学/演示环境，生产环境建议改造为加盐哈希（如 BCrypt）
- 部署到 IIS 时，请确保应用池启用 .NET Framework 4.x，并配置 SQL Server 连接字符串
- 移动端测试建议使用 Chrome DevTools 的设备模拟器，或扫描二维码访问部署后的地址

## 更新日志

- 2026-07：统一页面品牌名称为「高校智慧请假管理云平台」，整理项目结构，补充运行文档
