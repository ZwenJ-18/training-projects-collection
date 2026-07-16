using System;

namespace WebLab5
{
    /// <summary>
    /// 全局错误页面 - 当系统发生未处理异常时显示
    /// 用户可从此页面返回登录页
    /// </summary>
    public partial class Error : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            // 从 Application_Error 或 Session 中获取错误信息
            if (Session["LastError"] != null)
            {
                lblMsg.Text = Session["LastError"].ToString();
                Session.Remove("LastError");
            }
            else if (Request.QueryString["msg"] != null)
            {
                lblMsg.Text = Request.QueryString["msg"];
            }
            else
            {
                lblMsg.Text = "系统发生未知错误，请稍后重试或联系管理员。";
            }
        }
    }
}
