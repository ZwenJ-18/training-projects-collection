using System;
using System.Configuration;
using System.Data.SqlClient;
using System.Web;

namespace WebLab5
{
    public class Global : HttpApplication
    {
        // 标记是否已初始化数据库（避免重复执行）
        private static bool _dbInitialized = false;
        private static readonly object _lock = new object();

        protected void Application_Start(object sender, EventArgs e)
        {
            InitDatabase();
        }

        /// <summary>
        /// 自动创建数据库和表（不存在时创建），老师打开项目按F5即可运行
        /// </summary>
        void InitDatabase()
        {
            if (_dbInitialized) return;
            lock (_lock)
            {
                if (_dbInitialized) return;

                try
                {
                    string connStr = ConfigurationManager.ConnectionStrings["WebLabDB"].ConnectionString;
                    // 提取数据库名
                    var builder = new SqlConnectionStringBuilder(connStr);
                    string dbName = builder.InitialCatalog;
                    // 连接到 master 数据库
                    builder.InitialCatalog = "master";
                    string masterConn = builder.ConnectionString;

                    using (SqlConnection con = new SqlConnection(masterConn))
                    {
                        con.Open();
                        // 创建数据库
                        string createDb = string.Format(@"
                            IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = '{0}')
                            BEGIN
                                CREATE DATABASE [{0}];
                            END", dbName);
                        new SqlCommand(createDb, con).ExecuteNonQuery();
                    }

                    // 切换到目标数据库建表
                    using (SqlConnection con = new SqlConnection(connStr))
                    {
                        con.Open();

                        // 创建 Class 表
                        new SqlCommand(@"
                            IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Class')
                            CREATE TABLE Class(
                                ClassId INT PRIMARY KEY IDENTITY(1,1),
                                ClassName NVARCHAR(50) NOT NULL
                            );", con).ExecuteNonQuery();

                        // 创建 UserInfo 表
                        new SqlCommand(@"
                            IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'UserInfo')
                            CREATE TABLE UserInfo(
                                UserId INT PRIMARY KEY IDENTITY(1,1),
                                UserName NVARCHAR(50) NOT NULL,
                                Password NVARCHAR(50) NOT NULL,
                                Role NVARCHAR(20) NOT NULL,
                                ClassId INT NULL,
                                FOREIGN KEY(ClassId) REFERENCES Class(ClassId)
                            );", con).ExecuteNonQuery();

                        // 创建 Leave 表
                        new SqlCommand(@"
                            IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Leave')
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
                            );", con).ExecuteNonQuery();

                        // 插入默认班级（如果表为空）
                        var cmdCount = new SqlCommand("SELECT COUNT(*) FROM Class", con);
                        if ((int)cmdCount.ExecuteScalar() == 0)
                        {
                            string[] classes = { "2024级计算机科学与技术1班", "2024级计算机科学与技术2班",
                                                 "2024级软件工程1班", "2024级软件工程2班", "2024级人工智能1班" };
                            foreach (var c in classes)
                                new SqlCommand("INSERT INTO Class(ClassName) VALUES(N'" + c + "')", con).ExecuteNonQuery();
                        }

                        // 插入默认管理员（如果用户表为空）
                        cmdCount = new SqlCommand("SELECT COUNT(*) FROM UserInfo", con);
                        if ((int)cmdCount.ExecuteScalar() == 0)
                        {
                            new SqlCommand(@"INSERT INTO UserInfo(UserName,Password,Role,ClassId) VALUES(N'张文静',N'82843695',N'领导',NULL)", con).ExecuteNonQuery();
                            new SqlCommand(@"INSERT INTO UserInfo(UserName,Password,Role,ClassId) VALUES(N'刘老师',N'123456',N'辅导员',1)", con).ExecuteNonQuery();
                            new SqlCommand(@"INSERT INTO UserInfo(UserName,Password,Role,ClassId) VALUES(N'张三',N'123456',N'学生',1)", con).ExecuteNonQuery();
                            new SqlCommand(@"INSERT INTO UserInfo(UserName,Password,Role,ClassId) VALUES(N'李四',N'123456',N'学生',1)", con).ExecuteNonQuery();
                        }
                    }

                    _dbInitialized = true;
                }
                catch (Exception ex)
                {
                    // 数据库初始化失败时记录但不阻止应用启动
                    System.Diagnostics.Debug.WriteLine("数据库初始化失败: " + ex.Message);
                }
            }
        }

        protected void Session_Start(object sender, EventArgs e) { }

        protected void Application_BeginRequest(object sender, EventArgs e) { }

        protected void Application_AuthenticateRequest(object sender, EventArgs e) { }

        protected void Application_Error(object sender, EventArgs e)
        {
            Exception ex = Server.GetLastError();
            if (ex != null)
            {
                Exception inner = ex;
                while (inner.InnerException != null) inner = inner.InnerException;
                Session["LastError"] = inner.Message;
                Server.ClearError();
                Response.Redirect("~/Error.aspx?msg=" + HttpUtility.UrlEncode(inner.Message), false);
                System.Web.HttpContext.Current.ApplicationInstance.CompleteRequest();
            }
        }

        protected void Session_End(object sender, EventArgs e) { }
        protected void Application_End(object sender, EventArgs e) { }
    }
}
