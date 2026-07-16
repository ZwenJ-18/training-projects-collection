package com.supermarket.points.model.entity;

import lombok.Data;
import java.util.Date;

@Data
public class PointsRecord {
    private Integer id;         // 记录ID
    private Integer userId;     // 用户ID
    private Integer commodityId;// 商品ID
    private Integer points;     // 积分变动（正数-增加，负数-扣除）
    private String type;        // 变动类型（BUY-购买获取，EXCHANGE-兑换扣除）
    private Date createTime;    // 记录时间
}