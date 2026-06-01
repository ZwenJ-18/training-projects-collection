<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Login.aspx.cs" Inherits="WebLab5.Login" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head runat="server">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>得比大学请假系统 - 登录</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <style>
        /* 全局设计系统 - 与其他页面完全统一 */
        :root {
            /* 背景与玻璃效果 */
            --bg-gradient: linear-gradient(135deg, #fafbfc 0%, #f1f3f5 100%);
            --glass-bg: rgba(255, 255, 255, 0.65);
            --glass-border: rgba(255, 255, 255, 0.25);
            --glass-shadow: 0 8px 32px rgba(0, 0, 0, 0.04);
            
            /* 颜色系统 - 中性灰为主 */
            --text-primary: #212529;
            --text-secondary: #6c757d;
            --text-tertiary: #adb5bd;
            --text-danger: #dc3545;
            --accent-primary: #495057;
            --accent-hover: #343a40;
            --accent-active: #212529;
            --border-color: #e9ecef;
            
            /* 圆角系统 */
            --radius-2xl: 24px;
            --radius-xl: 20px;
            --radius-lg: 16px;
            --radius-md: 12px;
            --radius-sm: 8px;
            
            /* 间距系统 */
            --spacing-2xs: 4px;
            --spacing-xs: 8px;
            --spacing-sm: 12px;
            --spacing-md: 16px;
            --spacing-lg: 24px;
            --spacing-xl: 32px;
            --spacing-2xl: 48px;
            
            /* 动画系统 */
            --transition-smooth: 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
            --transition-spring: 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: -apple-system, BlinkMacSystemFont, "SF Pro Display", "Segoe UI", Roboto, sans-serif;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
        }

        body {
            background: var(--bg-gradient);
            background-attachment: fixed;
            color: var(--text-primary);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: var(--spacing-lg);
        }

        /* 玻璃态卡片 - 与其他页面完全一致 */
        .glass {
            background: var(--glass-bg);
            backdrop-filter: blur(20px);
            -webkit-backdrop-filter: blur(20px);
            border: 1px solid var(--glass-border);
            box-shadow: var(--glass-shadow);
        }

        /* 登录卡片 */
        .login-card {
            width: 100%;
            max-width: 420px;
            border-radius: var(--radius-2xl);
            padding: var(--spacing-2xl);
            opacity: 0;
            transform: translateY(20px);
            animation: fadeSlideUp 0.5s ease forwards;
        }

        @keyframes fadeSlideUp {
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* 顶部图标区域 - 与其他页面card-cover设计语言统一 */
        .login-icon {
            width: 72px;
            height: 72px;
            border-radius: 50%;
            background: linear-gradient(135deg, #495057 0%, #343a40 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto var(--spacing-lg);
            box-shadow: 0 4px 16px rgba(73, 80, 87, 0.15);
        }

        .login-icon i {
            font-size: 32px;
            color: white;
        }

        .login-title {
            font-size: 28px;
            font-weight: 600;
            text-align: center;
            margin-bottom: var(--spacing-xl);
            color: var(--text-primary);
            letter-spacing: -0.5px;
        }

        /* 表单样式 - 与其他页面完全统一 */
        .form-group {
            margin-bottom: var(--spacing-md);
            position: relative;
        }

        .form-label {
            font-size: 13px;
            font-weight: 500;
            color: var(--text-secondary);
            margin-bottom: var(--spacing-xs);
            display: block;
            letter-spacing: 0.2px;
        }

        .form-control {
            width: 100%;
            border: 1px solid var(--border-color);
            border-radius: var(--radius-md);
            padding: 16px 18px 16px 44px;
            font-size: 15px;
            color: var(--text-primary);
            background: rgba(255, 255, 255, 0.5);
            transition: var(--transition-smooth);
            letter-spacing: 0.2px;
        }

        .form-control:focus {
            outline: none;
            border-color: var(--accent-primary);
            box-shadow: 0 0 0 3px rgba(73, 80, 87, 0.1);
            background: rgba(255, 255, 255, 0.7);
        }

        .form-control::placeholder {
            color: var(--text-tertiary);
        }

        /* 输入框左侧图标 */
        .form-icon {
            position: absolute;
            left: 16px;
            top: 50%;
            transform: translateY(-50%);
            font-size: 18px;
            color: var(--text-tertiary);
            transition: var(--transition-smooth);
        }

        .form-control:focus + .form-icon {
            color: var(--accent-primary);
        }

        /* 密码显示/隐藏按钮 */
        .password-toggle {
            position: absolute;
            right: 16px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            font-size: 18px;
            color: var(--text-tertiary);
            cursor: pointer;
            transition: var(--transition-smooth);
            padding: 4px;
        }

        .password-toggle:hover {
            color: var(--accent-primary);
        }

        /* 按钮样式 - 与其他页面完全统一 */
        .btn {
            border: none;
            border-radius: var(--radius-md);
            padding: 16px;
            font-size: 15px;
            font-weight: 500;
            cursor: pointer;
            transition: var(--transition-spring);
            width: 100%;
            letter-spacing: 0.3px;
        }

        .btn:active {
            transform: scale(0.96);
        }

        .btn-primary {
            background: var(--accent-primary);
            color: white;
        }

        .btn-primary:hover {
            background: var(--accent-hover);
        }

        .btn-primary:active {
            background: var(--accent-active);
        }

        /* 错误提示 */
        .error-text {
            color: var(--text-danger);
            font-size: 13px;
            text-align: center;
            margin-top: var(--spacing-md);
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 6px;
        }

        .error-text i {
            font-size: 16px;
        }

        /* 响应式优化 */
        @media (max-width: 576px) {
            .login-card {
                padding: var(--spacing-xl);
            }
            
            .login-icon {
                width: 64px;
                height: 64px;
            }
            
            .login-icon i {
                font-size: 28px;
            }
        }
    </style>
</head>
<body>
    <form runat="server">
        <div class="login-card glass">
            <!-- 顶部图标区域 -->
            <div class="login-icon">
                <i class="bi bi-calendar-check"></i>
            </div>
            
            <div class="login-title">
                得比大学请假管理系统
            </div>

            <!-- 账号输入框 -->
            <div class="form-group">
                <label class="form-label">账号</label>
                <asp:TextBox ID="txtUsername" runat="server" CssClass="form-control" placeholder="请输入您的账号" autocomplete="username"></asp:TextBox>
                <i class="bi bi-person form-icon"></i>
            </div>

            <!-- 密码输入框 -->
            <div class="form-group">
                <label class="form-label">密码</label>
                <asp:TextBox ID="txtPassword" runat="server" CssClass="form-control" TextMode="Password" placeholder="请输入您的密码" autocomplete="current-password"></asp:TextBox>
                <i class="bi bi-lock form-icon"></i>
                <button type="button" class="password-toggle" id="passwordToggle">
                    <i class="bi bi-eye-slash"></i>
                </button>
            </div>

            <asp:Button ID="btnLogin" runat="server" Text="登录" CssClass="btn btn-primary" OnClick="btnLogin_Click" />
            <asp:Label ID="lblError" runat="server" CssClass="error-text" Visible="false">
                <i class="bi bi-exclamation-circle"></i>
                <span id="errorMessage"></span>
            </asp:Label>
        </div>
    </form>

    <!-- 密码显示/隐藏功能 -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const passwordInput = document.getElementById('<%= txtPassword.ClientID %>');
            const passwordToggle = document.getElementById('passwordToggle');
            const toggleIcon = passwordToggle.querySelector('i');
            
            passwordToggle.addEventListener('click', function() {
                if (passwordInput.type === 'password') {
                    passwordInput.type = 'text';
                    toggleIcon.className = 'bi bi-eye';
                } else {
                    passwordInput.type = 'password';
                    toggleIcon.className = 'bi bi-eye-slash';
                }
            });
            
            // 自动聚焦账号输入框
            document.getElementById('<%= txtUsername.ClientID %>').focus();
            
            // 回车键提交登录
            document.addEventListener('keypress', function(e) {
                if (e.key === 'Enter') {
                    document.getElementById('<%= btnLogin.ClientID %>').click();
                }
            });
        });
    </script>
</body>
</html>