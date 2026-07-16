-- ============================================================
-- 高校智慧请假管理云平台 数据库完整初始化脚本
-- 高校智慧请假管理云平台
-- ============================================================

USE master;
GO

-- 1. 删除旧数据库（如果存在）
IF EXISTS (SELECT * FROM sys.databases WHERE name = 'LeaveDB')
BEGIN
    ALTER DATABASE LeaveDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE LeaveDB;
END
GO

-- 2. 创建数据库
CREATE DATABASE LeaveDB;
GO
USE LeaveDB;
GO

-- 3. 班级表
CREATE TABLE Class(
    ClassId INT PRIMARY KEY IDENTITY(1,1),
    ClassName NVARCHAR(50) NOT NULL
);
GO

-- 4. 用户表（学生、辅导员、领导三种角色）
CREATE TABLE UserInfo(
    UserId INT PRIMARY KEY IDENTITY(1,1),
    UserName NVARCHAR(50) NOT NULL,
    Password NVARCHAR(50) NOT NULL,
    Role NVARCHAR(20) NOT NULL,  -- 学生 / 辅导员 / 领导
    ClassId INT NULL,
    FOREIGN KEY(ClassId) REFERENCES Class(ClassId)
);
GO

-- 5. 请假表
CREATE TABLE Leave(
    LeaveId INT PRIMARY KEY IDENTITY(1,1),
    UserId INT NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    Days INT NOT NULL,
    Reason NVARCHAR(200),
    Status NVARCHAR(20) DEFAULT N'待审批',
    CheckUserId INT NULL,
    FOREIGN KEY(UserId) REFERENCES UserInfo(UserId),
    FOREIGN KEY(CheckUserId) REFERENCES UserInfo(UserId)
);
GO

-- ============================================================
-- 6. 插入班级数据
-- ============================================================
INSERT INTO Class (ClassName) VALUES
    (N'2024级计算机科学与技术1班'),
    (N'2024级计算机科学与技术2班'),
    (N'2024级软件工程1班'),
    (N'2024级软件工程2班'),
    (N'2024级人工智能1班');
GO

-- ============================================================
-- 7. 插入用户数据
-- ============================================================

-- 领导账号
INSERT INTO UserInfo (UserName, Password, Role, ClassId)
VALUES (N'张文静', N'82843695', N'领导', NULL);

-- 辅导员账号（每个班一个辅导员）
INSERT INTO UserInfo (UserName, Password, Role, ClassId) VALUES
    (N'刘老师', N'123456', N'辅导员', 1),
    (N'陈老师', N'123456', N'辅导员', 2),
    (N'李老师', N'123456', N'辅导员', 3),
    (N'王老师', N'123456', N'辅导员', 4),
    (N'赵老师', N'123456', N'辅导员', 5);

-- 学生账号
INSERT INTO UserInfo (UserName, Password, Role, ClassId) VALUES
    (N'张三', N'123456', N'学生', 1),
    (N'李四', N'123456', N'学生', 1),
    (N'王五', N'123456', N'学生', 2),
    (N'赵六', N'123456', N'学生', 2),
    (N'孙七', N'123456', N'学生', 3),
    (N'周八', N'123456', N'学生', 3),
    (N'吴九', N'123456', N'学生', 4),
    (N'郑十', N'123456', N'学生', 5);
GO

-- ============================================================
-- 8. 插入请假测试数据
-- ============================================================

-- 待审批的请假（≤3天，辅导员可审批）
INSERT INTO Leave (UserId, StartDate, EndDate, Days, Reason, Status, CheckUserId) VALUES
    (6, '2026-06-10', '2026-06-12', 3, N'家中有事需要处理', N'待审批', NULL),
    (7, '2026-06-15', '2026-06-16', 2, N'参加学术竞赛', N'待审批', NULL);

-- 待审批的请假（>3天，需院长审批）
INSERT INTO Leave (UserId, StartDate, EndDate, Days, Reason, Status, CheckUserId) VALUES
    (8, '2026-06-10', '2026-06-14', 5, N'因病需要住院治疗', N'待审批', NULL),
    (11, '2026-06-20', '2026-06-25', 6, N'参加校外实习项目', N'待审批', NULL);

-- 已审批通过的（辅导员已批）
INSERT INTO Leave (UserId, StartDate, EndDate, Days, Reason, Status, CheckUserId) VALUES
    (6, '2026-05-01', '2026-05-01', 1, N'参加表哥婚礼', N'辅导员通过', 2),
    (7, '2026-05-10', '2026-05-12', 3, N'发烧感冒', N'辅导员通过', 2);

-- 已拒绝的
INSERT INTO Leave (UserId, StartDate, EndDate, Days, Reason, Status, CheckUserId) VALUES
    (8, '2026-05-20', '2026-05-25', 6, N'旅游度假', N'拒绝', 3);

-- 院长已通过的
INSERT INTO Leave (UserId, StartDate, EndDate, Days, Reason, Status, CheckUserId) VALUES
    (11, '2026-04-01', '2026-04-05', 5, N'参加全国编程大赛', N'院长通过', 1);
GO

-- ============================================================
-- 9. 验证数据
-- ============================================================
PRINT '=== 班级表 ===';
SELECT * FROM Class;
PRINT '=== 用户表 ===';
SELECT UserId, UserName, Role, ClassId FROM UserInfo;
PRINT '=== 请假表 ===';
SELECT LeaveId, UserId, StartDate, EndDate, Days, Status FROM Leave;
GO

PRINT '数据库完整初始化完成！';
