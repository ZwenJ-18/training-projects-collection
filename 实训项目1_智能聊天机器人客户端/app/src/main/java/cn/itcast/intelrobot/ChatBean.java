package cn.itcast.intelrobot;

public class ChatBean {
    // 0 = 机器人消息（左边），1 = 用户消息（右边）
    public static final int TYPE_ROBOT = 0;
    public static final int TYPE_USER = 1;

    private String content; // 消息内容
    private int type;      // 消息类型

    public ChatBean(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
