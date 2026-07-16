using System;
using System.Data.SqlClient;
using System.Configuration;

namespace WebLab5
{
    public partial class Login : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (Request.QueryString["msg"] != null)
            {
                lblError.Text = Request.QueryString["msg"];
                lblError.Visible = true;
            }
        }

        protected void btnLogin_Click(object sender, EventArgs e)
        {
            string username = txtUsername.Text.Trim();
            string password = txtPassword.Text.Trim();

            if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(password))
            {
                lblError.Text = "账号和密码不能为空";
                lblError.Visible = true;
                return;
            }

            string connStr = ConfigurationManager.ConnectionStrings["WebLabDB"].ConnectionString;

            using (SqlConnection conn = new SqlConnection(connStr))
            {
                conn.Open();
                string sql = @"SELECT UserId, UserName, Role 
                               FROM UserInfo 
                               WHERE UserName=@username AND Password=@password";

                using (SqlCommand cmd = new SqlCommand(sql, conn))
                {
                    cmd.Parameters.AddWithValue("@username", username);
                    cmd.Parameters.AddWithValue("@password", password);

                    SqlDataReader dr = cmd.ExecuteReader();
                    if (dr.Read())
                    {
                        Session["UserId"] = dr["UserId"].ToString();
                        Session["UserName"] = dr["UserName"].ToString();
                        Session["Role"] = dr["Role"].ToString().Trim();

                        string role = Session["Role"].ToString();

                        if (role == "学生")
                        {
                            Response.Redirect("Student.aspx");
                        }
                        else if (role == "辅导员")
                        {
                            Response.Redirect("Teacher.aspx");
                        }
                        else if (role == "领导")
                        {
                            Response.Redirect("Admin.aspx");
                        }
                        else
                        {
                            lblError.Text = "用户角色无效，请联系管理员";
                            lblError.Visible = true;
                        }
                    }
                    else
                    {
                        lblError.Text = "账号或密码错误";
                        lblError.Visible = true;
                    }
                }
            }
        }
    }
}