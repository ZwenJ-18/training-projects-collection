\# 🤖 实训项目1 - 智能聊天机器人客户端



\## 📌 项目简介

这是一款基于 Android 平台的智能聊天机器人客户端，用户可以通过文本消息与机器人进行对话交互。

项目实现了消息列表展示、对话气泡UI、消息发送与接收等核心功能。



\---



🛠️ 技术栈

语言：Java

平台：Android

构建工具：Gradle

核心组件：RecyclerView、Handler、Thread、AndroidX







✨ 功能说明

1\.  用户交互

&#x20;   - 用户输入文本消息，点击发送按钮发送

&#x20;   - 机器人模拟回复文本消息

2\.消息展示

&#x20;   - 使用 RecyclerView 实现消息列表

&#x20;   - 区分用户消息和机器人消息的左右气泡样式

&#x20;   - 消息发送后自动滚动到底部

3\. 基础架构

&#x20;   - 多线程模拟机器人回复的延迟效果

&#x20;   - 使用 Handler 在主线程更新 UI

&#x20;   - 消息实体类封装，实现解耦







📂 项目结构

text

实训项目1\_智能聊天机器人客户端/

├── app/

│   ├── src/main/

│   │   ├── java/cn/itcast/intelrobot/

│   │   │   ├── ChatBean.java       # 消息实体类

│   │   │   ├── ChatAdapter.java    # 消息列表适配器

│   │   │   └── MainActivity.java   # 主界面Activity

│   │   ├── res/

│   │   │   ├── layout/             # 布局文件

│   │   │   ├── drawable/           # 背景、图标资源

│   │   │   └── values/            # 颜色、字符串配置

│   │   └── AndroidManifest.xml     # 应用清单文件

│   └── build.gradle

└── build.gradle



🚀 运行说明

环境要求：Android Studio + JDK + Android SDK（API 21+）

导入项目：在 Android Studio 中选择「Open an existing project」，选择项目根目录

运行方式：连接 Android 模拟器或真机，点击 Run 按钮即可启动应用





📄 版权声明

本项目为课程实训作业，仅用于学习交流，禁止商用或抄袭。



