package com.supermarket.points.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermarket.points.model.entity.Commodity;
import com.supermarket.points.mapper.CommodityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 商品管理控制器
 * 路径前缀：/admin/commodity
 */
@Controller
@RequestMapping("/admin/commodity")
public class CommodityController {

    private static final Logger log = LoggerFactory.getLogger(CommodityController.class);

    // 图片保存根路径（和application.properties中的静态资源映射对应）
    private static final String UPLOAD_PATH = "C:/supermarket_images/images/";

    @Autowired
    private CommodityMapper commodityMapper;

    /**
     * 跳转商品列表页（修复搜索+分页）
     */
    @GetMapping("/goods-list")
    public String list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            Model model
    ) {
        try {
            // 1. 规范PageHelper使用（避免警告）
            PageHelper.startPage(pageNum, pageSize);

            // 2. 执行查询
            List<Commodity> commodityList;
            if (keyword != null && !keyword.trim().isEmpty()) {
                // 传递模糊查询的关键词（%已在Controller拼接，XML直接用）
                commodityList = commodityMapper.selectByKeyword("%" + keyword.trim() + "%");
                log.info("搜索商品：关键词={}，找到{}条", keyword, commodityList.size());
            } else {
                commodityList = commodityMapper.selectList();
            }

            // 3. 封装分页信息（PageInfo自动计算总页数/总条数）
            PageInfo<Commodity> pageInfo = new PageInfo<>(commodityList);

            // 4. 回显数据到页面
            model.addAttribute("commodityList", commodityList);
            model.addAttribute("pageInfo", pageInfo);
            model.addAttribute("keyword", keyword);

        } catch (Exception e) {
            log.error("查询商品列表失败", e);
            model.addAttribute("errorMsg", "查询商品失败：" + e.getMessage());
        }
        return "admin/commodity/list";
    }

    /**
     * 跳转新增商品页面
     */
    @GetMapping("/new")
    public String add() {
        return "admin/commodity/add";
    }

    /**
     * 保存新增的商品（新增图片上传功能）
     */
    @PostMapping("/save")
    public String save(
            @RequestParam("name") String name,
            @RequestParam("code") String code,
            @RequestParam("price") Double price,
            @RequestParam("stock") Integer stock,
            @RequestParam(value = "points", defaultValue = "0") Integer points,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "description", defaultValue = "") String description,
            // 新增：接收上传的图片文件
            @RequestParam(value = "coverFile", required = false) MultipartFile coverFile,
            Model model
    ) {
        try {
            // 1. 参数校验
            if (name == null || name.trim().isEmpty()) {
                model.addAttribute("errorMsg", "商品名称不能为空");
                return "admin/commodity/add";
            }
            if (code == null || code.trim().isEmpty()) {
                model.addAttribute("errorMsg", "商品编码不能为空");
                return "admin/commodity/add";
            }
            // 校验编码是否重复
            Commodity existCommodity = commodityMapper.selectByCode(code.trim());
            if (existCommodity != null) {
                model.addAttribute("errorMsg", "商品编码已存在，请更换");
                return "admin/commodity/add";
            }
            if (price == null || price < 0) {
                model.addAttribute("errorMsg", "价格必须大于等于0");
                return "admin/commodity/add";
            }
            if (stock == null || stock < 0) {
                model.addAttribute("errorMsg", "库存必须大于等于0");
                return "admin/commodity/add";
            }

            // 2. 处理图片上传
            String coverPath = "";
            if (coverFile != null && !coverFile.isEmpty()) {
                try {
                    // 2.1 获取原文件名
                    String originalFilename = coverFile.getOriginalFilename();
                    if (originalFilename == null || originalFilename.isEmpty()) {
                        model.addAttribute("errorMsg", "图片文件名不能为空");
                        return "admin/commodity/add";
                    }

                    // 2.2 校验图片格式（只允许jpg/png/webp）
                    String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
                    if (!suffix.equals(".jpg") && !suffix.equals(".png") && !suffix.equals(".webp")) {
                        model.addAttribute("errorMsg", "仅支持jpg/png/webp格式的图片");
                        return "admin/commodity/add";
                    }

                    // 2.3 生成唯一文件名（避免重复覆盖）
                    String uniqueFileName = UUID.randomUUID().toString() + suffix;

                    // 2.4 确保保存目录存在
                    File uploadDir = new File(UPLOAD_PATH);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs(); // 递归创建目录
                    }

                    // 2.5 保存图片到指定路径
                    File destFile = new File(UPLOAD_PATH + uniqueFileName);
                    coverFile.transferTo(destFile);

                    // 2.6 组装数据库存储的路径（前端可访问）
                    coverPath = "/images/" + uniqueFileName;
                    log.info("图片上传成功：{} -> {}", originalFilename, coverPath);

                } catch (IOException e) {
                    log.error("图片上传失败", e);
                    model.addAttribute("errorMsg", "图片上传失败：" + e.getMessage());
                    return "admin/commodity/add";
                }
            }

            // 3. 组装商品数据
            Commodity commodity = new Commodity();
            commodity.setName(name.trim());
            commodity.setCode(code.trim());
            commodity.setPrice(BigDecimal.valueOf(price));
            commodity.setStock(stock);
            commodity.setPoints(points);
            commodity.setStatus(status);
            commodity.setDescription(description);
            commodity.setCover(coverPath); // 保存图片路径
            commodity.setCreateTime(new Date());

            // 4. 执行新增
            int rows = commodityMapper.insert(commodity);
            if (rows > 0) {
                log.info("新增商品成功：名称={}，编码={}，图片={}", name, code, coverPath);
                return "redirect:/admin/commodity/goods-list";
            } else {
                model.addAttribute("errorMsg", "新增商品失败：数据库插入失败");
            }

        } catch (Exception e) {
            log.error("新增商品失败", e);
            model.addAttribute("errorMsg", "新增商品失败：" + e.getMessage());
            return "admin/commodity/add";
        }
        return "admin/commodity/add";
    }

    /**
     * 跳转修改商品页面
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        try {
            // 1. 校验ID
            if (id == null || id < 1) {
                return "redirect:/admin/commodity/goods-list";
            }

            // 2. 查询商品
            Commodity commodity = commodityMapper.selectById(id);
            if (commodity == null) {
                log.error("商品ID{}不存在", id);
                return "redirect:/admin/commodity/goods-list";
            }

            // 3. 传递数据到页面
            model.addAttribute("commodity", commodity);

        } catch (Exception e) {
            log.error("跳转编辑页面失败", e);
            return "redirect:/admin/commodity/goods-list";
        }
        return "admin/commodity/edit";
    }

    /**
     * 保存商品修改（支持图片更新）
     */
    @PostMapping("/update")
    public String update(
            @RequestParam("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("stock") Integer stock,
            @RequestParam(value = "points", defaultValue = "0") Integer points,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "description", defaultValue = "") String description,
            // 新增：接收上传的新图片
            @RequestParam(value = "coverFile", required = false) MultipartFile coverFile,
            Model model
    ) {
        try {
            // 1. 参数校验
            if (id == null || id < 1) {
                return "redirect:/admin/commodity/goods-list";
            }
            if (name == null || name.trim().isEmpty()) {
                model.addAttribute("errorMsg", "商品名称不能为空");
                return "redirect:/admin/commodity/edit/" + id;
            }
            if (price == null || price < 0) {
                model.addAttribute("errorMsg", "价格必须大于等于0");
                return "redirect:/admin/commodity/edit/" + id;
            }
            if (stock == null || stock < 0) {
                model.addAttribute("errorMsg", "库存必须大于等于0");
                return "redirect:/admin/commodity/edit/" + id;
            }

            // 2. 查询原商品
            Commodity oldCommodity = commodityMapper.selectById(id);
            if (oldCommodity == null) {
                log.error("商品ID{}不存在", id);
                return "redirect:/admin/commodity/goods-list";
            }

            // 3. 处理图片更新（如果上传了新图片）
            String coverPath = oldCommodity.getCover(); // 默认保留原图片路径
            if (coverFile != null && !coverFile.isEmpty()) {
                try {
                    // 3.1 获取原文件名
                    String originalFilename = coverFile.getOriginalFilename();
                    if (originalFilename == null || originalFilename.isEmpty()) {
                        model.addAttribute("errorMsg", "图片文件名不能为空");
                        return "redirect:/admin/commodity/edit/" + id;
                    }

                    // 3.2 校验图片格式
                    String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
                    if (!suffix.equals(".jpg") && !suffix.equals(".png") && !suffix.equals(".webp")) {
                        model.addAttribute("errorMsg", "仅支持jpg/png/webp格式的图片");
                        return "redirect:/admin/commodity/edit/" + id;
                    }

                    // 3.3 生成唯一文件名
                    String uniqueFileName = UUID.randomUUID().toString() + suffix;

                    // 3.4 保存新图片
                    File uploadDir = new File(UPLOAD_PATH);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }
                    File destFile = new File(UPLOAD_PATH + uniqueFileName);
                    coverFile.transferTo(destFile);

                    // 3.5 更新图片路径
                    coverPath = "/images/" + uniqueFileName;
                    log.info("商品{}图片更新成功：{}", id, coverPath);

                } catch (IOException e) {
                    log.error("图片更新失败", e);
                    model.addAttribute("errorMsg", "图片更新失败：" + e.getMessage());
                    return "redirect:/admin/commodity/edit/" + id;
                }
            }

            // 4. 组装更新数据
            Commodity updateCommodity = new Commodity();
            updateCommodity.setId(id);
            updateCommodity.setName(name.trim());
            updateCommodity.setPrice(BigDecimal.valueOf(price));
            updateCommodity.setStock(stock);
            updateCommodity.setPoints(points);
            updateCommodity.setStatus(status);
            updateCommodity.setDescription(description);
            updateCommodity.setCover(coverPath); // 更新图片路径

            // 保留原有字段
            updateCommodity.setCode(oldCommodity.getCode());
            updateCommodity.setCreateTime(oldCommodity.getCreateTime());

            // 5. 执行更新
            int rows = commodityMapper.updateById(updateCommodity);
            if (rows > 0) {
                log.info("修改商品成功：ID={}, 名称={}, 状态={}, 图片={}", id, name, status == 1 ? "上架" : "下架", coverPath);
                return "redirect:/admin/commodity/goods-list";
            } else {
                model.addAttribute("errorMsg", "修改商品失败：无数据更新");
            }

        } catch (Exception e) {
            log.error("修改商品失败", e);
            model.addAttribute("errorMsg", "修改商品失败：" + e.getMessage());
            return "redirect:/admin/commodity/edit/" + id;
        }
        return "redirect:/admin/commodity/edit/" + id;
    }

    /**
     * 删除商品
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        try {
            if (id != null && id > 0) {
                // 可选：删除商品时同时删除图片文件
                Commodity commodity = commodityMapper.selectById(id);
                if (commodity != null && commodity.getCover() != null && !commodity.getCover().isEmpty()) {
                    String imgPath = UPLOAD_PATH + commodity.getCover().replace("/images/", "");
                    File imgFile = new File(imgPath);
                    if (imgFile.exists()) {
                        imgFile.delete();
                        log.info("删除商品{}的图片：{}", id, imgPath);
                    }
                }

                commodityMapper.deleteById(id);
                log.info("删除商品成功：ID={}", id);
            }
        } catch (Exception e) {
            log.error("删除商品失败", e);
        }
        return "redirect:/admin/commodity/goods-list";
    }
}