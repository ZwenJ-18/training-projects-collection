using System;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;
using System.Web.UI.WebControls;

namespace WebLab5
{
    public partial class Student : System.Web.UI.Page
    {
        string connStr = ConfigurationManager.ConnectionStrings["WebLabDB"].ConnectionString;

        // 前端已定义，这里不需要重复声明
        // protected Button btnChangePassword = new Button();

        protected void Page_Load(object sender, EventArgs e)
        {
            // 登录验证
            if (Session["UserName"] == null || Session["Role"].ToString() != "学生")
            {
                Response.Redirect("Login.aspx");
                return;
            }

            if (!IsPostBack)
            {
                LoadUserInfo();
                LoadLeaveHistory();
            }
        }

        // 加载用户个人信息和统计
        void LoadUserInfo()
        {
            string userName = Session["UserName"].ToString();

            using (SqlConnection conn = new SqlConnection(connStr))
            {
                conn.Open();

                // 获取用户基本信息
                string sqlUser = @"
                    SELECT u.UserName, c.ClassName 
                    FROM UserInfo u
                    LEFT JOIN Class c ON u.ClassId = c.ClassId
                    WHERE u.UserName = @UserName";

                SqlCommand cmdUser = new SqlCommand(sqlUser, conn);
                cmdUser.Parameters.AddWithValue("@UserName", userName);
                SqlDataReader dr = cmdUser.ExecuteReader();

                if (dr.Read())
                {
                    lblName.Text = dr["UserName"].ToString();
                    lblClass.Text = dr["ClassName"].ToString();
                    lblProfileName.Text = dr["UserName"].ToString();
                    lblProfileClass.Text = dr["ClassName"].ToString();
                }
                dr.Close();

                // 统计请假记录
                string sqlCount = @"
                    SELECT COUNT(*) 
                    FROM Leave l
                    JOIN UserInfo u ON l.UserId = u.UserId
                    WHERE u.UserName = @UserName";

                SqlCommand cmdCount = new SqlCommand(sqlCount, conn);
                cmdCount.Parameters.AddWithValue("@UserName", userName);
                int count = (int)cmdCount.ExecuteScalar();
                lblLeaveCount.Text = count.ToString();
            }
        }

        // 加载请假历史记录
        void LoadLeaveHistory()
        {
            string userName = Session["UserName"].ToString();

            using (SqlConnection conn = new SqlConnection(connStr))
            {
                conn.Open();
                string sql = @"
                    SELECT l.LeaveId AS 申请编号, 
                           l.StartDate AS 开始日期, 
                           l.EndDate AS 结束日期, 
                           l.Days AS 请假天数, 
                           l.Reason AS 请假原因, 
                           l.Status AS 当前状态
                    FROM Leave l
                    JOIN UserInfo u ON l.UserId = u.UserId
                    WHERE u.UserName = @UserName
                    ORDER BY l.LeaveId DESC";

                SqlDataAdapter da = new SqlDataAdapter(sql, conn);
                da.SelectCommand.Parameters.AddWithValue("@UserName", userName);
                DataTable dt = new DataTable();
                da.Fill(dt);

                gvHistory.DataSource = dt;
                gvHistory.DataBind();
                lblEmptyHistory.Visible = (dt.Rows.Count == 0);
            }
        }

        // 提交请假申请
        protected void btnSubmit_Click(object sender, EventArgs e)
        {
            // 基本验证
            if (string.IsNullOrEmpty(txtStart.Text) || string.IsNullOrEmpty(txtEnd.Text) || string.IsNullOrEmpty(txtReason.Text))
            {
                ClientScript.RegisterStartupScript(this.GetType(), "alert",
                    "Swal.fire('错误', '请填写完整的请假信息', 'error');", true);
                return;
            }

            DateTime startDate, endDate;
            if (!DateTime.TryParse(txtStart.Text, out startDate) || !DateTime.TryParse(txtEnd.Text, out endDate))
            {
                ClientScript.RegisterStartupScript(this.GetType(), "alert",
                    "Swal.fire('错误', '请输入正确的日期格式', 'error');", true);
                return;
            }

            if (endDate < startDate)
            {
                ClientScript.RegisterStartupScript(this.GetType(), "alert",
                    "Swal.fire('错误', '结束日期不能早于开始日期', 'error');", true);
                return;
            }

            // 计算请假天数（包含首尾）
            int days = (endDate - startDate).Days + 1;
            if (days <= 0)
            {
                ClientScript.RegisterStartupScript(this.GetType(), "alert",
                    "Swal.fire('错误', '请假天数必须大于0', 'error');", true);
                return;
            }

            string userName = Session["UserName"].ToString();

            using (SqlConnection conn = new SqlConnection(connStr))
            {
                conn.Open();

                // 获取用户ID
                string sqlUserId = "SELECT UserId FROM UserInfo WHERE UserName = @UserName";
                SqlCommand cmdUserId = new SqlCommand(sqlUserId, conn);
                cmdUserId.Parameters.AddWithValue("@UserName", userName);
                int userId = (int)cmdUserId.ExecuteScalar();

                // 插入请假记录
                string sqlInsert = @"
                    INSERT INTO Leave (UserId, StartDate, EndDate, Days, Reason, Status)
                    VALUES (@UserId, @StartDate, @EndDate, @Days, @Reason, N'待审批')";

                SqlCommand cmdInsert = new SqlCommand(sqlInsert, conn);
                cmdInsert.Parameters.AddWithValue("@UserId", userId);
                cmdInsert.Parameters.AddWithValue("@StartDate", startDate);
                cmdInsert.Parameters.AddWithValue("@EndDate", endDate);
                cmdInsert.Parameters.AddWithValue("@Days", days);
                cmdInsert.Parameters.AddWithValue("@Reason", txtReason.Text.Trim());
                cmdInsert.ExecuteNonQuery();
            }

            // 提交成功后刷新数据
            LoadUserInfo();
            LoadLeaveHistory();

            // 清空表单
            txtStart.Text = "";
            txtEnd.Text = "";
            txtReason.Text = "";

            // 成功提示
            ClientScript.RegisterStartupScript(this.GetType(), "alert",
                "Swal.fire('提交成功', '您的请假申请已提交，等待审批', 'success');", true);
        }

        // ✅ 修复：修改密码（和前端隐藏域完美匹配）
        protected void btnChangePassword_Click(object sender, EventArgs e)
        {
            try
            {
                // 从前端隐藏输入框获取密码
                string oldPwd = hdnOldPwd.Value;
                string newPwd = hdnNewPwd.Value;
                string userName = Session["UserName"].ToString();

                // 空值验证
                if (string.IsNullOrWhiteSpace(oldPwd) || string.IsNullOrWhiteSpace(newPwd))
                {
                    ClientScript.RegisterStartupScript(this.GetType(), "err",
                        "Swal.fire('修改失败', '请输入原密码和新密码', 'error');", true);
                    return;
                }

                using (SqlConnection conn = new SqlConnection(connStr))
                {
                    conn.Open();

                    // 1. 验证原密码
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

                    // 2. 更新数据库密码（真正生效）
                    string updateSql = "UPDATE UserInfo SET Password = @NewPwd WHERE UserName = @UserName";
                    SqlCommand updateCmd = new SqlCommand(updateSql, conn);
                    updateCmd.Parameters.AddWithValue("@NewPwd", newPwd);
                    updateCmd.Parameters.AddWithValue("@UserName", userName);
                    updateCmd.ExecuteNonQuery();
                }

                // 3. 成功后清空会话，强制重新登录
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