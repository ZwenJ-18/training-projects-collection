package com.supermarket.points.model.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Commodity {
    private Integer id;         // 商品ID
    private String name;        // 商品名称
    // 新增：商品编码字段
    private String code;
    private String cover;       // 商品封面
    private String description; // 商品描述
    private BigDecimal price;   // 商品价格（BigDecimal类型，精确计算）
    private Integer stock;      // 库存
    private Integer points;     // 兑换所需积分（保留，不影响消费积分计算）
    private Date createTime;    // 创建时间
    private Integer status;     // 状态（1-上架，0-下架）
}