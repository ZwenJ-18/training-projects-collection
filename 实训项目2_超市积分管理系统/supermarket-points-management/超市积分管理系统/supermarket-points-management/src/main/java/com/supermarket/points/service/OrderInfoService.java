package com.supermarket.points.service;

import com.supermarket.points.model.dto.OrderCreateDTO;
import com.supermarket.points.model.entity.OrderInfo;
import com.supermarket.points.model.vo.PageVO;

public interface OrderInfoService {
    // 创建订单
    String createOrder(Integer userId, OrderCreateDTO dto);

    // 分页查询用户订单
    PageVO<OrderInfo> getUserOrderPage(Integer userId, Integer pageNum, Integer pageSize);

    // 支付订单
    boolean payOrder(String orderNo);
}