package com.supermarket.points.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class BCryptTest {
    public static void main(String[] args) {
        // 这里替换成你数据库里实际的用户密码，比如管理员密码123456，普通用户密码654321
        String adminPwd = "admin123";
        String userPwd = "user123";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 生成加密后的密码
        String encodedAdmin = encoder.encode(adminPwd);
        String encodedUser = encoder.encode(userPwd);
        // 打印加密结果
        System.out.println("管理员密码admin123的加密值：" + encodedAdmin);
        System.out.println("普通用户密码user123的加密值：" + encodedUser);
    }
}