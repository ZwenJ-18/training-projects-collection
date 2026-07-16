<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Error.aspx.cs" Inherits="WebLab5.Error" %>
<!DOCTYPE html>
<html>
<head runat="server">
    <title>出错了</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="alert alert-danger">
            <h4>系统异常</h4>
            <p><asp:Label ID="lblMsg" runat="server" /></p>
            <a href="Login.aspx" class="btn btn-primary">返回登录</a>
        </div>
    </div>
</body>
</html>