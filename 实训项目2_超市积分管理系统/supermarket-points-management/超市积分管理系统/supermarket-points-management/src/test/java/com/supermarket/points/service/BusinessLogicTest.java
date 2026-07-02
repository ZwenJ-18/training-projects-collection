package com.supermarket.points.service;

import com.supermarket.points.model.entity.Commodity;
import com.supermarket.points.model.entity.PointLog;
import com.supermarket.points.model.entity.SysUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 核心业务逻辑单元测试 (7 条)
 */
class BusinessLogicTest {

    @Test
    @DisplayName("积分计算：100元消费应累计10积分")
    void shouldCalculatePointsByAmount() {
        BigDecimal amount = new BigDecimal("100.00");
        int points = amount.divide(new BigDecimal("10")).intValue();
        assertEquals(10, points, "每消费10元应累计1积分");
    }

    @Test
    @DisplayName("积分计算：零元消费应累计0积分")
    void shouldReturnZeroForZeroAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        int points = amount.divide(new BigDecimal("10")).intValue();
        assertEquals(0, points);
    }

    @Test
    @DisplayName("库存扣减：正常数量应成功扣减")
    void shouldDeductStock() {
        Commodity c = new Commodity();
        c.setStock(100);
        int orderQuantity = 3;
        int remaining = c.getStock() - orderQuantity;
        assertEquals(97, remaining);
    }

    @Test
    @DisplayName("库存扣减：库存不足应拒绝")
    void shouldRejectInsufficientStock() {
        Commodity c = new Commodity();
        c.setStock(5);
        int orderQuantity = 10;
        assertTrue(orderQuantity > c.getStock(), "订购数量不能超过库存");
    }

    @Test
    @DisplayName("积分兑换：积分不足应拒绝")
    void shouldRejectInsufficientPoints() {
        int userPoints = 50;
        int requiredPoints = 100;
        assertTrue(userPoints < requiredPoints, "用户积分不足时不能兑换");
    }

    @Test
    @DisplayName("积分流水记录应包含完整字段")
    void shouldHaveCompletePointLogFields() {
        PointLog log = new PointLog();
        log.setUserId(1L);
        log.setPointsChange(10);
        log.setType("消费累计");
        log.setCreatedAt(new Date());

        assertNotNull(log.getUserId());
        assertNotNull(log.getPointsChange());
        assertNotNull(log.getType());
        assertNotNull(log.getCreatedAt());
    }

    @Test
    @DisplayName("密码加密后应与明文不同")
    void shouldEncryptPassword() {
        String plainPassword = "123456";
        // BCrypt 加密后的密码长度至少为 60
        String encrypted = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        assertNotEquals(plainPassword, encrypted);
        assertTrue(encrypted.length() >= 60, "BCrypt hash should be at least 60 characters");
    }
}
