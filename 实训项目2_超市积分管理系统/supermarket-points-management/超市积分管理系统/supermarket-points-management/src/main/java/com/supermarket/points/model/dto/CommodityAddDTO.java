package com.supermarket.points.model.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 新增商品DTO（前端传参）
 */
@Data
public class CommodityAddDTO {
    private String name;        // 商品名称（必填）
    private String code;        // 商品编码（必填，唯一）
    private String cover;       // 封面图片
    private String description; // 商品描述
    private BigDecimal price;   // 价格
    private Integer stock;      // 库存
    private Integer points;     // 积分
    private Integer status;     // 状态（1=启用，0=禁用）
}