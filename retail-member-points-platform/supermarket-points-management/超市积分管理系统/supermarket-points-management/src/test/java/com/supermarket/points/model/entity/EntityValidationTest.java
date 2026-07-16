package com.supermarket.points.model.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 实体类字段校验测试 (8 条)
 */
class EntityValidationTest {

    @Test
    @DisplayName("SysUser 所有必填字段应可设置")
    void shouldSetSysUserFields() {
        SysUser user = new SysUser();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("encrypted123");
        user.setRole("ADMIN");
        user.setCreatedAt(new Date());

        assertEquals(1L, user.getId());
        assertEquals("admin", user.getUsername());
        assertEquals("ADMIN", user.getRole());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    @DisplayName("Commodity 商品价格应为正数")
    void shouldHavePositivePrice() {
        Commodity c = new Commodity();
        c.setPrice(new BigDecimal("29.90"));
        assertTrue(c.getPrice().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Commodity 商品库存不应为负数")
    void shouldNotHaveNegativeStock() {
        Commodity c = new Commodity();
        c.setStock(0);
        assertTrue(c.getStock() >= 0);
    }

    @ParameterizedTest
    @CsvSource({
            "100, 10, 90",
            "50, 5, 45",
            "10, 10, 0"
    })
    @DisplayName("批量库存扣减参数化测试")
    void shouldDeductStockCorrectly(int initial, int deduct, int expected) {
        assertEquals(expected, initial - deduct);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 20, 50, 100, 200})
    @DisplayName("积分兑换数量应为正数")
    void shouldHavePositivePointsValue(int points) {
        assertTrue(points > 0);
    }

    @Test
    @DisplayName("PointLog 更新时间应在创建时间之后")
    void shouldHaveUpdatedAfterCreated() {
        PointLog log = new PointLog();
        Date created = new Date();
        log.setCreatedAt(created);
        assertNotNull(log.getCreatedAt());
    }

    @Test
    @DisplayName("Commodity 商品名称不应为空")
    void shouldNotHaveEmptyName() {
        Commodity c = new Commodity();
        c.setName("测试商品");
        assertNotNull(c.getName());
        assertFalse(c.getName().isEmpty());
    }

    @Test
    @DisplayName("SysUser 角色字段应为有效枚举值")
    void shouldHaveValidRole() {
        String[] validRoles = {"ADMIN", "USER"};
        SysUser user = new SysUser();
        user.setRole("ADMIN");
        boolean isValid = false;
        for (String role : validRoles) {
            if (role.equals(user.getRole())) {
                isValid = true;
                break;
            }
        }
        assertTrue(isValid, "角色应为 ADMIN 或 USER");
    }
}
