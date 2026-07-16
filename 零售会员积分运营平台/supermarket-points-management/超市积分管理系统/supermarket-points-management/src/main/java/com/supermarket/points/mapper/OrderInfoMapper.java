package com.supermarket.points.mapper;

import com.supermarket.points.model.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderInfoMapper {
    // 新增订单
    int insert(OrderInfo orderInfo);

    // 根据用户ID查订单（分页）
    List<OrderInfo> selectByUserId(@Param("userId") Integer userId, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    // 查询用户订单总数
    Integer selectTotalByUserId(Integer userId);

    // 根据订单号更新支付状态
    int updatePayStatus(@Param("orderNo") String orderNo, @Param("payStatus") String payStatus, @Param("orderStatus") String orderStatus);

    // 新增：根据订单号查询订单（解决编译错误）
    @Select("SELECT * FROM order_info WHERE order_no = #{orderNo}")
    OrderInfo selectByOrderNo(String orderNo);
}