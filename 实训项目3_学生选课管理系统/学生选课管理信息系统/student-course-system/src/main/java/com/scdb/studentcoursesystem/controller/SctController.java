package com.scdb.studentcoursesystem.controller;

import com.scdb.studentcoursesystem.annotation.RequireRole;
import com.scdb.studentcoursesystem.entity.Result;
import com.scdb.studentcoursesystem.entity.Sct;
import com.scdb.studentcoursesystem.service.SctService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 选课控制器（移除HttpServletRequest依赖，改用前端传参方式）
 * 解决Servlet API编译错误 + 注入规范问题
 */
@RestController
@RequestMapping("/sct")
public class SctController {

    private static final Logger logger = LoggerFactory.getLogger(SctController.class);
    private final SctService sctService; // 改用构造器注入，解决"不建议字段注入"警告

    // 构造器注入（替代@Autowired字段注入）
    public SctController(SctService sctService) {
        this.sctService = sctService;
    }

    /**
     * 添加选课记录
     */
    @RequireRole({"STUDENT"})
    @PostMapping("/add")
    public Result<?> add(@RequestBody Sct sct) {
        try {
            if (sct == null || sct.getSno() == null || sct.getCno() == null
                    || sct.getSno().trim().isEmpty() || sct.getCno().trim().isEmpty()) {
                return Result.error("学号/课程号不能为空");
            }
            // 过滤admin账号选课
            if ("admin".equals(sct.getSno().trim())) {
                return Result.error("管理员账号禁止选课");
            }
            boolean flag = sctService.addSct(sct);
            return flag ? Result.success("添加选课成功") : Result.error("添加失败：该学生已选此课程");
        } catch (Exception e) {
            logger.error("添加选课异常", e);
            return Result.error("添加选课异常：" + e.getMessage());
        }
    }

    /**
     * 查询所有选课信息（关联学生姓名/课程名称）
     */
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> list() {
        try {
            List<Map<String, Object>> sctList = sctService.getAllSctsWithName();
            return Result.success("查询选课成功", sctList);
        } catch (Exception e) {
            logger.error("查询选课异常", e);
            return Result.error("查询选课异常：" + e.getMessage());
        }
    }

    /**
     * 查询指定学生的选课信息（前端传学号，替代HttpServletRequest）
     * 调用示例：/sct/my?sno=202324115122
     */
    @GetMapping("/my")
    public Result<List<Map<String, Object>>> getMySct(@RequestParam String sno) {
        try {
            if (sno == null || sno.trim().isEmpty()) {
                logger.warn("查询个人选课：学号为空");
                return Result.error("学号不能为空");
            }
            // 过滤admin账号查询
            if ("admin".equals(sno.trim())) {
                return Result.error("管理员账号无选课记录");
            }
            List<Map<String, Object>> mySct = sctService.getSctBySno(sno.trim());
            if (mySct == null || mySct.isEmpty()) {
                return Result.success("暂无选课记录", mySct);
            }
            return Result.success("查询成功", mySct);
        } catch (Exception e) {
            logger.error("查询学生{}选课异常", sno, e);
            return Result.error("查询选课失败：" + e.getMessage());
        }
    }

    /**
     * 查询所有选课信息（纯实体，兼容旧接口）
     */
    @GetMapping("/list/entity")
    public Result<List<Sct>> listEntity() {
        try {
            List<Sct> sctList = sctService.getAllScts();
            return Result.success("查询选课成功", sctList);
        } catch (Exception e) {
            logger.error("查询选课实体异常", e);
            return Result.error("查询选课实体异常：" + e.getMessage());
        }
    }

    /**
     * 按学号+课程号查询选课记录
     */
    @GetMapping("/query/{sno}/{cno}")
    public Result<Sct> query(@PathVariable String sno, @PathVariable String cno) {
        try {
            if (sno == null || cno == null || sno.trim().isEmpty() || cno.trim().isEmpty()) {
                return Result.error("学号/课程号不能为空");
            }
            Sct sct = sctService.getSctBySnoAndCno(sno.trim(), cno.trim());
            return sct != null ? Result.success("查询选课成功", sct) : Result.error("未查询到该选课记录");
        } catch (Exception e) {
            logger.error("查询选课异常", e);
            return Result.error("查询选课异常：" + e.getMessage());
        }
    }

    /**
     * 修改选课记录（学期/成绩）
     */
    @RequireRole({"TEACHER", "ADMIN"})
    @PutMapping("/update")
    public Result<?> update(@RequestBody Sct sct) {
        try {
            if (sct == null || sct.getSno() == null || sct.getCno() == null
                    || sct.getSno().trim().isEmpty() || sct.getCno().trim().isEmpty()) {
                return Result.error("学号/课程号不能为空");
            }
            boolean flag = sctService.updateSct(sct);
            return flag ? Result.success("修改选课成功") : Result.error("修改失败：选课记录不存在");
        } catch (Exception e) {
            logger.error("修改选课异常", e);
            return Result.error("修改选课异常：" + e.getMessage());
        }
    }

    /**
     * 删除选课记录
     */
    @DeleteMapping("/delete/{sno}/{cno}")
    public Result<?> delete(@PathVariable String sno, @PathVariable String cno) {
        try {
            if (sno == null || cno == null || sno.trim().isEmpty() || cno.trim().isEmpty()) {
                return Result.error("学号/课程号不能为空");
            }
            boolean flag = sctService.deleteSctBySnoAndCno(sno.trim(), cno.trim());
            return flag ? Result.success("删除选课成功") : Result.error("删除失败：未找到该选课记录");
        } catch (Exception e) {
            logger.error("删除选课异常", e);
            return Result.error("删除选课异常：" + e.getMessage());
        }
    }
}