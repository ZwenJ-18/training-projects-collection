using System;
using System.Web;

namespace WebLab5
{
    public class Global : HttpApplication
    {
        protected void Application_Start(object sender, EventArgs e)
        {
        }

        protected void Session_Start(object sender, EventArgs e)
        {
        }

        protected void Application_BeginRequest(object sender, EventArgs e)
        {
        }

        protected void Application_AuthenticateRequest(object sender, EventArgs e)
        {
        }

        // 关键：把“一出错就跳登录页”删掉，否则必死循环
        protected void Application_Error(object sender, EventArgs e)
        {
            Exception ex = Server.GetLastError();
            Server.ClearError();
            // 不跳转！不跳转！不跳转！
            // Response.Redirect("Login.aspx?msg=系统异常，请重新登录");
        }

        protected void Session_End(object sender, EventArgs e)
        {
        }

        protected void Application_End(object sender, EventArgs e)
        {
        }
    }
}