package com.supermarket.points.model.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 编辑商品DTO（前端传参）
 */
@Data
public class CommodityEditDTO {
    private Integer id;         // 商品ID（必填）
    private String name;        // 商品名称（必填）
    private String code;        // 商品编码（必填，唯一）
    private BigDecimal price;   // 价格
    private Integer stock;      // 库存
    private Integer points;     // 积分
    private Integer status;     // 状态（1=启用，0=禁用）
}