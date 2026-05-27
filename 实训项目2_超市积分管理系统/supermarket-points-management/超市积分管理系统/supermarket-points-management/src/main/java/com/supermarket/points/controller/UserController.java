package com.supermarket.points.controller;

import com.supermarket.points.mapper.CommodityMapper;
import com.supermarket.points.mapper.SysPointsLogMapper;
import com.supermarket.points.mapper.SysUserMapper;
import com.supermarket.points.model.entity.Commodity;
import com.supermarket.points.model.entity.SysPointsLog;
import com.supermarket.points.model.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/user")
public class UserController {

    private final SysUserMapper sysUserMapper;
    private final CommodityMapper commodityMapper;
    private final SysPointsLogMapper pointsLogMapper;

    public UserController(SysUserMapper sysUserMapper, CommodityMapper commodityMapper, SysPointsLogMapper pointsLogMapper) {
        this.sysUserMapper = sysUserMapper;
        this.commodityMapper = commodityMapper;
        this.pointsLogMapper = pointsLogMapper;
    }

    @GetMapping("/list")
    public String userList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model) {
        if (pageNum < 1) pageNum = 1;
        if (pageSize < 1 || pageSize > 100) pageSize = 10;

        try {
            Integer start = (pageNum - 1) * pageSize;
            List<SysUser> userList = sysUserMapper.selectAll(start, pageSize);
            Integer total = sysUserMapper.selectTotal();

            log.info("查询用户列表 - 页码：{}，每页条数：{}，结果数：{}，总数：{}",
                    pageNum, pageSize, userList.size(), total);

            model.addAttribute("userList", userList);
            model.addAttribute("total", total);
            model.addAttribute("pageNum", pageNum);
            model.addAttribute("pageSize", pageSize);
            int totalPages = total == 0 ? 1 : (total + pageSize - 1) / pageSize;
            model.addAttribute("totalPages", totalPages);

        } catch (Exception e) {
            log.error("查询用户列表失败", e);
            model.addAttribute("errorMsg", "查询用户列表失败：" + e.getMessage());
            return "admin/error"; // 这里的admin/error如果不存在，改成你实际的错误页
        }
        return "admin/user/list";
    }

    @PostMapping("/exchange")
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public String exchangeCommodity(
            @RequestParam("commodityId") Integer commodityId,
            RedirectAttributes redirect) {
        log.info("开始处理积分兑换请求，商品ID：{}", commodityId);

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || "anonymousUser".equals(authentication.getName())) {
                log.warn("积分兑换失败：用户未登录");
                redirect.addFlashAttribute("errorMsg", "请先登录后再进行兑换操作");
                return "redirect:/login";
            }

            String username = authentication.getName();
            SysUser user = sysUserMapper.selectByUsername(username);
            if (user == null) {
                log.warn("积分兑换失败：用户不存在，用户名：{}", username);
                redirect.addFlashAttribute("errorMsg", "用户信息不存在，请重新登录");
                return "redirect:/login";
            }

            Commodity commodity = commodityMapper.selectById(commodityId);
            if (commodity == null) {
                log.warn("积分兑换失败：商品不存在，商品ID：{}", commodityId);
                redirect.addFlashAttribute("errorMsg", "兑换的商品不存在");
                return "redirect:/user/home";
            }

            if (commodity.getStock() == null || commodity.getStock() <= 0) {
                log.warn("积分兑换失败：商品库存不足，商品ID：{}，当前库存：{}",
                        commodityId, commodity.getStock());
                redirect.addFlashAttribute("errorMsg", commodity.getName() + " 库存不足，无法兑换");
                return "redirect:/user/home";
            }

            Integer userPoints = user.getPoints() == null ? 0 : user.getPoints();
            Integer commodityPoints = commodity.getPoints() == null ? 0 : commodity.getPoints();
            if (userPoints < commodityPoints) {
                log.warn("积分兑换失败：用户积分不足，用户名：{}，用户积分：{}，商品所需积分：{}",
                        username, userPoints, commodityPoints);
                redirect.addFlashAttribute("errorMsg", "积分不足！当前积分：" + userPoints + "，所需积分：" + commodityPoints);
                return "redirect:/user/home";
            }

            // 修复：把updateById改成你已有的updateUser方法
            user.setPoints(userPoints - commodityPoints);
            sysUserMapper.updateUser(user);
            log.info("用户积分扣减成功，用户名：{}，扣减积分：{}，剩余积分：{}",
                    username, commodityPoints, user.getPoints());

            // 扣减商品库存
            int updateCount = commodityMapper.updateStock(commodityId, -1);
            if (updateCount == 0) {
                log.warn("商品库存扣减失败，商品ID：{}", commodityId);
                throw new RuntimeException("库存扣减失败，请重试");
            }
            Commodity updatedCommodity = commodityMapper.selectById(commodityId);
            log.info("商品库存扣减成功，商品ID：{}，商品名称：{}，原库存：{}，新库存：{}",
                    commodityId, commodity.getName(), commodity.getStock(), updatedCommodity.getStock());

            // 记录积分日志
            SysPointsLog logEntity = new SysPointsLog();
            logEntity.setUserId(user.getId());
            logEntity.setUsername(username);
            logEntity.setPoints(-commodityPoints);
            logEntity.setType("兑换商品");
            logEntity.setRemark("兑换商品：" + commodity.getName() + "（商品ID：" + commodityId + "）");
            logEntity.setCommodityId(commodityId);
            logEntity.setCommodityName(commodity.getName());
            logEntity.setCreateTime(new Date());
            pointsLogMapper.insert(logEntity);
            log.info("积分日志记录成功，日志ID：{}", logEntity.getId());

            redirect.addFlashAttribute("successMsg",
                    "兑换成功！已扣减 " + commodityPoints + " 积分，" +
                            commodity.getName() + " 库存：" + commodity.getStock() + " → " + updatedCommodity.getStock());

        } catch (Exception e) {
            log.error("积分兑换异常，商品ID：{}", commodityId, e);
            redirect.addFlashAttribute("errorMsg", "兑换失败：" + e.getMessage());
        }

        return "redirect:/user/home";
    }

    @GetMapping("/error")
    public String errorPage(Model model) {
        if (!model.containsAttribute("errorMsg")) {
            model.addAttribute("errorMsg", "操作失败，请重试！");
        }
        return "admin/error"; // 如果没有这个页面，改成"redirect:/admin/user/list"
    }

    @GetMapping("/success")
    public String successPage(Model model) {
        if (!model.containsAttribute("successMsg")) {
            model.addAttribute("successMsg", "操作成功！");
        }
        return "admin/success"; // 如果没有这个页面，改成"redirect:/admin/user/list"
    }

    @GetMapping({"/logout", "/user/logout"})
    public String logout(HttpSession session) {
        SecurityContextHolder.clearContext();
        session.invalidate();
        log.info("用户退出登录成功，Session已销毁");
        return "redirect:/login";
    }
}