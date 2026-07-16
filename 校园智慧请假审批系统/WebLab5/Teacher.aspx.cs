using System;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;
using System.Web.UI.WebControls;

namespace WebLab5
{
    public partial class Teacher : System.Web.UI.Page
    {
        string connStr = ConfigurationManager.ConnectionStrings["WebLabDB"].ConnectionString;

        protected void Page_Load(object sender, EventArgs e)
        {
            // 登录验证 + 角色验证
            if (Session["UserName"] == null || Session["Role"] == null || Session["Role"].ToString() != "辅导员")
            {
                Response.Redirect("Login.aspx");
                return;
            }

            // ✅ 修复：无论页面如何刷新，姓名永远显示
            lblTeacherName.Text = Session["UserName"].ToString();

            if (!IsPostBack)
            {
                LoadPendingApprovals();
                LoadApprovalHistory();
                LoadClassList();
                LoadClassStats();
            }
        }

        // 获取当前登录老师的UserId
        int GetCurrentTeacherId()
        {
            string teacherName = Session["UserName"].ToString();
            using (SqlConnection conn = new SqlConnection(connStr))
            {
                conn.Open();
                string sql = "SELECT UserId FROM UserInfo WHERE UserName = @UserName";
                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.AddWithValue("@UserName", teacherName);
                return (int)cmd.ExecuteScalar();
            }
        }

        // 加载待审批列表（只显示自己班级学生 ≤3天的待审批）
        void LoadPendingApprovals()
        {
            using (SqlConnection conn = new SqlConnection(connStr))
            {
                conn.Open();
                string sql = @"
                    SELECT l.LeaveId, u.UserName, l.StartDate, l.EndDate, l.Days, l.Reason, l.Status
                    FROM Leave l
                    JOIN UserInfo u ON l.UserId = u.UserId
                    WHERE l.Days <= 3 
                      AND l.Status = N'待审批'
                      AND u.ClassId = (SELECT ClassId FROM UserInfo WHERE UserName = @TeacherName)
                    ORDER BY l.LeaveId DESC";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.AddWithValue("@TeacherName", Session["UserName"].ToString());

                SqlDataAdapter da = new SqlDataAdapter(cmd);
                DataTable dt = new DataTable();
                da.Fill(dt);

                gvTeacher.DataSource = dt;
                gvTeacher.DataBind();
                lblEmptyPending.Visible = (dt.Rows.Count == 0);
            }
        }

        // 加载审批历史（只显示自己审批过的 + 自己班级）
        void LoadApprovalHistory()
        {
            int teacherId = GetCurrentTeacherId();

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
                    WHERE l.CheckUserId = @TeacherId 
                      AND l.Status IN (N'辅导员通过', N'拒绝')
                      AND u.ClassId = (SELECT ClassId FROM UserInfo WHERE UserName = @TeacherName)
                    ORDER BY l.LeaveId DESC";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.AddWithValue("@TeacherId", teacherId);
                cmd.Parameters.AddWithValue("@TeacherName", Session["UserName"].ToString());

                SqlDataAdapter da = new SqlDataAdapter(cmd);
                DataTable dt = new DataTable();
                da.Fill(dt);

                gvHistory.DataSource = dt;
                gvHistory.DataBind();
                lblEmptyHistory.Visible = (dt.Rows.Count == 0);
            }
        }

        // 加载班级列表（只显示自己负责的班级）
        void LoadClassList()
        {
            using (SqlConnection conn = new SqlConnection(connStr))
            {
                conn.Open();
                string sql = @"
                    SELECT c.ClassId AS 班级编号, 
                           c.ClassName AS 班级名称, 
                           COUNT(u.UserId) AS 学生人数
                    FROM Class c
                    LEFT JOIN UserInfo u ON c.ClassId = u.ClassId AND u.Role = N'学生'
                    WHERE c.ClassId = (SELECT ClassId FROM UserInfo WHERE UserName = @TeacherName)
                    GROUP BY c.ClassId, c.ClassName
                    ORDER BY c.ClassId";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.AddWithValue("@TeacherName", Session["UserName"].ToString());

                SqlDataAdapter da = new SqlDataAdapter(cmd);
                DataTable dt = new DataTable();
                da.Fill(dt);

                gvClass.DataSource = dt;
                gvClass.DataBind();
                lblEmptyClass.Visible = (dt.Rows.Count == 0);
            }
        }

        // 加载班级请假统计（只显示自己班级）
        void LoadClassStats()
        {
            using (SqlConnection conn = new SqlConnection(connStr))
            {
                conn.Open();
                string sql = @"
                    SELECT c.ClassName AS 班级名称,
                           COUNT(l.LeaveId) AS 总请假次数,
                           SUM(CASE WHEN l.Status = N'辅导员通过' THEN 1 ELSE 0 END) AS 通过次数,
                           SUM(CASE WHEN l.Status = N'拒绝' THEN 1 ELSE 0 END) AS 拒绝次数,
                           ISNULL(SUM(l.Days), 0) AS 总请假天数
                    FROM Class c
                    LEFT JOIN UserInfo u ON c.ClassId = u.ClassId AND u.Role = N'学生'
                    LEFT JOIN Leave l ON u.UserId = l.UserId
                    WHERE c.ClassId = (SELECT ClassId FROM UserInfo WHERE UserName = @TeacherName)
                    GROUP BY c.ClassId, c.ClassName
                    ORDER BY c.ClassId";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.AddWithValue("@TeacherName", Session["UserName"].ToString());

                SqlDataAdapter da = new SqlDataAdapter(cmd);
                DataTable dt = new DataTable();
                da.Fill(dt);

                gvStats.DataSource = dt;
                gvStats.DataBind();
                lblEmptyStats.Visible = (dt.Rows.Count == 0);
            }
        }

        // 审批操作（通过/拒绝，同时记录审批人ID）
        protected void btnApprove_Click(object sender, EventArgs e)
        {
            int leaveId = int.Parse(hdnLeaveId.Value);
            string status = hdnStatus.Value;
            int teacherId = GetCurrentTeacherId();

            using (SqlConnection conn = new SqlConnection(connStr))
            {
                conn.Open();
                string sql = @"
                    UPDATE Leave 
                    SET Status = @Status, CheckUserId = @CheckUserId 
                    WHERE LeaveId = @LeaveId";

                SqlCommand cmd = new SqlCommand(sql, conn);
                cmd.Parameters.AddWithValue("@Status", status);
                cmd.Parameters.AddWithValue("@CheckUserId", teacherId);
                cmd.Parameters.AddWithValue("@LeaveId", leaveId);
                cmd.ExecuteNonQuery();
            }

            // 刷新数据
            LoadPendingApprovals();
            LoadApprovalHistory();
            LoadClassStats();

            // 成功提示
            ClientScript.RegisterStartupScript(this.GetType(), "alert",
                $"Swal.fire('操作成功', '请假申请已" + status + "', 'success');", true);
        }

        // ✅ 新增：修改密码功能
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

                    // 验证原密码
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

                    // 更新新密码
                    string updateSql = "UPDATE UserInfo SET Password = @NewPwd WHERE UserName = @UserName";
                    SqlCommand updateCmd = new SqlCommand(updateSql, conn);
                    updateCmd.Parameters.AddWithValue("@NewPwd", newPwd);
                    updateCmd.Parameters.AddWithValue("@UserName", userName);
                    updateCmd.ExecuteNonQuery();
                }

                // 强制退出，使用新密码登录
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

        // 退出登录
        protected void lnkLogout_Click(object sender, EventArgs e)
        {
            Session.Clear();
            Session.Abandon();
            Response.Redirect("Login.aspx");
        }
    }
}