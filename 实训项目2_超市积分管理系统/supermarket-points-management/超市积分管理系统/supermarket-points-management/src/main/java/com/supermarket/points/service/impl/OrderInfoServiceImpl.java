package com.supermarket.points.service.impl;

import com.supermarket.points.mapper.OrderInfoMapper;
import com.supermarket.points.model.dto.OrderCreateDTO;
import com.supermarket.points.model.entity.Commodity;
import com.supermarket.points.model.entity.OrderInfo;
import com.supermarket.points.model.entity.PointsRecord;
import com.supermarket.points.model.entity.SysUser;
import com.supermarket.points.model.vo.PageVO;
import com.supermarket.points.service.CommodityService;
import com.supermarket.points.service.OrderInfoService;
import com.supermarket.points.service.PointsRecordService;
import com.supermarket.points.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private UserService userService;
    @Autowired
    private PointsRecordService pointsRecordService;

    @Override
    @Transactional
    public String createOrder(Integer userId, OrderCreateDTO dto) {
        // 1. 查商品
        Commodity commodity = commodityService.getCommodityById(dto.getCommodityId());
        if (commodity == null || commodity.getStock() < dto.getNum()) {
            return null;
        }
        // 2. 生成订单号
        String orderNo = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        // 3. 计算订单金额和积分
        BigDecimal amount = commodity.getPrice().multiply(new BigDecimal(dto.getNum()));
        Integer points = commodity.getPoints() * dto.getNum(); // 购买获得积分
        // 4. 扣减商品库存
        boolean deductStock = commodityService.deductStock(commodity.getId(), dto.getNum());
        if (!deductStock) {
            return null;
        }
        // 5. 新增订单
        OrderInfo order = new OrderInfo();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setCommodityId(commodity.getId());
        order.setAmount(amount);
        order.setPoints(points);
        order.setPayStatus("UNPAID");
        order.setOrderStatus("UNPAID");
        order.setCreateTime(new Date());
        orderInfoMapper.insert(order);
        // 6. 返回订单号
        return orderNo;
    }

    @Override
    public PageVO<OrderInfo> getUserOrderPage(Integer userId, Integer pageNum, Integer pageSize) {
        pageNum = (pageNum - 1) * pageSize;
        List<OrderInfo> list = orderInfoMapper.selectByUserId(userId, pageNum, pageSize);
        Integer total = orderInfoMapper.selectTotalByUserId(userId);
        PageVO<OrderInfo> pageVO = new PageVO<>();
        pageVO.setList(list);
        pageVO.setTotal(total);
        pageVO.setPageNum(pageNum / pageSize + 1);
        pageVO.setPageSize(pageSize);
        pageVO.setTotalPage((total + pageSize - 1) / pageSize);
        return pageVO;
    }

    @Override
    @Transactional
    public boolean payOrder(String orderNo) {
        // 1. 这里简化支付逻辑，实际项目需要对接支付接口
        // 2. 更新订单状态
        boolean update = orderInfoMapper.updatePayStatus(orderNo, "PAID", "SUCCESS") > 0;
        if (!update) {
            return false;
        }
        // 3. 查询订单信息
        OrderInfo order = orderInfoMapper.selectByOrderNo(orderNo);
        if (order == null) {
            return false;
        }
        // 4. 给用户增加积分
        userService.updateUserPoints(order.getUserId(), order.getPoints());
        // 5. 新增积分记录
        PointsRecord record = new PointsRecord();
        record.setUserId(order.getUserId());
        record.setCommodityId(order.getCommodityId());
        record.setPoints(order.getPoints());
        record.setType("BUY");
        pointsRecordService.addPointsRecord(record);
        return true;
    }
}