package com.supermarket.points.model.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderInfo {
    private Integer id;         // 订单ID
    private String orderNo;     // 订单编号
    private Integer userId;     // 用户ID
    private Integer commodityId;// 商品ID
    private BigDecimal amount;  // 订单金额
    private Integer points;     // 获得/扣除积分
    private String payStatus;   // 支付状态（PAID-已支付，UNPAID-未支付）
    private String orderStatus; // 订单状态（SUCCESS-成功，CANCEL-取消）
    private Date createTime;    // 创建时间
    private Date payTime;       // 支付时间
}