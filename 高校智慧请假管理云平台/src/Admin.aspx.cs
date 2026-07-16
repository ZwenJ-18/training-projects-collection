using System;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;
using System.Web.UI.WebControls;
using System.Web.Script.Serialization;

namespace WebLab5
{
    public partial class Admin : System.Web.UI.Page
    {
        string connStr = ConfigurationManager.ConnectionStrings["WebLabDB"].ConnectionString;

        protected void Page_Load(object sender, EventArgs e)
        {
            // ==============================================
            // 🔒 权限核心：只有【领导】能访问此页面
            // 辅导员 / 学生 / 未登录 → 全部跳回登录页
            // ==============================================
            if (Session["UserName"] == null || Session["Role"] == null || Session["Role"].ToString() != "领导")
            {
                Response.Redirect("Login.aspx?msg=无权限，请使用院长账号登录");
                return;
            }

            lblAdminName.Text = Session["UserName"].ToString();

            if (Request.QueryString["action"] == "getClasses")
            {
                bool forTeacher = Request.QueryString["type"] == "teacher";
                GetClassList(forTeacher);
                return;
            }

            // 每次页面加载都刷新待审批列表
            LoadAdminData();
            if (!IsPostBack)
            {
                LoadHistory();
                LoadStats();
                LoadAllUsers();
            }

            // PRG模式：重定向回来后显示操作结果
            if (Session["AdminMsg"] != null)
            {
                string msg = Session["AdminMsg"].ToString();
                Session.Remove("AdminMsg");
                ClientScript.RegisterStartupScript(this.GetType(), "adminAlert",
                    $"Swal.fire('操作成功', '请假申请已{msg}', 'success');", true);
            }
        }

        // 获取班级：老师=未被绑定的，学生=全部
        void GetClassList(bool onlyUnassignedForTeacher)
        {
            DataTable dt = new DataTable();
            using (SqlConnection conn = new SqlConnection(connStr))
            {
                conn.Open();
                string sql;

                if (onlyUnassignedForTeacher)
                {
                    sql = @"SELECT ClassId, ClassName FROM Class 
                            WHERE ClassId NOT IN (SELECT ISNULL(ClassId,0) FROM UserInfo WHERE Role=N'辅导员')
                            ORDER BY ClassName";
                }
                else
                {
                    sql = "SELECT ClassId, ClassName FROM Class ORDER BY ClassName";
                }

                SqlDataAdapter da = new SqlDataAdapter(sql, conn);
                da.Fill(dt);
            }

            var list = new System.Collections.Generic.List<dynamic>();
            foreach (DataRow row in dt.Rows)
            {
                list.Add(new
                {
                    ClassId = (int)row["ClassId"],
                    ClassName = row["ClassName"].ToString()
                });
            }

            Response.ContentType = "application/json";
            Response.Write(new JavaScriptSerializer().Serialize(list));
            Response.End();
        }

        void LoadAdminData()
        {
            using (SqlConnection conn = new SqlConnection(connStr))
            {
                conn.Open();
                string sql = @"SELECT l.LeaveId, u.UserName, l.StartDate, l.EndDate, l.Days, l.Reason, l.Status
                               FROM Leave l
                               JOIN UserInfo u ON l.UserId = u.UserId
                               WHERE l.Days > 3 AND l.Status = N'待审批'";

                SqlDataAdapter da = new SqlDataAdapter(sql, conn);
                DataTable dt = new DataTable();
                da.Fill(dt);

                gvAdmin.DataSource = dt;
                gvAdmin.DataBind();
                lblEmptyPending.Visible = (dt.Rows.Count == 0);
            }
        }

        void LoadHistory()
        {
            try
            {
                string currentUser = Session["UserName"].ToString();

                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();
                    string sql = @"
                        SELECT l.LeaveId AS 申请编号, 
                               u.UserName AS 学生姓名, 
                               l.StartDate AS 开始日期, 
                               l.EndDate AS 结束日期, 
                               l.Days AS 请假天数, 
                               l.Reason AS 请假原因, 
                               l.Status AS 审批结果
                        FROM Leave l
                        JOIN UserInfo u ON l.UserId = u.UserId
                        JOIN UserInfo c ON l.CheckUserId = c.UserId
                        WHERE c.UserName = @CurrentUser
                        ORDER BY l.LeaveId DESC";

                    SqlDataAdapter da = new SqlDataAdapter(sql, conn);
                    da.SelectCommand.Parameters.AddWithValue("@CurrentUser", currentUser);
                    DataTable dt = new DataTable();
                    da.Fill(dt);

                    gvHistory.DataSource = dt;
                    gvHistory.DataBind();
                    lblEmptyHistory.Visible = (dt.Rows.Count == 0);
                }
            }
            catch
            {
                lblEmptyHistory.Visible = true;
            }
        }

        void LoadStats()
        {
            try
            {
                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();

                    string sqlClass = @"
                        SELECT c.ClassName AS 班级名称, 
                               COUNT(l.LeaveId) AS 总请假次数,
                               SUM(CASE WHEN l.Status LIKE N'%通过%' THEN 1 ELSE 0 END) AS 通过次数,
                               SUM(CASE WHEN l.Status = N'拒绝' THEN 1 ELSE 0 END) AS 拒绝次数,
                               ISNULL(SUM(l.Days), 0) AS 总请假天数
                        FROM Class c
                        LEFT JOIN UserInfo s ON c.ClassId = s.ClassId AND s.Role = N'学生'
                        LEFT JOIN Leave l ON s.UserId = l.UserId
                        GROUP BY c.ClassId, c.ClassName
                        ORDER BY 总请假次数 DESC";
                    SqlDataAdapter daClass = new SqlDataAdapter(sqlClass, conn);
                    DataTable dtClass = new DataTable();
                    daClass.Fill(dtClass);
                    gvClass.DataSource = dtClass;
                    gvClass.DataBind();
                    lblEmptyClass.Visible = (dtClass.Rows.Count == 0);

                    string sqlUser = @"
                        SELECT u.UserName AS 学生姓名, 
                               c.ClassName AS 所属班级,
                               COUNT(l.LeaveId) AS 总请假次数,
                               ISNULL(SUM(l.Days), 0) AS 总请假天数
                        FROM UserInfo u
                        LEFT JOIN Class c ON u.ClassId = c.ClassId
                        LEFT JOIN Leave l ON u.UserId = l.UserId
                        WHERE u.Role = N'学生'
                        GROUP BY u.UserId, u.UserName, c.ClassName
                        ORDER BY 总请假次数 DESC";
                    SqlDataAdapter daUser = new SqlDataAdapter(sqlUser, conn);
                    DataTable dtUser = new DataTable();
                    daUser.Fill(dtUser);
                    gvUser.DataSource = dtUser;
                    gvUser.DataBind();
                    lblEmptyUser.Visible = (dtUser.Rows.Count == 0);
                }
            }
            catch
            {
                lblEmptyClass.Visible = true;
                lblEmptyUser.Visible = true;
            }
        }

        void LoadAllUsers()
        {
            try
            {
                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();
                    string sql = @"
                        SELECT u.UserId AS 用户编号, 
                               u.UserName AS 用户名, 
                               u.Role AS 用户角色,
                               ISNULL(c.ClassName, '无') AS 所属班级
                        FROM UserInfo u
                        LEFT JOIN Class c ON u.ClassId = c.ClassId
                        ORDER BY u.Role, u.UserName";

                    SqlDataAdapter da = new SqlDataAdapter(sql, conn);
                    DataTable dt = new DataTable();
                    da.Fill(dt);

                    gvUsers.DataSource = dt;
                    gvUsers.DataBind();
                    lblEmptyUsers.Visible = (dt.Rows.Count == 0);
                }
            }
            catch
            {
                lblEmptyUsers.Visible = true;
            }
        }

        protected void btnAdminApprove_Click(object sender, EventArgs e)
        {
            int leaveId = int.Parse(hdnAdminLeaveId.Value);
            string status = hdnAdminStatus.Value;

            using (SqlConnection conn = new SqlConnection(connStr))
            {
                conn.Open();
                string sql = @"UPDATE Leave 
                               SET Status = @Status, 
                                   CheckUserId = (SELECT UserId FROM UserInfo WHERE UserName = @CurrentUser)
                               WHERE LeaveId = @LeaveId";

                using (SqlCommand cmd = new SqlCommand(sql, conn))
                {
                    cmd.Parameters.AddWithValue("@Status", status);
                    cmd.Parameters.AddWithValue("@CurrentUser", Session["UserName"].ToString());
                    cmd.Parameters.AddWithValue("@LeaveId", leaveId);
                    cmd.ExecuteNonQuery();
                }
            }

            // PRG模式：重定向避免刷新重复审批
            Session["AdminMsg"] = status;
            Response.Redirect("Admin.aspx", false);
            System.Web.HttpContext.Current.ApplicationInstance.CompleteRequest();
        }

        // 添加用户（辅导员必须绑定班级）
        protected void btnAddUser_Click(object sender, EventArgs e)
        {
            try
            {
                string username = hdnAddUserName.Value.Trim();
                string pwd = hdnAddPwd.Value.Trim();
                string role = hdnAddRole.Value.Trim();
                int classId = int.Parse(hdnAddClassId.Value);

                if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(pwd) || string.IsNullOrEmpty(role))
                {
                    ShowMsg("error", "添加失败", "请填写完整信息");
                    return;
                }

                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();

                    SqlCommand checkCmd = new SqlCommand("SELECT COUNT(*) FROM UserInfo WHERE UserName=@UserName", conn);
                    checkCmd.Parameters.AddWithValue("@UserName", username);
                    int exists = (int)checkCmd.ExecuteScalar();
                    if (exists > 0)
                    {
                        ShowMsg("error", "添加失败", "用户名已存在");
                        return;
                    }

                    string sql = @"INSERT INTO UserInfo (UserName, Password, Role, ClassId) 
                                   VALUES (@UserName, @Password, @Role, @ClassId)";
                    SqlCommand cmd = new SqlCommand(sql, conn);
                    cmd.Parameters.AddWithValue("@UserName", username);
                    cmd.Parameters.AddWithValue("@Password", pwd);
                    cmd.Parameters.AddWithValue("@Role", role);
                    cmd.Parameters.AddWithValue("@ClassId", classId);
                    cmd.ExecuteNonQuery();
                }

                LoadAllUsers();
                ShowMsg("success", "添加成功", "新用户已创建");
            }
            catch (Exception ex)
            {
                ShowMsg("error", "添加失败", ex.Message);
            }
        }

        // 新建班级
        protected void btnAddClass_Click(object sender, EventArgs e)
        {
            try
            {
                string name = hdnNewClassName.Value.Trim();
                if (string.IsNullOrEmpty(name))
                {
                    ShowMsg("error", "创建失败", "班级名称不能为空");
                    return;
                }

                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();
                    SqlCommand checkCmd = new SqlCommand("SELECT COUNT(*) FROM Class WHERE ClassName=@Name", conn);
                    checkCmd.Parameters.AddWithValue("@Name", name);
                    int exists = (int)checkCmd.ExecuteScalar();
                    if (exists > 0)
                    {
                        ShowMsg("error", "创建失败", "班级已存在");
                        return;
                    }

                    SqlCommand cmd = new SqlCommand("INSERT INTO Class(ClassName) VALUES(@Name)", conn);
                    cmd.Parameters.AddWithValue("@Name", name);
                    cmd.ExecuteNonQuery();
                }

                ShowMsg("success", "创建成功", "班级已添加");
            }
            catch (Exception ex)
            {
                ShowMsg("error", "创建失败", ex.Message);
            }
        }

        protected void btnChangePassword_Click(object sender, EventArgs e)
        {
            try
            {
                string oldPwd = hdnOldPwd.Value;
                string newPwd = hdnNewPwd.Value;
                string userName = Session["UserName"].ToString();

                if (string.IsNullOrWhiteSpace(oldPwd) || string.IsNullOrWhiteSpace(newPwd))
                {
                    ClientScript.RegisterStartupScript(this.GetType(), "err",
                        "Swal.fire('修改失败', '请输入原密码和新密码', 'error');", true);
                    return;
                }

                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();

                    string checkSql = "SELECT COUNT(*) FROM UserInfo WHERE UserName = @UserName AND Password = @OldPwd";
                    SqlCommand checkCmd = new SqlCommand(checkSql, conn);
                    checkCmd.Parameters.AddWithValue("@UserName", userName);
                    checkCmd.Parameters.AddWithValue("@OldPwd", oldPwd);

                    int count = (int)checkCmd.ExecuteScalar();
                    if (count == 0)
                    {
                        ClientScript.RegisterStartupScript(this.GetType(), "err",
                            "Swal.fire('修改失败', '原密码输入错误', 'error');", true);
                        return;
                    }

                    string updateSql = "UPDATE UserInfo SET Password = @NewPwd WHERE UserName = @UserName";
                    SqlCommand updateCmd = new SqlCommand(updateSql, conn);
                    updateCmd.Parameters.AddWithValue("@NewPwd", newPwd);
                    updateCmd.Parameters.AddWithValue("@UserName", userName);
                    updateCmd.ExecuteNonQuery();
                }

                Session.Clear();
                Session.Abandon();

                ClientScript.RegisterStartupScript(this.GetType(), "success",
                    "Swal.fire('修改成功', '密码已更新，请使用新密码登录', 'success').then(function(){window.location.href='Login.aspx';});", true);
            }
            catch (Exception ex)
            {
                ClientScript.RegisterStartupScript(this.GetType(), "err",
                    "Swal.fire('修改失败', '系统错误：" + ex.Message + "', 'error');", true);
            }
        }

        protected void lnkLogout_Click(object sender, EventArgs e)
        {
            Session.Clear();
            Session.Abandon();
            Response.Redirect("Login.aspx");
        }

        void ShowMsg(string icon, string title, string text)
        {
            ClientScript.RegisterStartupScript(this.GetType(), "msg",
                $"Swal.fire('{title}','{text}','{icon}');", true);
        }
    }
}