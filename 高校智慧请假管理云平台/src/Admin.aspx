<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Admin.aspx.cs" Inherits="WebLab5.Admin" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head runat="server">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>院长审批 & 统计 | 学生请假系统</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <style>
        /* ==============================================
           瑞士水疗风设计系统 - 严格遵循8px网格
           ============================================== */
        :root {
            /* 中性灰阶调色板 - 无任何彩色干扰 */
            --bg-primary: #f8f9fa;
            --bg-gradient: linear-gradient(135deg, #f0f4ff 0%, #f8f6f3 100%);
            --glass-bg: rgba(255, 255, 255, 0.72);
            --glass-border: rgba(255, 255, 255, 0.4);
            --glass-shadow: 0 2px 16px rgba(0, 0, 0, 0.04);
            --glass-shadow-hover: 0 8px 32px rgba(0, 0, 0, 0.06);
            
            --text-primary: #212529;
            --text-secondary: #6c757d;
            --text-tertiary: #adb5bd;
            
            --accent-primary: #4F6EF7;
            --accent-hover: #3D56E0;
            --accent-active: #2C44CC;
            --accent-light: #EEF1FE;
            --accent-success: #10B981;
            --accent-danger: #dc3545;
            --accent-warning: #ffc107;
            
            --border-color: #e9ecef;
            --divider-color: #f1f3f5;
            
            /* 圆角系统 */
            --radius-2xl: 24px;
            --radius-xl: 20px;
            --radius-lg: 16px;
            --radius-md: 12px;
            --radius-sm: 8px;
            
            /* 间距系统 - 8px倍数 */
            --spacing-2xs: 4px;
            --spacing-xs: 8px;
            --spacing-sm: 12px;
            --spacing-md: 16px;
            --spacing-lg: 24px;
            --spacing-xl: 32px;
            --spacing-2xl: 48px;
            
            /* 动画系统 - 物理级真实感 */
            --transition-smooth: 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
            --transition-spring: 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: -apple-system, BlinkMacSystemFont, "SF Pro Display", "Inter", "Segoe UI", Roboto, sans-serif;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
        }

        body {
            background: var(--bg-gradient);
            background-attachment: fixed;
            color: var(--text-primary);
            min-height: 100vh;
            padding-top: 72px;
            line-height: 1.6;
        }

        /* 毛玻璃材质 - 超通透磨砂效果 */
        .glass {
            background: var(--glass-bg);
            backdrop-filter: blur(24px);
            -webkit-backdrop-filter: blur(24px);
            border: 1px solid var(--glass-border);
            box-shadow: var(--glass-shadow);
        }

        /* 状态标签样式 */
        .status {
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            letter-spacing: 0.3px;
        }

        .status-pending {
            background: rgba(108, 117, 125, 0.1);
            color: var(--text-secondary);
        }

        .status-approved {
            background: rgba(40, 167, 69, 0.1);
            color: var(--accent-success);
        }

        .status-rejected {
            background: rgba(220, 53, 69, 0.1);
            color: var(--accent-danger);
        }

        /* ==============================================
           顶部导航 - 固定吸顶
           ============================================== */
        .top-nav {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            height: 64px;
            z-index: 9999;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 var(--spacing-xl);
            border-bottom: 1px solid var(--divider-color);
            background: rgba(255, 255, 255, 0.8);
            backdrop-filter: blur(20px);
            -webkit-backdrop-filter: blur(20px);
        }

        .nav-links {
            display: flex;
            gap: var(--spacing-lg);
        }

        .nav-link {
            color: var(--text-secondary);
            text-decoration: none;
            font-size: 14px;
            font-weight: 500;
            transition: var(--transition-smooth);
            display: flex;
            align-items: center;
            gap: 6px;
            padding: var(--spacing-xs) var(--spacing-sm);
            border-radius: var(--radius-sm);
        }

        .nav-link:hover {
            color: var(--text-primary);
            background: rgba(0, 0, 0, 0.03);
        }

        .nav-link.active {
            color: var(--text-primary);
            font-weight: 600;
        }

        /* ==============================================
           左侧侧边栏 - 固定不滚动
           ============================================== */
        .sidebar {
            position: fixed;
            top: 80px;
            left: var(--spacing-lg);
            width: calc(20% - var(--spacing-lg));
            height: calc(100vh - 96px);
            border-radius: var(--radius-2xl);
            padding: var(--spacing-xl);
            display: flex;
            flex-direction: column;
            gap: var(--spacing-md);
            z-index: 100;
        }

        .sidebar-title {
            font-size: 16px;
            font-weight: 600;
            margin-bottom: var(--spacing-sm);
            color: var(--text-primary);
            letter-spacing: 0.3px;
        }

        .sidebar-menu {
            display: flex;
            flex-direction: column;
            gap: var(--spacing-xs);
        }

        .sidebar-item {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 14px 18px;
            border-radius: var(--radius-lg);
            color: var(--text-secondary);
            text-decoration: none;
            font-size: 15px;
            font-weight: 500;
            transition: var(--transition-smooth);
            cursor: pointer;
        }

        .sidebar-item:hover {
            background: rgba(0, 0, 0, 0.03);
            color: var(--text-primary);
        }

        .sidebar-item.active {
            background: var(--accent-light);
            color: var(--accent-primary);
            font-weight: 600;
        }

        .sidebar-item i {
            font-size: 18px;
            width: 20px;
            text-align: center;
        }

        /* ==============================================
           主内容区 - 右侧80%
           ============================================== */
        .main-content {
            margin-left: 20%;
            padding: 0 var(--spacing-xl) var(--spacing-2xl);
        }

        .page-header {
            margin-bottom: var(--spacing-xl);
        }

        /* 问候语：字号比标题更大，突出显示 */
        .greeting {
            font-size: 32px;
            font-weight: 600;
            color: var(--text-primary);
            margin-bottom: var(--spacing-xs);
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .greeting a.user-name-link {
            color: var(--accent-primary);
            font-weight: 700;
            text-decoration: none;
            transition: var(--transition-smooth);
            cursor: pointer;
        }
        .greeting a.user-name-link:hover {
            color: var(--accent-hover);
            text-decoration: underline;
        }

        .page-title {
            font-size: 24px;
            font-weight: 600;
            color: var(--text-primary);
            letter-spacing: -0.3px;
            margin-bottom: var(--spacing-xs);
        }

        .page-subtitle {
            font-size: 15px;
            color: var(--text-secondary);
        }

        /* 内容区域切换 */
        .content-section {
            display: none;
        }

        .content-section.active {
            display: block;
            animation: fadeSlideUp 0.5s ease forwards;
        }

        @keyframes fadeSlideUp {
            from {
                opacity: 0;
                transform: translateY(16px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* 毛玻璃卡片 - 核心视觉元素 */
        .card {
            border-radius: var(--radius-2xl);
            padding: var(--spacing-xl);
            transition: var(--transition-smooth);
            overflow: hidden;
            margin-bottom: var(--spacing-lg);
        }

        .card:hover {
            transform: translateY(-12px);
            box-shadow: var(--glass-shadow-hover);
        }

        .card-cover {
            width: 100%;
            height: 120px;
            border-radius: var(--radius-lg);
            background: linear-gradient(135deg, #EEF1FE 0%, #E0E4FD 100%);
            margin-bottom: var(--spacing-lg);
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .card-cover i {
            font-size: 48px;
            color: var(--accent-primary);
            opacity: 0.6;
        }

        .card-title {
            font-size: 18px;
            font-weight: 600;
            margin-bottom: var(--spacing-xs);
            color: var(--text-primary);
            letter-spacing: 0.2px;
        }

        .card-description {
            font-size: 14px;
            color: var(--text-secondary);
            margin-bottom: var(--spacing-lg);
            line-height: 1.6;
        }

        /* 表格样式 */
        .table {
            width: 100%;
            border-collapse: collapse;
        }

        .table th {
            text-align: left;
            font-size: 12px;
            font-weight: 600;
            color: var(--text-secondary);
            padding: 12px 14px;
            border-bottom: 2px solid var(--divider-color);
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .table td {
            padding: 16px 14px;
            font-size: 14px;
            border-bottom: 1px solid var(--divider-color);
            color: var(--text-primary);
        }

        .table tr:last-child td {
            border-bottom: none;
        }

        /* 操作按钮样式 */
        .btn-action {
            padding: 8px 16px;
            border-radius: var(--radius-sm);
            font-size: 13px;
            font-weight: 500;
            border: none;
            transition: var(--transition-spring);
            cursor: pointer;
            letter-spacing: 0.2px;
        }

        .btn-action:active {
            transform: scale(0.94);
        }

        .btn-approve {
            background: var(--accent-success);
            color: white;
            margin-right: 8px;
        }

        .btn-reject {
            background: var(--accent-danger);
            color: white;
        }

        .btn-action:hover {
            opacity: 0.9;
        }

        .empty-state {
            color: var(--text-tertiary);
            font-size: 14px;
            text-align: center;
            padding: 60px 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 12px;
        }

        .empty-state i {
            font-size: 48px;
            color: var(--text-tertiary);
        }

        /* 统计卡片网格 */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: var(--spacing-lg);
        }

        /* ==============================================
           底部区域
           ============================================== */
        .footer {
            margin-top: var(--spacing-2xl);
            padding: var(--spacing-2xl);
            border-radius: var(--radius-2xl);
            text-align: center;
        }

        .footer-content {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: var(--spacing-xl);
            margin-bottom: var(--spacing-xl);
        }

        .footer-section {
            text-align: left;
        }

        .footer-title {
            font-size: 16px;
            font-weight: 600;
            color: var(--text-primary);
            margin-bottom: var(--spacing-md);
        }

        .footer-text {
            font-size: 14px;
            color: var(--text-secondary);
            line-height: 1.8;
            margin-bottom: var(--spacing-sm);
        }

        .footer-contact {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-bottom: var(--spacing-sm);
            color: var(--text-secondary);
            font-size: 14px;
        }

        .footer-contact i {
            font-size: 16px;
            color: var(--text-tertiary);
        }

        .qr-code {
            width: 120px;
            height: 120px;
            border-radius: var(--radius-md);
            background: white;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto;
            border: 1px solid var(--border-color);
            overflow: hidden; /* 确保图片圆角生效 */
        }

        .qr-code i {
            font-size: 48px;
            color: var(--text-tertiary);
        }

        .footer-bottom {
            padding-top: var(--spacing-lg);
            border-top: 1px solid var(--divider-color);
            color: var(--text-tertiary);
            font-size: 13px;
        }

        /* ==============================================
           移动端：汉堡菜单 & 抽屉侧边栏
           ============================================== */
        .hamburger-btn {
            display: none;
            background: none;
            border: none;
            font-size: 24px;
            color: var(--text-primary);
            cursor: pointer;
            width: 40px; height: 40px;
            border-radius: 50%;
            align-items: center;
            justify-content: center;
            transition: var(--transition-smooth);
        }
        .hamburger-btn:active { background: var(--accent-light); transform: scale(0.92); }

        .sidebar-overlay {
            display: none;
            position: fixed;
            top: 0; left: 0;
            width: 100%; height: 100%;
            background: rgba(0, 0, 0, 0.4);
            z-index: 199;
            opacity: 0;
            transition: opacity 0.25s ease;
        }
        .sidebar-overlay.show { display: block; opacity: 1; }

        .drawer-header {
            display: none;
            padding: 30px 20px 20px; text-align: center;
            border-bottom: 1px solid var(--divider-color); margin-bottom: 8px;
        }
        .drawer-avatar {
            width: 56px; height: 56px; border-radius: 50%;
            background: linear-gradient(135deg, var(--accent-primary), #7B93FA);
            color: #fff; font-size: 24px; font-weight: 700;
            display: flex; align-items: center; justify-content: center;
            margin: 0 auto 10px;
        }
        .drawer-name { font-size: 16px; font-weight: 600; color: var(--text-primary); }
        .drawer-role {
            font-size: 12px; color: var(--accent-primary);
            background: var(--accent-light); padding: 2px 12px;
            border-radius: 20px; display: inline-block; margin-top: 4px;
        }

        .table-responsive {
            width: 100%;
            overflow-x: auto;
            -webkit-overflow-scrolling: touch;
        }

        /* ==============================================
           响应式设计 - 完美适配所有设备
           ============================================== */
        @media (max-width: 992px) {
            .hamburger-btn { display: flex; }
            .drawer-header { display: block; }

            .top-nav { height: 52px; padding: 0 12px; background: rgba(255,255,255,0.92); border-bottom: 1px solid var(--border-color); }
            .nav-links { position: absolute; left: 50%; transform: translateX(-50%); gap: 4px; }
            .nav-link { font-size: 14px; font-weight: 600; padding: 6px 10px; color: var(--text-primary); }
            .nav-link span { display: inline !important; }
            .nav-link i { display: none; }

            .sidebar {
                position: fixed; top: 0; left: 0; height: 100vh;
                width: 280px !important; z-index: 200;
                transform: translateX(-100%);
                transition: transform 0.28s cubic-bezier(0.32, 0.72, 0, 1);
                border-radius: 0 20px 20px 0; padding-top: 0;
            }
            .sidebar.open { transform: translateX(0); box-shadow: 8px 0 40px rgba(0,0,0,0.12); }

            .main-content { margin-left: 0; padding: 0 14px 80px; }
            body { padding-top: 56px; }
            .stats-grid { grid-template-columns: 1fr; }

            .card { padding: 20px; border-radius: 16px; margin-bottom: 0; }
            .card:hover { transform: none; box-shadow: var(--glass-shadow); }
            .card-cover { height: 90px; border-radius: 12px; margin-bottom: 16px; }
            .card-cover i { font-size: 36px; }
            .card-title { font-size: 16px; margin-bottom: 4px; }
            .card-description { font-size: 13px; margin-bottom: 16px; }

            .table, .table tbody, .table tr, .table td { display: block; }
            .table thead { display: none; }
            .table tr { background: #fff; border-radius: 14px; padding: 14px 16px; margin-bottom: 10px; box-shadow: 0 1px 4px rgba(0,0,0,0.04); border: 1px solid var(--border-color); }
            .table td { padding: 7px 0; border: none; font-size: 14px; display: flex; justify-content: space-between; align-items: center; }
            .table td:before { content: attr(data-label); font-size: 12px; font-weight: 600; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.3px; min-width: 80px; }
            .table .status { display: inline-block; }
            .btn-action { width: auto; min-width: 64px; }

            .form-control { padding: 14px 16px; font-size: 16px; border-radius: 12px; }
            .btn { height: 48px; font-size: 16px; border-radius: 12px; }

            .main-content { padding-bottom: calc(80px + env(safe-area-inset-bottom, 16px)); }

            .footer { padding: 24px 20px; border-radius: 16px; }
            .footer-content { grid-template-columns: 1fr; gap: 20px; text-align: center; }
            .footer-section { text-align: center; }
            .footer-contact { justify-content: center; }
        }
    </style>
</head>
<body>
    <form runat="server">
        <asp:HiddenField ID="hdnAdminLeaveId" runat="server" />
        <asp:HiddenField ID="hdnAdminStatus" runat="server" />
        <asp:Button ID="btnAdminApprove" runat="server" OnClick="btnAdminApprove_Click" style="display:none;" />

        <!-- ✅ 修改密码专用控件 -->
        <asp:HiddenField ID="hdnOldPwd" runat="server" />
        <asp:HiddenField ID="hdnNewPwd" runat="server" />
        <asp:Button ID="btnChangePassword" runat="server" OnClick="btnChangePassword_Click" style="display:none;" />

        <!-- ✅ 添加用户专用控件 -->
        <asp:HiddenField ID="hdnAddUserName" runat="server" />
        <asp:HiddenField ID="hdnAddPwd" runat="server" />
        <asp:HiddenField ID="hdnAddRole" runat="server" />
        <asp:HiddenField ID="hdnAddClassId" runat="server" />
        <asp:Button ID="btnAddUser" runat="server" OnClick="btnAddUser_Click" style="display:none;" />

        <!-- ✅ 新建班级专用控件 -->
        <asp:HiddenField ID="hdnNewClassName" runat="server" />
        <asp:Button ID="btnAddClass" runat="server" OnClick="btnAddClass_Click" style="display:none;" />

        <!-- 移动端遮罩层 -->
        <div class="sidebar-overlay" id="sidebarOverlay"></div>

        <!-- 顶部导航 -->
        <div class="top-nav">
            <button type="button" class="hamburger-btn" id="hamburgerBtn" aria-label="菜单">
                <i class="bi bi-list"></i>
            </button>
            <div class="nav-links">
                <a href="Admin.aspx" class="nav-link active">
                    <i class="bi bi-shield-check"></i>
                    <span>院长审批</span>
                </a>
                <a href="Login.aspx" class="nav-link">
                    <i class="bi bi-house"></i>
                    <span>首页</span>
                </a>
                <a href="Admin.aspx" class="nav-link">
                    <i class="bi bi-question-circle"></i>
                    <span>帮助</span>
                </a>

            </div>
            <asp:LinkButton ID="lnkLogout" runat="server" OnClick="lnkLogout_Click" class="nav-link">
                <i class="bi bi-box-arrow-right"></i>
                <span>退出登录</span>
            </asp:LinkButton>
        </div>

        <!-- 左侧侧边栏 -->
        <div class="sidebar glass">
            <div class="drawer-header">
                <div class="drawer-avatar" id="drawerAvatar">院</div>
                <div class="drawer-name" id="drawerName">院长</div>
                <div class="drawer-role" id="drawerRole">领导</div>
            </div>
            <div class="sidebar-title">功能菜单</div>
            <div class="sidebar-menu">
                <div class="sidebar-item active" onclick="switchTab('pending')">
                    <i class="bi bi-list-check"></i>
                    待审批列表
                </div>
                <div class="sidebar-item" onclick="switchTab('history')">
                    <i class="bi bi-clock-history"></i>
                    审批历史
                </div>
                <div class="sidebar-item" onclick="switchTab('stats')">
                    <i class="bi bi-bar-chart"></i>
                    数据统计
                </div>
                <div class="sidebar-item" onclick="switchTab('users')">
                    <i class="bi bi-people"></i>
                    用户管理
                </div>
                <!-- ✅ 新增：修改密码 -->
                <div class="sidebar-item" onclick="openChangePassword()">
                    <i class="bi bi-shield-lock"></i>
                    修改密码
                </div>
            </div>
        </div>

        <!-- 主内容区 -->
        <div class="main-content">
            <div class="page-header">
                <!-- 问候语：字号比标题更大，突出显示 -->
                <div class="greeting">
                    你好，
                    <a href="javascript:void(0);" onclick="switchTab('users')" class="user-name-link">
                        <asp:Label ID="lblAdminName" runat="server"></asp:Label>
                    </a>
                </div>
                <h1 class="page-title">院长审批 & 统计</h1>
                <p class="page-subtitle">审批超过3天的请假申请，查看全校统计数据</p>
            </div>

            <!-- 1. 待审批列表 -->
            <div id="pending" class="content-section active">
                <div class="card glass">
                    <div class="card-cover">
                        <i class="bi bi-list-check"></i>
                    </div>
                    <h3 class="card-title">待审批列表（>3天）</h3>
                    <p class="card-description">审批3天以上的学生请假申请</p>
                    
                    <div class="table-responsive">
                    <asp:GridView ID="gvAdmin" runat="server" CssClass="table"
                        AutoGenerateColumns="False" DataKeyNames="LeaveId" GridLines="None">
                        <Columns>
                            <asp:BoundField DataField="LeaveId" HeaderText="申请编号" />
                            <asp:BoundField DataField="UserName" HeaderText="学生姓名" />
                            <asp:BoundField DataField="StartDate" HeaderText="开始日期" DataFormatString="{0:yyyy-MM-dd}" HtmlEncode="false" />
                            <asp:BoundField DataField="EndDate" HeaderText="结束日期" DataFormatString="{0:yyyy-MM-dd}" HtmlEncode="false" />
                            <asp:BoundField DataField="Days" HeaderText="请假天数" />
                            <asp:BoundField DataField="Reason" HeaderText="请假原因" />
                            <asp:TemplateField HeaderText="当前状态">
                                <ItemTemplate>
                                    <span class="status status-pending"><%# Eval("Status") %></span>
                                </ItemTemplate>
                            </asp:TemplateField>
                            <asp:TemplateField HeaderText="操作">
                                <ItemTemplate>
                                    <button type="button" class="btn-action btn-approve" 
                                        onclick='adminApprove(<%# Eval("LeaveId") %>, "院长通过")'>通过</button>
                                    <button type="button" class="btn-action btn-reject" 
                                        onclick='adminApprove(<%# Eval("LeaveId") %>, "拒绝")'>拒绝</button>
                                </ItemTemplate>
                            </asp:TemplateField>
                        </Columns>
                    </asp:GridView>
                    </div>
                    <asp:Label ID="lblEmptyPending" runat="server" CssClass="empty-state" Visible="false">
                        <i class="bi bi-check-circle"></i>
                        <div>暂无待审批数据</div>
                    </asp:Label>
                </div>
            </div>

            <!-- 2. 审批历史 -->
            <div id="history" class="content-section">
                <div class="card glass">
                    <div class="card-cover">
                        <i class="bi bi-clock-history"></i>
                    </div>
                    <h3 class="card-title">审批历史</h3>
                    <p class="card-description">查看您所有的审批记录</p>
                    
                    <div class="table-responsive">
                    <asp:GridView ID="gvHistory" runat="server" CssClass="table" AutoGenerateColumns="false" GridLines="None">
                        <Columns>
                            <asp:BoundField DataField="申请编号" HeaderText="申请编号" />
                            <asp:BoundField DataField="学生姓名" HeaderText="学生姓名" />
                            <asp:BoundField DataField="开始日期" HeaderText="开始日期" DataFormatString="{0:yyyy-MM-dd}" HtmlEncode="false" />
                            <asp:BoundField DataField="结束日期" HeaderText="结束日期" DataFormatString="{0:yyyy-MM-dd}" HtmlEncode="false" />
                            <asp:BoundField DataField="请假天数" HeaderText="请假天数" />
                            <asp:BoundField DataField="请假原因" HeaderText="请假原因" />
                            <asp:TemplateField HeaderText="审批结果">
                                <ItemTemplate>
                                    <span class='status <%# Eval("审批结果").ToString().Contains("通过") ? "status-approved" : "status-rejected" %>'>
                                        <%# Eval("审批结果") %>
                                    </span>
                                </ItemTemplate>
                            </asp:TemplateField>
                        </Columns>
                    </asp:GridView>
                    </div>
                    <asp:Label ID="lblEmptyHistory" runat="server" CssClass="empty-state" Visible="false">
                        <i class="bi bi-clock-x"></i>
                        <div>暂无审批历史数据</div>
                    </asp:Label>
                </div>
            </div>

            <!-- 3. 数据统计 -->
            <div id="stats" class="content-section">
                <div class="stats-grid">
                    <div class="card glass">
                        <div class="card-cover">
                            <i class="bi bi-building"></i>
                        </div>
                        <h3 class="card-title">班级请假统计</h3>
                        <p class="card-description">计算机学院各班级请假数据统计</p>
                        
                        <div class="table-responsive">
                        <asp:GridView ID="gvClass" runat="server" CssClass="table" AutoGenerateColumns="false" GridLines="None">
                            <Columns>
                                <asp:BoundField DataField="班级名称" HeaderText="班级名称" />
                                <asp:BoundField DataField="总请假次数" HeaderText="总请假次数" />
                                <asp:BoundField DataField="通过次数" HeaderText="通过次数" />
                                <asp:BoundField DataField="拒绝次数" HeaderText="拒绝次数" />
                                <asp:BoundField DataField="总请假天数" HeaderText="总请假天数" />
                            </Columns>
                        </asp:GridView>
                        </div>
                        <asp:Label ID="lblEmptyClass" runat="server" CssClass="empty-state" Visible="false">
                            <i class="bi bi-building-x"></i>
                            <div>暂无班级统计数据</div>
                        </asp:Label>
                    </div>

                    <div class="card glass">
                        <div class="card-cover">
                            <i class="bi bi-person"></i>
                        </div>
                        <h3 class="card-title">学生请假统计</h3>
                        <p class="card-description">计算机学院学生个人请假数据统计</p>
                        
                        <div class="table-responsive">
                        <asp:GridView ID="gvUser" runat="server" CssClass="table" AutoGenerateColumns="false" GridLines="None">
                            <Columns>
                                <asp:BoundField DataField="学生姓名" HeaderText="学生姓名" />
                                <asp:BoundField DataField="所属班级" HeaderText="所属班级" />
                                <asp:BoundField DataField="总请假次数" HeaderText="总请假次数" />
                                <asp:BoundField DataField="总请假天数" HeaderText="总请假天数" />
                            </Columns>
                        </asp:GridView>
                        </div>
                        <asp:Label ID="lblEmptyUser" runat="server" CssClass="empty-state" Visible="false">
                            <i class="bi bi-person-x"></i>
                            <div>暂无学生统计数据</div>
                        </asp:Label>
                    </div>
                </div>
            </div>

            <!-- 4. 用户管理 → 已修复点击跳转 -->
            <div id="users" class="content-section">
                <div onclick="event.stopPropagation();" class="card glass">
                    <div class="card-cover">
                        <i class="bi bi-people"></i>
                    </div>
                    <h3 class="card-title">
                        用户管理
                        <button type="button" onclick="openAddUser(event)" class="btn btn-success btn-sm ms-3" style="border-radius:8px;">
                            <i class="bi bi-plus-circle"></i> 添加用户
                        </button>
                        <button type="button" onclick="openAddClass()" class="btn btn-primary btn-sm ms-2" style="border-radius:8px;">
                            <i class="bi bi-building-add"></i> 新建班级
                        </button>
                    </h3>
                    <p class="card-description">管理系统所有用户账户</p>
                    
                    <div class="table-responsive">
                    <asp:GridView ID="gvUsers" runat="server" CssClass="table" AutoGenerateColumns="false" GridLines="None">
                        <Columns>
                            <asp:BoundField DataField="用户编号" HeaderText="用户编号" />
                            <asp:BoundField DataField="用户名" HeaderText="用户名" />
                            <asp:BoundField DataField="用户角色" HeaderText="用户角色" />
                            <asp:BoundField DataField="所属班级" HeaderText="所属班级" />
                        </Columns>
                    </asp:GridView>
                    </div>
                    <asp:Label ID="lblEmptyUsers" runat="server" CssClass="empty-state" Visible="false">
                        <i class="bi bi-people-x"></i>
                        <div>暂无用户数据</div>
                    </asp:Label>
                </div>
            </div>

            <!-- 底部区域 -->
            <div class="footer glass">
                <div class="footer-content">
                    <div class="footer-section">
                        <h4 class="footer-title">关于系统</h4>
                        <p class="footer-text">
                            高校智慧请假管理云平台是一款专为高校设计的数字化审批平台，
                           实现了请假申请、审批、统计全流程自动化，
                           提高了教务管理效率，方便了学生和老师的日常使用。
                        </p>
                    </div>
                    
                    <div class="footer-section">
                        <h4 class="footer-title">联系我们</h4>
                        <div class="footer-contact">
                            <i class="bi bi-envelope"></i>
                            <span>admin@example.com</span>
                        </div>
                        <div class="footer-contact">
                            <i class="bi bi-telephone"></i>
                            <span>020-12345678</span>
                        </div>
                        <div class="footer-contact">
                            <i class="bi bi-geo-alt"></i>
                            <span>行政楼203室</span>
                        </div>
                    </div>
                    
                    <div class="footer-section">
                        <div class="qr-code">
                             <img src="/images/qrcode.jpg" alt="扫码关注公众号" style="width: 100%; height: 100%; object-fit: contain;">
                        </div>
                        <p class="footer-text" style="margin-top: 12px; text-align: center;">
                            关注公众号获取更多服务
                        </p>
                    </div>
                </div>
                
                <div class="footer-bottom">
                    © 2026 高校智慧请假管理云平台 | 技术支持：高校智慧请假管理云平台开发团队
                </div>
            </div>
        </div>
    </form>

    <script>
        // ========== 移动端抽屉菜单 ==========
        (function() {
            var hamburger = document.getElementById('hamburgerBtn');
            var sidebar = document.querySelector('.sidebar');
            var overlay = document.getElementById('sidebarOverlay');
            var menuItems = document.querySelectorAll('.sidebar-item');

            var adminName = document.getElementById('<%= lblAdminName.ClientID %>');
            var avatarEl = document.getElementById('drawerAvatar');
            var nameEl = document.getElementById('drawerName');
            var roleEl = document.getElementById('drawerRole');
            if (adminName && avatarEl) avatarEl.textContent = (adminName.textContent || adminName.innerText || '院').charAt(0);
            if (adminName && nameEl) nameEl.textContent = adminName.textContent || adminName.innerText || '院长';
            if (roleEl) roleEl.textContent = '领导';

            function openMenu() {
                sidebar.classList.add('open');
                overlay.classList.add('show');
                document.body.style.overflow = 'hidden';
            }
            function closeMenu() {
                sidebar.classList.remove('open');
                overlay.classList.remove('show');
                document.body.style.overflow = '';
            }

            if (hamburger) hamburger.addEventListener('click', openMenu);
            if (overlay) overlay.addEventListener('click', closeMenu);
            menuItems.forEach(function(item) {
                item.addEventListener('click', function() {
                    if (window.innerWidth <= 992) setTimeout(closeMenu, 200);
                });
            });
            window.addEventListener('resize', function() {
                if (window.innerWidth > 992) closeMenu();
            });
        })();

        // 侧边栏切换功能
        function switchTab(tabId) {
            document.querySelectorAll('.content-section').forEach(section => {
                section.classList.remove('active');
            });
            document.querySelectorAll('.sidebar-item').forEach(item => {
                item.classList.remove('active');
            });
            document.getElementById(tabId).classList.add('active');
            document.querySelector('.sidebar-item[onclick="switchTab(\'' + tabId + '\')"]').classList.add('active');
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }

        // 审批功能
        function adminApprove(leaveId, status) {
            document.getElementById("<%= hdnAdminLeaveId.ClientID %>").value = leaveId;
            document.getElementById("<%= hdnAdminStatus.ClientID %>").value = status;
            document.getElementById("<%= btnAdminApprove.ClientID %>").click();
        }

        // ✅ 修改密码弹窗
        function openChangePassword() {
            Swal.fire({
                title: '修改密码',
                html:
                    '<input id="swal_old" class="swal2-input" placeholder="原密码" type="password">' +
                    '<input id="swal_new" class="swal2-input" placeholder="新密码" type="password">' +
                    '<input id="swal_confirm" class="swal2-input" placeholder="确认新密码" type="password">',
                showCancelButton: true,
                confirmButtonText: '确认修改',
                cancelButtonText: '取消',
                preConfirm: () => {
                    const old = document.getElementById('swal_old').value;
                    const newPwd = document.getElementById('swal_new').value;
                    const confirm = document.getElementById('swal_confirm').value;

                    if (!old || !newPwd || !confirm) {
                        Swal.showValidationMessage('请填写所有字段');
                        return false;
                    }
                    if (newPwd !== confirm) {
                        Swal.showValidationMessage('两次密码不一致');
                        return false;
                    }
                    return { oldPwd: old, newPwd: newPwd };
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    document.getElementById('<%=hdnOldPwd.ClientID%>').value = result.value.oldPwd;
                    document.getElementById('<%=hdnNewPwd.ClientID%>').value = result.value.newPwd;
                    document.getElementById('<%=btnChangePassword.ClientID%>').click();
                }
            });
        }

        // ✅ 添加用户（已去掉领导，辅导员显示班级）
        function openAddUser(event) {
            event.stopPropagation();

            Swal.fire({
                title: '添加新用户',
                html:
                    '<input id="add_username" class="swal2-input" placeholder="用户名">' +
                    '<input id="add_pwd" class="swal2-input" placeholder="密码" type="password">' +
                    '<select id="add_role" class="swal2-input">' +
                        '<option value="">请选择角色</option>' +
                        '<option value="学生">学生</option>' +
                        '<option value="辅导员">辅导员</option>' +
                    '</select>' +
                    '<select id="add_class" class="swal2-input" style="display:none;">' +
                        '<option value="0">请选择班级</option>' +
                    '</select>',
                showCancelButton: true,
                confirmButtonText: '确认添加',
                cancelButtonText: '取消',
                didOpen: () => {
                    const roleSel = document.getElementById('add_role');
                    const classSel = document.getElementById('add_class');

                    roleSel.addEventListener('change', function () {
                        if (this.value === '学生' || this.value === '辅导员') {
                            classSel.style.display = 'block';
                        } else {
                            classSel.style.display = 'none';
                        }
                        loadClasses(this.value === '辅导员');
                    });

                    function loadClasses(isTeacher) {
                        classSel.innerHTML = '<option value="0">请选择班级</option>';
                        fetch('Admin.aspx?action=getClasses&type=' + (isTeacher ? 'teacher' : ''))
                            .then(res => res.json())
                            .then(data => {
                                data.forEach(cls => {
                                    const opt = document.createElement('option');
                                    opt.value = cls.ClassId;
                                    opt.innerText = cls.ClassName;
                                    classSel.appendChild(opt);
                                });
                            });
                    }
                },
                preConfirm: () => {
                    const username = document.getElementById('add_username').value;
                    const pwd = document.getElementById('add_pwd').value;
                    const role = document.getElementById('add_role').value;
                    const classId = document.getElementById('add_class').value;

                    if (!username || !pwd || !role) {
                        Swal.showValidationMessage('请填写完整信息');
                        return false;
                    }
                    if ((role === '学生' || role === '辅导员') && classId == 0) {
                        Swal.showValidationMessage('必须选择班级');
                        return false;
                    }
                    return { username, pwd, role, classId };
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    document.getElementById('<%=hdnAddUserName.ClientID%>').value = result.value.username;
                    document.getElementById('<%=hdnAddPwd.ClientID%>').value = result.value.pwd;
                    document.getElementById('<%=hdnAddRole.ClientID%>').value = result.value.role;
                    document.getElementById('<%=hdnAddClassId.ClientID%>').value = result.value.classId;
                    document.getElementById('<%=btnAddUser.ClientID%>').click();
                }
            });
        }

        // ✅ 新建班级弹窗
        function openAddClass() {
            Swal.fire({
                title: '新建班级',
                html: '<input id="new_class_name" class="swal2-input" placeholder="例如：2026级计算机1班">',
                showCancelButton: true,
                confirmButtonText: '创建',
                cancelButtonText: '取消',
                preConfirm: () => {
                    const name = document.getElementById('new_class_name').value.trim();
                    if (!name) {
                        Swal.showValidationMessage('请输入班级名称');
                        return false;
                    }
                    return { name };
                }
            }).then(result => {
                if (result.isConfirmed) {
                    document.getElementById('<%=hdnNewClassName.ClientID%>').value = result.value.name;
                    document.getElementById('<%=btnAddClass.ClientID%>').click();
                }
            });
        }
    </script>
</body>
</html>