# -*- coding: utf-8 -*-
"""直接用 pyodbc 初始化请假管理云平台数据库"""
import pyodbc

SERVER = r'(localdb)\MSSQLLocalDB'
CONN_STR = f'DRIVER={{ODBC Driver 17 for SQL Server}};SERVER={SERVER};DATABASE=master;Trusted_Connection=yes;'

master = pyodbc.connect(CONN_STR, autocommit=True)
cur = master.cursor()

# 直接创建（跳过删除已不存在的库）
cur.execute("CREATE DATABASE WebLabDB")
print("1. 数据库已创建")
cur.close()
master.close()

# 连接新数据库建表
DB_CONN = f'DRIVER={{ODBC Driver 17 for SQL Server}};SERVER={SERVER};DATABASE=WebLabDB;Trusted_Connection=yes;'
db = pyodbc.connect(DB_CONN, autocommit=True)
c = db.cursor()

c.execute("CREATE TABLE Class(ClassId INT PRIMARY KEY IDENTITY(1,1), ClassName NVARCHAR(50) NOT NULL)")
c.execute("CREATE TABLE UserInfo(UserId INT PRIMARY KEY IDENTITY(1,1), UserName NVARCHAR(50) NOT NULL, Password NVARCHAR(50) NOT NULL, Role NVARCHAR(20) NOT NULL, ClassId INT NULL, FOREIGN KEY(ClassId) REFERENCES Class(ClassId))")
c.execute("CREATE TABLE Leave(LeaveId INT PRIMARY KEY IDENTITY(1,1), UserId INT NOT NULL, StartDate DATE NOT NULL, EndDate DATE NOT NULL, Days INT NOT NULL, Reason NVARCHAR(200), Status NVARCHAR(20) DEFAULT N'待审批', CheckUserId INT NULL, FOREIGN KEY(UserId) REFERENCES UserInfo(UserId), FOREIGN KEY(CheckUserId) REFERENCES UserInfo(UserId))")
print("2. 三张表已创建")

# 班级
for cn in ['2024级计算机科学与技术1班','2024级计算机科学与技术2班','2024级软件工程1班','2024级软件工程2班','2024级人工智能1班']:
    c.execute("INSERT INTO Class(ClassName) VALUES(?)", cn)

# 用户
c.execute("INSERT INTO UserInfo(UserName,Password,Role,ClassId) VALUES(?,?,?,?)", '张文静','82843695','领导',None)
for tname,cid in [('刘老师',1),('陈老师',2),('李老师',3),('王老师',4),('赵老师',5)]:
    c.execute("INSERT INTO UserInfo(UserName,Password,Role,ClassId) VALUES(?,?,?,?)", tname,'123456','辅导员',cid)
for sname,cid in [('张三',1),('李四',1),('王五',2),('赵六',2),('孙七',3),('周八',3),('吴九',4),('郑十',5)]:
    c.execute("INSERT INTO UserInfo(UserName,Password,Role,ClassId) VALUES(?,?,?,?)", sname,'123456','学生',cid)

# 获取用户 ID 映射
ids = {}
for row in c.execute("SELECT UserId,UserName FROM UserInfo").fetchall():
    ids[row.UserName] = row.UserId

# 请假数据
c.execute("INSERT INTO Leave(UserId,StartDate,EndDate,Days,Reason,Status) VALUES(?,?,?,?,?,?)", ids['张三'],'2026-06-10','2026-06-12',3,'家中有事需要处理','待审批')
c.execute("INSERT INTO Leave(UserId,StartDate,EndDate,Days,Reason,Status) VALUES(?,?,?,?,?,?)", ids['李四'],'2026-06-15','2026-06-16',2,'参加学术竞赛','待审批')
c.execute("INSERT INTO Leave(UserId,StartDate,EndDate,Days,Reason,Status) VALUES(?,?,?,?,?,?)", ids['王五'],'2026-06-10','2026-06-14',5,'因病需要住院治疗','待审批')
c.execute("INSERT INTO Leave(UserId,StartDate,EndDate,Days,Reason,Status) VALUES(?,?,?,?,?,?)", ids['郑十'],'2026-06-20','2026-06-25',6,'参加校外实习项目','待审批')
c.execute("INSERT INTO Leave(UserId,StartDate,EndDate,Days,Reason,Status,CheckUserId) VALUES(?,?,?,?,?,?,?)", ids['张三'],'2026-05-01','2026-05-01',1,'参加表哥婚礼','辅导员通过',ids['刘老师'])
c.execute("INSERT INTO Leave(UserId,StartDate,EndDate,Days,Reason,Status,CheckUserId) VALUES(?,?,?,?,?,?,?)", ids['李四'],'2026-05-10','2026-05-12',3,'发烧感冒','辅导员通过',ids['刘老师'])
print("3. 测试数据已插入")

# 验证
print("\n=== 用户列表 ===")
for r in c.execute("SELECT UserName,Role FROM UserInfo").fetchall():
    print(f"  {r.UserName} ({r.Role})")

print(f"\n共计: {len(ids)} 个用户, 数据库初始化完成!")

c.close()
db.close()
