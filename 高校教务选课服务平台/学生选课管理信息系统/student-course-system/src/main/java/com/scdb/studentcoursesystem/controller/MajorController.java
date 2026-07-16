package com.scdb.studentcoursesystem.controller;
import com.scdb.studentcoursesystem.entity.Major;
import com.scdb.studentcoursesystem.entity.Result;
import com.scdb.studentcoursesystem.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/department/major")
public class MajorController {
    @Autowired
    private MajorService majorService;

    // 添加专业
    @PostMapping("/add")
    public Result<?> addMajor(@RequestBody Major major) {
        try {
            boolean flag = majorService.addMajor(major);
            return flag ? Result.success("添加专业成功") : Result.error("添加专业失败");
        } catch (Exception e) {
            return Result.error("添加专业异常：" + e.getMessage());
        }
    }

    // 查询所有专业
    @GetMapping("/all")
    public Result<List<Major>> getAllMajors() {
        List<Major> majors = majorService.findAllMajors();
        return Result.success(majors);
    }

    // 根据院系查专业
    @GetMapping("/list")
    public Result<List<Major>> getMajorByDept(@RequestParam String deptName) {
        List<Major> majors = majorService.findByDeptName(deptName);
        return Result.success(majors);
    }

    // 新增：删除专业接口（解决删除功能404）
    @DeleteMapping("/delete")
    public Result<?> deleteMajor(@RequestParam String majorName) {
        try {
            boolean flag = majorService.deleteMajor(majorName);
            return flag ? Result.success("删除专业成功") : Result.error("删除专业失败");
        } catch (Exception e) {
            return Result.error("删除专业异常：" + e.getMessage());
        }
    }

    // 新增：修改专业接口（解决修改功能404）
    @PutMapping("/update")
    public Result<?> updateMajor(@RequestBody Map<String, String> params) {
        try {
            // 校验参数非空
            String oldMajorName = params.get("oldMajorName");
            String newMajorName = params.get("newMajorName");
            String deptName = params.get("deptName");

            if (oldMajorName == null || oldMajorName.trim().isEmpty() ||
                    newMajorName == null || newMajorName.trim().isEmpty() ||
                    deptName == null || deptName.trim().isEmpty()) {
                return Result.error("参数不能为空！");
            }

            boolean flag = majorService.updateMajor(oldMajorName.trim(), newMajorName.trim(), deptName.trim());
            return flag ? Result.success("修改专业成功") : Result.error("修改专业失败");
        } catch (Exception e) {
            return Result.error("修改专业异常：" + e.getMessage());
        }
    }
}