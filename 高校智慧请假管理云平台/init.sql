
USE master;
IF EXISTS (SELECT * FROM sys.databases WHERE name = 'WebLabDB')
BEGIN
    ALTER DATABASE WebLabDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE WebLabDB;
END
CREATE DATABASE WebLabDB;
USE WebLabDB;

CREATE TABLE Class(ClassId INT PRIMARY KEY IDENTITY(1,1), ClassName NVARCHAR(50) NOT NULL);
CREATE TABLE UserInfo(UserId INT PRIMARY KEY IDENTITY(1,1), UserName NVARCHAR(50) NOT NULL, Password NVARCHAR(50) NOT NULL, Role NVARCHAR(20) NOT NULL, ClassId INT NULL, FOREIGN KEY(ClassId) REFERENCES Class(ClassId));
CREATE TABLE Leave(LeaveId INT PRIMARY KEY IDENTITY(1,1), UserId INT NOT NULL, StartDate DATE NOT NULL, EndDate DATE NOT NULL, Days INT NOT NULL, Reason NVARCHAR(200), Status NVARCHAR(20) DEFAULT N'待审批', CheckUserId INT NULL, FOREIGN KEY(UserId) REFERENCES UserInfo(UserId), FOREIGN KEY(CheckUserId) REFERENCES UserInfo(UserId));

INSERT INTO Class(ClassName) VALUES(N'2024级计算机科学与技术1班'),(N'2024级计算机科学与技术2班'),(N'2024级软件工程1班'),(N'2024级软件工程2班'),(N'2024级人工智能1班');

INSERT INTO UserInfo(UserName,Password,Role,ClassId) VALUES(N'张文静',N'82843695',N'领导',NULL);
INSERT INTO UserInfo(UserName,Password,Role,ClassId) VALUES(N'刘老师',N'123456',N'辅导员',1),(N'陈老师',N'123456',N'辅导员',2),(N'李老师',N'123456',N'辅导员',3),(N'王老师',N'123456',N'辅导员',4),(N'赵老师',N'123456',N'辅导员',5);
INSERT INTO UserInfo(UserName,Password,Role,ClassId) VALUES(N'张三',N'123456',N'学生',1),(N'李四',N'123456',N'学生',1),(N'王五',N'123456',N'学生',2),(N'赵六',N'123456',N'学生',2),(N'孙七',N'123456',N'学生',3),(N'周八',N'123456',N'学生',3),(N'吴九',N'123456',N'学生',4),(N'郑十',N'123456',N'学生',5);

INSERT INTO Leave(UserId,StartDate,EndDate,Days,Reason,Status) VALUES(7,N'2026-06-10',N'2026-06-12',3,N'家中有事',N'待审批');
INSERT INTO Leave(UserId,StartDate,EndDate,Days,Reason,Status) VALUES(8,N'2026-06-15',N'2026-06-16',2,N'参加学术竞赛',N'待审批');
INSERT INTO Leave(UserId,StartDate,EndDate,Days,Reason,Status) VALUES(9,N'2026-06-10',N'2026-06-14',5,N'因病住院',N'待审批');
INSERT INTO Leave(UserId,StartDate,EndDate,Days,Reason,Status) VALUES(14,N'2026-06-20',N'2026-06-25',6,N'校外实习',N'待审批');
