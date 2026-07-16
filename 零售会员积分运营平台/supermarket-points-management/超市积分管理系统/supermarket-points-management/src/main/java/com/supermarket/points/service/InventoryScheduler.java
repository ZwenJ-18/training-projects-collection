package com.supermarket.points.service;

import com.supermarket.points.mapper.CommodityMapper;
import com.supermarket.points.model.entity.Commodity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 库存预警定时任务
 * 每日凌晨 2 点扫描所有商品库存，低于 50 的商品输出告警日志
 */
@Component
@EnableScheduling
public class InventoryScheduler {

    private static final Logger logger = LoggerFactory.getLogger(InventoryScheduler.class);
    private static final int WARNING_THRESHOLD = 50;

    private final CommodityMapper commodityMapper;

    public InventoryScheduler(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void checkLowStock() {
        logger.info("===== 开始执行库存预警扫描 =====");
        try {
            List<Commodity> commodities = commodityMapper.selectAll();
            int lowStockCount = 0;
            for (Commodity c : commodities) {
                if (c.getStock() != null && c.getStock() < WARNING_THRESHOLD) {
                    logger.warn("⚠ 库存预警: 商品 [{}] 库存仅剩 {}，请及时补货！", c.getName(), c.getStock());
                    lowStockCount++;
                }
            }
            logger.info("===== 库存预警扫描完成，发现 {} 件低库存商品 =====", lowStockCount);
        } catch (Exception e) {
            logger.error("库存预警扫描异常", e);
        }
    }
}
