package com.supermarket.points.controller;

import com.supermarket.points.mapper.CommodityMapper;
import com.supermarket.points.mapper.SysPointsLogMapper;
import com.supermarket.points.mapper.SysUserMapper;
import com.supermarket.points.model.entity.Commodity;
import com.supermarket.points.model.entity.SysPointsLog;
import com.supermarket.points.model.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/points")
@PreAuthorize("hasRole('ADMIN')")
public class PointsController {
    private static final Logger logger = LoggerFactory.getLogger(PointsController.class);

    private final SysUserMapper sysUserMapper;
    private final SysPointsLogMapper pointsLogMapper;
    private final CommodityMapper commodityMapper;

    public PointsController(SysUserMapper sysUserMapper,
                            SysPointsLogMapper pointsLogMapper,
                            CommodityMapper commodityMapper) {
        this.sysUserMapper = sysUserMapper;
        this.pointsLogMapper = pointsLogMapper;
        this.commodityMapper = commodityMapper;
    }

    // 跳转到消费加积分页面
    @GetMapping("/consume")
    public String toConsume(Model model) {
        try {
            List<Commodity> commodityList = commodityMapper.selectExchangeableCommodities();
            model.addAttribute("commodityList", commodityList);
            logger.info("加载消费加积分页面，查询到商品数量：{}", commodityList.size());
        } catch (Exception e) {
            logger.error("加载消费加积分页面失败：查询商品列表异常", e);
            model.addAttribute("errorMsg", "加载商品列表失败：" + e.getMessage());
        }
        return "admin/points/consume";
    }

    // 消费加积分提交（核心修改：去掉longValue()，用Integer直接赋值）
    @PostMapping("/consume")
    public String consume(@RequestParam Integer userId,
                          @RequestParam Integer commodityId,
                          @RequestParam Integer num,
                          @RequestParam(required = false, defaultValue = "") String remark,
                          Model model) {
        try {
            SysUser user = sysUserMapper.selectById(userId);
            Commodity commodity = commodityMapper.selectById(commodityId);

            String commodityName = commodity != null ? commodity.getName() : "未知商品";
            BigDecimal commodityPrice = commodity != null && commodity.getPrice() != null
                    ? commodity.getPrice()
                    : BigDecimal.ZERO;

            if (user == null) {
                logger.warn("消费加积分失败：用户ID {} 不存在", userId);
                model.addAttribute("errorMsg", "用户不存在");
                return toConsume(model);
            }
            if (commodity == null) {
                logger.warn("消费加积分失败：商品ID {} 不存在", commodityId);
                model.addAttribute("errorMsg", "商品不存在");
                return toConsume(model);
            }
            if (num <= 0) {
                logger.warn("消费加积分失败：用户ID {} 消费数量无效（{}）", userId, num);
                model.addAttribute("errorMsg", "消费数量必须大于0");
                return toConsume(model);
            }

            BigDecimal addPointsBig = commodityPrice
                    .divide(new BigDecimal(2), 0, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(num));
            int addPoints = addPointsBig.intValue();

            if (addPoints <= 0) {
                logger.warn("消费加积分失败：商品ID {} 价格配置无效（{}元），消费数量：{}",
                        commodityId, commodityPrice, num);
                model.addAttribute("errorMsg", "商品价格配置异常，无法计算积分（积分需大于0）");
                return toConsume(model);
            }

            user.setPoints(user.getPoints() + addPoints);
            user.setUpdateTime(new Date());
            sysUserMapper.updateUser(user);

            // 核心修改：去掉longValue()，直接用Integer赋值
            SysPointsLog log = new SysPointsLog();
            log.setUserId(userId); // 直接用userId（Integer）
            log.setUsername(user.getUsername() != null ? user.getUsername() : "未知用户");
            log.setPoints(addPoints);
            log.setType("消费");
            String logRemark = remark.isEmpty()
                    ? "消费商品：" + commodityName + "×" + num + "（单价：" + commodityPrice + "元），自动加积分：" + addPoints + "（价格÷2）"
                    : remark + "（消费商品：" + commodityName + "×" + num + "，单价：" + commodityPrice + "元，自动加积分：" + addPoints + "（价格÷2））";
            log.setRemark(logRemark);
            log.setCreateTime(new Date());
            log.setCommodityId(commodityId); // 直接用commodityId（Integer）
            log.setCommodityName(commodityName);
            pointsLogMapper.insert(log);

            logger.info("用户ID {} 消费加积分成功：商品 {}×{}（单价{}元），新增积分 {}（价格÷2）",
                    userId, commodityName, num, commodityPrice, addPoints);
        } catch (Exception e) {
            logger.error("用户ID {} 消费加积分操作失败", userId, e);
            model.addAttribute("errorMsg", "操作失败：" + e.getMessage());
            return toConsume(model);
        }
        return "redirect:/admin/user/list";
    }

    // 跳转到积分兑换商品页面
    @GetMapping("/exchange")
    public String toExchange(@RequestParam("userId") Integer userId, Model model) {
        try {
            SysUser user = sysUserMapper.selectById(userId);
            if (user == null) {
                logger.warn("加载兑换页面失败：用户ID {} 不存在", userId);
                model.addAttribute("errorMsg", "用户不存在");
                return "admin/points/exchange";
            }
            List<Commodity> commodityList = commodityMapper.selectExchangeableCommodities();
            logger.info("用户ID {} 加载兑换页面，查询到可兑换商品数量：{}", userId, commodityList.size());

            model.addAttribute("userId", userId);
            model.addAttribute("userPoints", user.getPoints());
            model.addAttribute("commodityList", commodityList);
        } catch (Exception e) {
            logger.error("用户ID {} 加载兑换页面失败", userId, e);
            model.addAttribute("errorMsg", "加载兑换页面失败：" + e.getMessage());
        }
        return "admin/points/exchange";
    }

    // 兑换商品扣积分提交（核心修改：去掉longValue()）
    @PostMapping("/exchange")
    @Transactional
    public String exchange(@RequestParam Integer userId,
                           @RequestParam Integer commodityId,
                           @RequestParam Integer num,
                           @RequestParam(required = false, defaultValue = "") String remark,
                           Model model) {
        try {
            SysUser user = sysUserMapper.selectById(userId);
            Commodity commodity = commodityMapper.selectById(commodityId);

            String commodityName = commodity != null ? commodity.getName() : "未知商品";
            int commodityPoints = commodity != null ? commodity.getPoints() : 0;

            if (user == null) {
                logger.warn("兑换商品失败：用户ID {} 不存在", userId);
                model.addAttribute("errorMsg", "用户不存在");
                return toExchange(userId, model);
            }
            if (commodity == null) {
                logger.warn("兑换商品失败：商品ID {} 不存在", commodityId);
                model.addAttribute("errorMsg", "商品不存在");
                return toExchange(userId, model);
            }

            int totalPoints = commodityPoints * num;

            if (commodityPoints <= 0) {
                logger.warn("兑换商品失败：商品ID {} 积分配置无效（{}）", commodityId, commodityPoints);
                model.addAttribute("errorMsg", "该商品未配置兑换积分，无法兑换！");
                return toExchange(userId, model);
            }
            if (user.getPoints() < totalPoints) {
                logger.warn("兑换商品失败：用户ID {} 积分不足（当前：{}，需：{}）", userId, user.getPoints(), totalPoints);
                model.addAttribute("errorMsg",
                        "积分不足！当前积分：" + user.getPoints() +
                                "，所需总积分：" + totalPoints +
                                "（商品积分：" + commodityPoints + " × 数量：" + num + "）");
                return toExchange(userId, model);
            }
            if (commodity.getStock() < num) {
                logger.warn("兑换商品失败：商品ID {} 库存不足（当前：{}，请求：{}）", commodityId, commodity.getStock(), num);
                model.addAttribute("errorMsg",
                        "商品库存不足！当前库存：" + commodity.getStock() +
                                "，请求兑换数量：" + num);
                return toExchange(userId, model);
            }

            user.setPoints(user.getPoints() - totalPoints);
            user.setUpdateTime(new Date());
            sysUserMapper.updateUser(user);

            // 原来的代码：只有一行调用，没校验结果
// commodityMapper.updateStock(commodityId, num);

// 改成：加日志 + 校验结果 + 抛异常
            int stockAffect = commodityMapper.updateStock(commodityId, num);
            if (stockAffect == 0) {
                logger.error("用户ID {} 兑换商品ID {}×{} 库存扣减失败！数据库未更新", userId, commodityId, num);
                throw new RuntimeException("库存扣减失败！请联系管理员");
            }
            logger.info("用户ID {} 兑换商品ID {}×{} 库存扣减成功，影响行数：{}", userId, commodityId, num, stockAffect);

            // 核心修改：去掉longValue()，直接用Integer赋值
            SysPointsLog log = new SysPointsLog();
            log.setUserId(userId); // 直接用userId（Integer）
            log.setUsername(user.getUsername() != null ? user.getUsername() : "未知用户");
            log.setPoints(-totalPoints);
            log.setType("兑换");
            String logRemark = remark.isEmpty()
                    ? "（商品：" + commodityName + "×" + num + "，扣减积分：" + totalPoints + "）"
                    : remark + "（商品：" + commodityName + "×" + num + "，扣减积分：" + totalPoints + "）";
            log.setRemark(logRemark);
            log.setCreateTime(new Date());
            log.setCommodityId(commodityId); // 直接用commodityId（Integer）
            log.setCommodityName(commodityName);
            pointsLogMapper.insert(log);

            logger.info("用户ID {} 兑换商品成功：商品ID {}×{}，扣减积分 {}", userId, commodityId, num, totalPoints);
            return "redirect:/admin/user/list";
        } catch (Exception e) {
            logger.error("用户ID {} 兑换商品ID {}×{} 操作失败", userId, commodityId, num, e);
            model.addAttribute("errorMsg", "兑换失败：" + e.getMessage());
            return toExchange(userId, model);
        }
    }

    // 查看用户积分日志
    @GetMapping("/log")
    public String pointsLog(@RequestParam Integer userId,
                            @RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            Model model) {
        try {
            Integer start = (pageNum - 1) * pageSize;
            List<SysPointsLog> logList = pointsLogMapper.selectByUserIdPage(userId, start, pageSize);

            for (SysPointsLog log : logList) {
                if (log == null) continue;
                log.setType(convertType(log.getType()));
                if (log.getCommodityName() == null || log.getCommodityName().isEmpty()) {
                    log.setCommodityName("未知商品");
                }
                if (log.getUsername() == null || log.getUsername().isEmpty()) {
                    log.setUsername("未知用户");
                }
            }

            Integer total = pointsLogMapper.selectLogTotalByUserId(userId);
            SysUser user = sysUserMapper.selectById(userId);
            model.addAttribute("username", user != null ? user.getUsername() : "未知用户");
            model.addAttribute("logList", logList);
            model.addAttribute("total", total);
            model.addAttribute("pageNum", pageNum);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("userId", userId);

            logger.info("查询用户ID {} 积分日志，页码 {}，每页 {} 条，总计 {} 条", userId, pageNum, pageSize, total);
        } catch (Exception e) {
            logger.error("查询用户ID {} 积分日志失败，页码 {}，每页 {} 条", userId, pageNum, pageSize, e);
            model.addAttribute("errorMsg", "查询日志失败：" + e.getMessage());
        }
        return "admin/points/log";
    }

    // 管理员查看所有积分日志
    @GetMapping("/allLog")
    public String allPointsLog(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               Model model) {
        try {
            Integer start = (pageNum - 1) * pageSize;
            List<SysPointsLog> logList = pointsLogMapper.selectAllPage(start, pageSize);

            for (SysPointsLog log : logList) {
                if (log == null) continue;
                log.setType(convertType(log.getType()));
                if (log.getCommodityName() == null || log.getCommodityName().isEmpty()) {
                    log.setCommodityName("未知商品");
                }
                if (log.getUsername() == null || log.getUsername().isEmpty()) {
                    log.setUsername("未知用户");
                }
            }

            Integer total = pointsLogMapper.selectAllLogTotal();
            model.addAttribute("logList", logList);
            model.addAttribute("total", total);
            model.addAttribute("pageNum", pageNum);
            model.addAttribute("pageSize", pageSize);

            logger.info("管理员查询所有积分日志，页码 {}，每页 {} 条，总计 {} 条", pageNum, pageSize, total);
        } catch (Exception e) {
            logger.error("管理员查询所有积分日志失败，页码 {}，每页 {} 条", pageNum, pageSize, e);
            model.addAttribute("errorMsg", "查询日志失败：" + e.getMessage());
        }
        return "admin/points/allLog";
    }

    // 工具方法：统一转换操作类型
    private String convertType(String type) {
        if (type == null || type.isEmpty()) {
            return "未知操作";
        }
        return switch (type.trim().toUpperCase()) {
            case "CONSUME", "消费" -> "消费";
            case "EXCHANGE", "兑换" -> "兑换";
            case "ADMIN", "管理员操作" -> "管理员操作";
            default -> type;
        };
    }
}