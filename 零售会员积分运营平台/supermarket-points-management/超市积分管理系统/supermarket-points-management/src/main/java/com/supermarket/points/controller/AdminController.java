package com.supermarket.points.controller;

import com.supermarket.points.model.dto.CommodityAddDTO;
import com.supermarket.points.model.dto.UserAddDTO;
import com.supermarket.points.model.dto.UserEditDTO;
import com.supermarket.points.model.entity.SysUser;
import com.supermarket.points.model.vo.PageVO;
import com.supermarket.points.model.vo.ResultVO;
import com.supermarket.points.service.CommodityService;
import com.supermarket.points.service.PointLogService;
import com.supermarket.points.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private PointLogService pointLogService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/user/list_admin")
    public String userList(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           Model model) {
        try {
            PageVO<SysUser> pageVO = userService.getUserPage(pageNum, pageSize);
            if (pageVO == null) {
                model.addAttribute("errorMsg", "查询结果为空");
            } else {
                model.addAttribute("page", pageVO);
                model.addAttribute("userList", pageVO.getList());
            }
        } catch (Exception e) {
            log.error("查询用户列表失败", e);
            model.addAttribute("errorMsg", "查询失败：" + e.getMessage());
        }
        return "admin/user/list";
    }

    @GetMapping("/user/add")
    public String userAdd() {
        return "admin/user/add";
    }

    @PostMapping("/user/add")
    public String addUser(@Valid UserAddDTO dto, Model model, RedirectAttributes redirect) {
        try {
            boolean success = userService.addUser(dto);
            if (success) {
                redirect.addFlashAttribute("msg", "新增用户成功！");
                return "redirect:/admin/user/list_admin";
            } else {
                model.addAttribute("errorMsg", "用户名已存在");
                return "admin/user/add";
            }
        } catch (Exception e) {
            log.error("新增用户失败", e);
            model.addAttribute("errorMsg", "新增失败：" + e.getMessage());
            return "admin/user/add";
        }
    }

    @GetMapping("/commodity/list")
    public String commodityList(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                Model model) {
        PageVO<?> pageVO = commodityService.getCommodityPage(pageNum, pageSize);
        model.addAttribute("page", pageVO);
        model.addAttribute("commodityList", pageVO.getList());
        return "admin/commodity/list";
    }

    @GetMapping("/commodity/add")
    public String commodityAdd() {
        return "admin/commodity/add";
    }

    @PostMapping("/commodity/add")
    @ResponseBody
    public ResultVO<?> addCommodity(@Valid @RequestBody CommodityAddDTO dto) {
        boolean success = commodityService.addCommodity(dto);
        return success ? ResultVO.success() : ResultVO.error("新增失败");
    }

    @GetMapping("/point/log")
    public String pointLogList(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               Model model) {
        PageVO<?> pageVO = pointLogService.getPointLogPage(pageNum, pageSize);
        model.addAttribute("page", pageVO);
        model.addAttribute("logList", pageVO.getList());
        return "admin/point/log";
    }

    @GetMapping("/user/edit/{id}")
    public String userEdit(@PathVariable Integer id, Model model, RedirectAttributes redirect) {
        try {
            SysUser user = userService.getUserById(id);
            if (user == null) {
                redirect.addFlashAttribute("errorMsg", "用户不存在！");
                return "redirect:/admin/user/list_admin";
            }
            model.addAttribute("user", user);
            return "admin/user/edit";
        } catch (Exception e) {
            log.error("加载编辑页失败，ID：{}", id, e);
            redirect.addFlashAttribute("errorMsg", "系统异常：" + e.getMessage());
            return "redirect:/admin/user/list_admin";
        }
    }

    @PostMapping("/user/edit")
    public String editUserSubmit(UserEditDTO dto, RedirectAttributes redirect) {
        try {
            SysUser oldUser = userService.getUserById(dto.getId());
            if (oldUser == null) {
                redirect.addFlashAttribute("errorMsg", "用户不存在！");
                return "redirect:/admin/user/list_admin";
            }
            dto.setUsername(oldUser.getUsername());

            boolean success = userService.editUser(dto);
            if (success) {
                redirect.addFlashAttribute("msg", "用户修改成功！");
            } else {
                redirect.addFlashAttribute("errorMsg", "修改失败");
            }
        } catch (Exception e) {
            log.error("修改用户失败，ID：{}", dto.getId(), e);
            redirect.addFlashAttribute("errorMsg", "系统异常：" + e.getMessage());
        }
        return "redirect:/admin/user/list_admin";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Integer id, RedirectAttributes redirect) {
        try {
            boolean success = userService.deleteUser(id);
            if (success) {
                redirect.addFlashAttribute("msg", "用户删除成功！");
            } else {
                redirect.addFlashAttribute("errorMsg", "删除失败（用户不存在或为管理员）");
            }
        } catch (Exception e) {
            log.error("删除用户失败，ID：{}", id, e);
            redirect.addFlashAttribute("errorMsg", "系统异常：" + e.getMessage());
        }
        return "redirect:/admin/user/list_admin";
    }

    @PostMapping("/user/adjustPoints")
    public String adjustPoints(Integer id, Integer points, RedirectAttributes redirect) {
        try {
            SysUser user = userService.getUserById(id);
            if (user == null) {
                redirect.addFlashAttribute("errorMsg", "用户不存在！");
                return "redirect:/admin/user/edit/" + id;
            }
            int newPoints = user.getPoints() + points;
            if (newPoints < 0) {
                redirect.addFlashAttribute("errorMsg", "积分不能为负！");
                return "redirect:/admin/user/edit/" + id;
            }
            userService.updateUserPoints(id, newPoints);
            redirect.addFlashAttribute("msg", "积分调整成功！");
        } catch (Exception e) {
            log.error("调整积分失败，ID：{}", id, e);
            redirect.addFlashAttribute("errorMsg", "系统异常：" + e.getMessage());
        }
        return "redirect:/admin/user/edit/" + id;
    }

    // 👇 管理员重置密码
    @PostMapping("/user/resetPassword")
    public String resetPassword(Integer id, RedirectAttributes redirect) {
        try {
            SysUser user = userService.getUserById(id);
            if (user == null) {
                redirect.addFlashAttribute("errorMsg", "用户不存在");
                return "redirect:/admin/user/edit/" + id;
            }
            userService.resetPassword(id, "123456");
            redirect.addFlashAttribute("msg", "密码已重置为：123456");
        } catch (Exception e) {
            log.error("重置密码失败", e);
            redirect.addFlashAttribute("errorMsg", "重置失败：" + e.getMessage());
        }
        return "redirect:/admin/user/edit/" + id;
    }
}