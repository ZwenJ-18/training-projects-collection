<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Student.aspx.cs" Inherits="WebLab5.Student" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head runat="server">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>请假申请 | 学生服务系统</title>
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
            --bg-gradient: linear-gradient(135deg, #fafbfc 0%, #f1f3f5 100%);
            --glass-bg: rgba(255, 255, 255, 0.65);
            --glass-border: rgba(255, 255, 255, 0.25);
            --glass-shadow: 0 8px 32px rgba(0, 0, 0, 0.04);
            --glass-shadow-hover: 0 20px 60px rgba(0, 0, 0, 0.08);
            
            --text-primary: #212529;
            --text-secondary: #6c757d;
            --text-tertiary: #adb5bd;
            
            --accent-primary: #495077;
            --accent-hover: #343a40;
            --accent-active: #212529;
            --accent-success: #28a745;
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
            --transition-bounce: 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55);
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
            background: rgba(0, 0, 0, 0.05);
            color: var(--text-primary);
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

        /* 卡片网格布局 - 自适应多设备 */
        .card-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
            gap: var(--spacing-lg);
        }

        /* 毛玻璃卡片 - 核心视觉元素 */
        .card {
            border-radius: var(--radius-2xl);
            padding: var(--spacing-xl);
            transition: var(--transition-smooth);
            overflow: hidden;
        }

        .card:hover {
            transform: translateY(-12px);
            box-shadow: var(--glass-shadow-hover);
        }

        .card-cover {
            width: 100%;
            height: 120px;
            border-radius: var(--radius-lg);
            background: linear-gradient(135deg, #e9ecef 0%, #dee2e6 100%);
            margin-bottom: var(--spacing-lg);
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .card-cover i {
            font-size: 48px;
            color: var(--text-tertiary);
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

        /* 信息行样式 */
        .info-row {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 16px;
            padding: 10px 0;
            border-bottom: 1px solid var(--divider-color);
        }

        .info-row:last-child {
            border-bottom: none;
            margin-bottom: 0;
            padding-bottom: 0;
        }

        .info-row i {
            font-size: 16px;
            color: var(--text-tertiary);
            width: 20px;
        }

        .info-row span {
            font-size: 15px;
            color: var(--text-primary);
            flex: 1;
        }

        .info-row strong {
            font-weight: 600;
            color: var(--accent-primary);
        }

        /* 统一表单样式 */
        .form-group {
            margin-bottom: var(--spacing-lg);
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
            padding: 16px 18px;
            font-size: 15px;
            color: var(--text-primary);
            background: rgba(255, 255, 255, 0.5);
            transition: var(--transition-smooth);
            font-family: inherit;
        }

        .form-control:focus {
            outline: none;
            border-color: var(--accent-primary);
            box-shadow: 0 0 0 3px rgba(73, 80, 87, 0.08);
            background: rgba(255, 255, 255, 0.8);
        }

        .form-control::placeholder {
            color: var(--text-tertiary);
        }

        /* 统一主按钮样式 - 物理级回弹 */
        .btn {
            border: none;
            border-radius: var(--radius-md);
            padding: 16px 24px;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
            transition: var(--transition-spring);
            width: 100%;
            letter-spacing: 0.2px;
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

        .empty-state {
            color: var(--text-tertiary);
            font-size: 14px;
            text-align: center;
            padding: 60px 0;
            display: flex;
            the-flex-direction: column;
            align-items: center;
            gap: 12px;
        }

        .empty-state i {
            font-size: 48px;
            color: var(--text-tertiary);
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

        /* 二维码图片样式 */
        .qr-code-img {
            width: 100%;
            height: 100%;
            object-fit: contain; /* 保持图片比例不变形 */
        }

        .footer-bottom {
            padding-top: var(--spacing-lg);
            border-top: 1px solid var(--divider-color);
            color: var(--text-tertiary);
            font-size: 13px;
        }

        /* ==============================================
           响应式设计 - 完美适配所有设备
           ============================================== */
        @media (max-width: 1200px) {
            .sidebar {
                width: calc(25% - var(--spacing-lg));
            }
            .main-content {
                margin-left: 25%;
            }
            .card-grid {
                grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
            }
        }

        @media (max-width: 992px) {
            .sidebar {
                display: none;
            }
            .main-content {
                margin-left: 0;
                padding: 0 var(--spacing-md) var(--spacing-xl);
            }
            .card-grid {
                grid-template-columns: 1fr;
            }
            .footer-content {
                grid-template-columns: 1fr;
                text-align: center;
            }
            .footer-section {
                text-align: center;
            }
        }

        @media (max-width: 576px) {
            .top-nav {
                padding: 0 var(--spacing-md);
            }
            .nav-links {
                gap: var(--spacing-sm);
            }
            .nav-link span {
                display: none;
            }
            .page-title {
                font-size: 20px;
            }
            .greeting {
                font-size: 26px;
            }
            .card {
                padding: var(--spacing-lg);
            }
        }
    </style>
</head>
<body>
    <form runat="server">
        <!-- ✅ 修复：新增隐藏输入框，用于传递密码 -->
        <input type="hidden" id="hdnOldPwd" runat="server" />
        <input type="hidden" id="hdnNewPwd" runat="server" />

        <!-- 顶部导航 -->
        <div class="top-nav">
            <div class="nav-links">
                <a href="Student.aspx" class="nav-link active">
                    <i class="bi bi-calendar-check"></i>
                    <span>请假申请</span>
                </a>
                <a href="https://bndchainletter.com" target="_blank" rel="noopener noreferrer" class="nav-link">
                    <i class="bi bi-search"></i>
                    <span>门童</span>
                </a>
                <a href="https://nmixx.lnk.to/BlueValentine" target="_blank" rel="noopener noreferrer" class="nav-link">
                    <i class="bi bi-github"></i>
                    <span>爻</span>
                </a>
            </div>
            <asp:LinkButton ID="lnkLogout" runat="server" OnClick="lnkLogout_Click" class="nav-link">
                <i class="bi bi-box-arrow-right"></i>
                <span>退出登录</span>
            </asp:LinkButton>
        </div>

        <!-- 左侧侧边栏 -->
        <div class="sidebar glass">
            <div class="sidebar-title">功能菜单</div>
            <div class="sidebar-menu">
                <div class="sidebar-item active" onclick="switchTab('apply')">
                    <i class="bi bi-pencil-square"></i>
                    提交请假申请
                </div>
                <div class="sidebar-item" onclick="switchTab('history')">
                    <i class="bi bi-clock-history"></i>
                    我的请假记录
                </div>
                <div class="sidebar-item" onclick="switchTab('profile')">
                    <i class="bi bi-person"></i>
                    个人信息
                </div>
                <div class="sidebar-item" onclick="openChangePassword()">
                    <i class="bi bi-shield-lock"></i>
                    修改密码
                </div>
            </div>
        </div>

        <!-- 主内容区 -->
        <div class="main-content">
            <div class="page-header">
                <div class="greeting">
                    你好，
                    <a href="javascript:void(0);" onclick="switchTab('profile')" class="user-name-link">
                        <asp:Label ID="lblName" runat="server"></asp:Label>
                    </a>
                </div>
                <h1 class="page-title">请假申请</h1>
                <p class="page-subtitle">快速提交您的请假申请，系统将自动流转审批</p>
            </div>

            <!-- 1. 提交请假申请 -->
            <div id="apply" class="content-section active">
                <div class="card-grid">
                    <div class="card glass">
                        <div class="card-cover">
                            <i class="bi bi-person-badge"></i>
                        </div>
                        <h3 class="card-title">个人信息</h3>
                        <p class="card-description">查看您的基本信息和本学期请假统计</p>
                        
                        <div class="info-row">
                            <i class="bi bi-person"></i>
                            <span>姓名：<asp:Label ID="lblCardName" runat="server"></asp:Label></span>
                        </div>
                        <div class="info-row">
                            <i class="bi bi-building"></i>
                            <span>班级：<asp:Label ID="lblClass" runat="server"></asp:Label></span>
                        </div>
                        <div class="info-row">
                            <i class="bi bi-calendar-check"></i>
                            <span>本学期已请假：<strong><asp:Label ID="lblLeaveCount" runat="server" Text="0"></asp:Label></strong> 次</span>
                        </div>
                    </div>

                    <div class="card glass">
                        <div class="card-cover">
                            <i class="bi bi-file-earmark-text"></i>
                        </div>
                        <h3 class="card-title">填写请假信息</h3>
                        <p class="card-description">请准确填写请假时间和原因，以便及时审批</p>
                        
                        <div class="form-group">
                            <label class="form-label">开始日期</label>
                            <asp:TextBox ID="txtStart" runat="server" CssClass="form-control" TextMode="Date"></asp:TextBox>
                        </div>
                        <div class="form-group">
                            <label class="form-label">结束日期</label>
                            <asp:TextBox ID="txtEnd" runat="server" CssClass="form-control" TextMode="Date"></asp:TextBox>
                        </div>
                        <div class="form-group">
                            <label class="form-label">请假原因</label>
                            <asp:TextBox ID="txtReason" runat="server" CssClass="form-control" TextMode="MultiLine" Rows="3" placeholder="请详细说明请假原因"></asp:TextBox>
                        </div>
                        <asp:Button ID="btnSubmit" runat="server" Text="提交申请" CssClass="btn btn-primary" OnClick="btnSubmit_Click" />
                    </div>
                </div>
            </div>
            
            <!-- 2. 我的请假记录 -->
            <div id="history" class="content-section">
                <div class="card glass">
                    <div class="card-cover">
                        <i class="bi bi-clock-history"></i>
                    </div>
                    <h3 class="card-title">我的请假记录</h3>
                    <p class="card-description">查看您所有的请假申请和审批状态</p>
                    
                    <asp:GridView ID="gvHistory" runat="server" CssClass="table" AutoGenerateColumns="false" GridLines="None">
                        <Columns>
                            <asp:BoundField DataField="申请编号" HeaderText="申请编号" />
                            <asp:BoundField DataField="开始日期" HeaderText="开始日期" DataFormatString="{0:yyyy-MM-dd}" HtmlEncode="false" />
                            <asp:BoundField DataField="结束日期" HeaderText="结束日期" DataFormatString="{0:yyyy-MM-dd}" HtmlEncode="false" />
                            <asp:BoundField DataField="请假天数" HeaderText="请假天数" />
                            <asp:BoundField DataField="请假原因" HeaderText="请假原因" />
                            <asp:TemplateField HeaderText="当前状态">
                                <ItemTemplate>
                                    <span class='status <%# Eval("当前状态").ToString() == "待审批" ? "status-pending" : Eval("当前状态").ToString().Contains("通过") ? "status-approved" : "status-rejected" %>'>
                                        <%# Eval("当前状态") %>
                                    </span>
                                </ItemTemplate>
                            </asp:TemplateField>
                        </Columns>
                    </asp:GridView>
                    <asp:Label ID="lblEmptyHistory" runat="server" CssClass="empty-state" Visible="false">
                        <i class="bi bi-calendar-x"></i>
                        <div>暂无请假记录</div>
                    </asp:Label>
                </div>
            </div>

            <!-- 3. 个人信息 -->
            <div id="profile" class="content-section">
                <div class="card glass">
                    <div class="card-cover">
                        <i class="bi bi-person"></i>
                    </div>
                    <h3 class="card-title">个人信息</h3>
                    <p class="card-description">您的系统账户基本信息</p>
                    
                    <div class="info-row">
                        <i class="bi bi-person"></i>
                        <span>姓名：<asp:Label ID="lblProfileName" runat="server"></asp:Label></span>
                    </div>
                    <div class="info-row">
                        <i class="bi bi-building"></i>
                        <span>班级：<asp:Label ID="lblProfileClass" runat="server"></asp:Label></span>
                    </div>
                    <div class="info-row">
                        <i class="bi bi-person-badge"></i>
                        <span>角色：学生</span>
                    </div>
                </div>
            </div>

            <!-- 底部区域 -->
            <div class="footer glass">
                <div class="footer-content">
                    <div class="footer-section">
                        <h4 class="footer-title">关于系统</h4>
                        <p class="footer-text">
                            学生请假管理系统是一款专为高校设计的数字化审批平台，
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
                    © 2026 学生请假管理系统 | 技术支持：WebLab5开发团队
                </div>
            </div>
        </div>

        <!-- ✅ 修复：隐藏的修改密码按钮 -->
        <asp:Button ID="btnChangePassword" runat="server" OnClick="btnChangePassword_Click" style="display:none;" />
    </form>

    <script>
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

        // ✅ 修复：修改密码弹窗（能正确传值到后台）
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
                    // 把密码存入隐藏域
                    document.getElementById('<%=hdnOldPwd.ClientID%>').value = result.value.oldPwd;
                    document.getElementById('<%=hdnNewPwd.ClientID%>').value = result.value.newPwd;
                    // 触发后台
                    document.getElementById('<%=btnChangePassword.ClientID%>').click();
                }
            });
        }

        document.getElementById('<%= btnSubmit.ClientID %>').addEventListener('click', function(e) {
            const start = document.getElementById('<%= txtStart.ClientID %>').value;
            const end = document.getElementById('<%= txtEnd.ClientID %>').value;
            const reason = document.getElementById('<%= txtReason.ClientID %>').value;
            if (!start || !end || !reason) {
                e.preventDefault(); Swal.fire('提示', '请填写完整信息', 'warning');
            }
        });
    </script>
</body>
</html>