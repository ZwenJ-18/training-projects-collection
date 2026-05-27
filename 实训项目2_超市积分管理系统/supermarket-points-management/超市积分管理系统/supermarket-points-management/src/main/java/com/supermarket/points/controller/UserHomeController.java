package com.supermarket.points.controller;

import com.supermarket.points.model.entity.Commodity;
import com.supermarket.points.model.entity.PointLog;
import com.supermarket.points.model.entity.SysUser;
import com.supermarket.points.model.vo.PageVO;
import com.supermarket.points.service.CommodityService;
import com.supermarket.points.service.PointLogService;
import com.supermarket.points.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserHomeController {
    private static final Logger logger = LoggerFactory.getLogger(UserHomeController.class);

    @Autowired
    private PointLogService pointLogService;
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private UserService userService;

    // 积分日志
    @GetMapping("/myPointLog")
    public String myPointLog(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        SysUser currentUser = userService.getUserByUsername(username);
        List<PointLog> logList = pointLogService.getLogByUserId(currentUser.getId());
        model.addAttribute("username", username);
        model.addAttribute("logList", logList);
        return "user/myPointLog";
    }

    // 用户首页（已修复：nickname 正常传递）
    @GetMapping("/home")
    public String userHome(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        SysUser currentUser = userService.getUserByUsername(username);

        model.addAttribute("username", username);
        model.addAttribute("nickname", currentUser.getNickname());
        model.addAttribute("userPoints", currentUser.getPoints());

        PageVO<?> commodityPage = commodityService.getOnSaleCommodityPage(1, 10);
        model.addAttribute("commodityList", commodityPage.getList());

        return "user/home";
    }

    // 修改昵称页面
    @GetMapping("/editNickname")
    public String editNickname(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        SysUser user = userService.getUserByUsername(username);
        model.addAttribute("nickname", user.getNickname());
        return "user/edit_nickname";
    }

    // 保存修改昵称
    @PostMapping("/updateNickname")
    public String updateNickname(@RequestParam String nickname,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        try {
            String username = userDetails.getUsername();
            SysUser user = userService.getUserByUsername(username);
            userService.updateNickname(user.getId(), nickname);
            redirectAttributes.addFlashAttribute("successMsg", "昵称修改成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/user/editNickname";
    }

    // 消费确认
    @GetMapping("/consume")
    public String consume(@RequestParam Long commodityId, Model model) {
        Commodity commodity = commodityService.getById(commodityId);
        if (commodity == null) {
            model.addAttribute("errorMsg", "商品不存在！");
            return "user/home";
        }

        BigDecimal price = commodity.getPrice() == null ? BigDecimal.ZERO : commodity.getPrice();
        logger.info("商品【{}】价格：{}元", commodity.getName(), price);

        BigDecimal pointsBig = price.divide(BigDecimal.valueOf(2), 0, RoundingMode.HALF_UP);
        int rewardPoints = pointsBig.intValue();
        logger.info("商品【{}】可获积分：{}分", commodity.getName(), rewardPoints);

        model.addAttribute("commodity", commodity);
        model.addAttribute("rewardPoints", rewardPoints);
        return "user/consume_confirm";
    }

    // 确认消费
    @PostMapping("/doConsume")
    public String doConsume(@RequestParam Long commodityId,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        try {
            String username = userDetails.getUsername();
            SysUser user = userService.getUserByUsername(username);
            Commodity commodity = commodityService.getById(commodityId);

            if (user == null || commodity == null) {
                redirectAttributes.addFlashAttribute("error", "用户或商品不存在！");
                return "redirect:/user/home";
            }

            BigDecimal price = commodity.getPrice() == null ? BigDecimal.ZERO : commodity.getPrice();
            BigDecimal pointsBig = price.divide(BigDecimal.valueOf(2), 0, RoundingMode.HALF_UP);
            Integer rewardPoints = pointsBig.intValue();

            if (rewardPoints <= 0) {
                redirectAttributes.addFlashAttribute("error", "该商品消费无积分！");
                return "redirect:/user/home";
            }

            userService.addPoints(username, rewardPoints);
            commodityService.updateStock(commodityId.intValue(), 1);

            PointLog consumeLog = new PointLog();
            consumeLog.setUserId(user.getId());
            consumeLog.setUsername(username);
            consumeLog.setCommodityId(commodityId.intValue());
            consumeLog.setCommodityName(commodity.getName());
            consumeLog.setPointChange(rewardPoints);
            consumeLog.setType("CONSUME");
            pointLogService.addPointLog(consumeLog);

            redirectAttributes.addFlashAttribute("msg", "消费成功！获得" + rewardPoints + "积分");
            logger.info("用户【{}】消费【{}】成功，积分+{}", username, commodity.getName(), rewardPoints);

        } catch (Exception e) {
            logger.error("消费失败：", e);
            redirectAttributes.addFlashAttribute("error", "消费失败！" + e.getMessage());
        }
        return "redirect:/user/home";
    }

    // ======================== 修改密码 ========================
    @GetMapping("/editPassword")
    public String editPassword() {
        return "user/edit_password";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        try {
            String username = userDetails.getUsername();
            SysUser user = userService.getUserByUsername(username);
            userService.updatePassword(user.getId(), oldPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMsg", "密码修改成功！请重新登录");
            return "redirect:/logout";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/user/editPassword";
        }
    }

    // 积分兑换
    @PostMapping("/exchange")
    @Transactional
    public String exchange(@RequestParam Long commodityId,
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        try {
            String username = userDetails.getUsername();
            SysUser user = userService.getUserByUsername(username);
            Commodity commodity = commodityService.getById(commodityId);

            if (commodity == null) {
                redirectAttributes.addFlashAttribute("error", "商品不存在！");
                return "redirect:/user/home";
            }
            if (user.getPoints() < commodity.getPoints()) {
                redirectAttributes.addFlashAttribute("error", "积分不足！");
                return "redirect:/user/home";
            }
            if (commodity.getStock() < 1) {
                redirectAttributes.addFlashAttribute("error", "商品库存不足！");
                return "redirect:/user/home";
            }

            userService.deductPoints(username, commodity.getPoints());
            int stockAffect = commodityService.updateStock(commodityId.intValue(), 1);

            if (stockAffect == 0) {
                throw new RuntimeException("库存扣减失败");
            }

            PointLog exchangeLog = new PointLog();
            exchangeLog.setUserId(user.getId());
            exchangeLog.setUsername(username);
            exchangeLog.setCommodityId(commodityId.intValue());
            exchangeLog.setCommodityName(commodity.getName());
            exchangeLog.setPointChange(-commodity.getPoints());
            exchangeLog.setType("EXCHANGE");
            pointLogService.addPointLog(exchangeLog);

            redirectAttributes.addFlashAttribute("msg", "兑换成功！已扣减" + commodity.getPoints() + "积分");

        } catch (Exception e) {
            logger.error("兑换失败：", e);
            redirectAttributes.addFlashAttribute("error", "兑换失败！" + e.getMessage());
        }
        return "redirect:/user/home";
    }
}