package com.supermarket.points.model.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class OrderCreateDTO {
    @NotNull(message = "商品ID不能为空")
    private Integer commodityId;

    @NotNull(message = "购买数量不能为空")
    private Integer num;
}