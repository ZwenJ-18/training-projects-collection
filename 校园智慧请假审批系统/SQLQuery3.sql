USE master;
GO

IF EXISTS (SELECT * FROM sys.databases WHERE name = 'WebLabDB')
BEGIN
    ALTER DATABASE WebLabDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE WebLabDB;
END
GO

CREATE DATABASE WebLabDB;
GO

USE WebLabDB;
GO

-- 班级表
CREATE TABLE Class(
    ClassId INT PRIMARY KEY IDENTITY(1,1),
    ClassName NVARCHAR(50) NOT NULL
);
GO

-- 用户表
CREATE TABLE UserInfo(
    UserId INT PRIMARY KEY IDENTITY(1,1),
    UserName NVARCHAR(50) NOT NULL,
    Password NVARCHAR(50) NOT NULL,
    Role NVARCHAR(20) NOT NULL,
    ClassId INT NULL,
    FOREIGN KEY(ClassId) REFERENCES Class(ClassId)
);
GO

-- 请假表：改回 Leave，和你代码一致
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

-- 领导账号：张文静 / 82843695
INSERT INTO UserInfo (UserName, Password, Role, ClassId)
VALUES (N'张文静', N'82843695', N'领导', NULL);
GO

-- 验证是否存在
SELECT * FROM UserInfo WHERE UserName=N'张文静';
GO