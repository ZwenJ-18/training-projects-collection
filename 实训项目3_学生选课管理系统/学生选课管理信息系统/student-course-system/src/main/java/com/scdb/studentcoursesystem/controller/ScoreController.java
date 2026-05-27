package com.scdb.studentcoursesystem.controller;

import com.scdb.studentcoursesystem.entity.Result;
import com.scdb.studentcoursesystem.entity.Score;
import com.scdb.studentcoursesystem.entity.vo.ScoreCourseVO;
import com.scdb.studentcoursesystem.service.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/score")
public class ScoreController {

    private static final Logger log = LoggerFactory.getLogger(ScoreController.class);
    private final ScoreService scoreService;

    @Autowired
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/list")
    public Result<List<Score>> findAll() {
        try {
            List<Score> scores = scoreService.findAllScores();
            return Result.success("查询成功", scores);
        } catch (Exception e) {
            log.error("查询所有成绩接口异常", e);
            e.printStackTrace();
            return Result.error("查询所有成绩失败：" + e.getMessage());
        }
    }

    @GetMapping("/{sno}")
    public Result<List<Score>> findBySno(@PathVariable String sno) {
        try {
            if (sno == null || sno.trim().isEmpty()) {
                return Result.error("学号不能为空");
            }
            List<Score> scores = scoreService.findScoreBySno(sno);
            return Result.success("查询成功", scores);
        } catch (Exception e) {
            log.error("根据学号{}查询成绩接口异常", sno, e);
            e.printStackTrace();
            return Result.error("查询成绩失败：" + e.getMessage());
        }
    }

    @GetMapping("/student/{sno}")
    public Result<List<ScoreCourseVO>> findScoreWithCourseBySno(@PathVariable String sno) {
        try {
            if (sno == null || sno.trim().isEmpty()) {
                log.warn("查询成绩+课程接口：学号为空");
                return Result.error("学号不能为空");
            }
            List<ScoreCourseVO> scoreList = scoreService.getScoreWithCourseBySno(sno.trim());
            if (scoreList == null || scoreList.isEmpty()) {
                log.info("学号{}暂无成绩数据", sno);
                return Result.success("暂无该学生成绩数据", scoreList == null ? List.of() : scoreList);
            }
            log.info("学号{}成绩查询成功，共{}条数据", sno, scoreList.size());
            return Result.success("查询成功", scoreList);
        } catch (Exception e) {
            log.error("根据学号{}查询成绩+课程接口异常", sno, e);
            e.printStackTrace();
            return Result.error("查询成绩失败：" + e.getCause() + " | " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public Result<Boolean> addScore(@RequestBody Score score) {
        try {
            if (score == null || score.getSno() == null || score.getCno() == null) {
                return Result.error("学号/课程号不能为空");
            }
            // 补充默认学期（适配sct表）
            if (score.getSemester() == null || score.getSemester().trim().isEmpty()) {
                score.setSemester("2024-2025-1");
            }
            boolean flag = scoreService.addScore(score);
            return flag ? Result.success(true) : Result.error("添加成绩失败：该成绩记录已存在");
        } catch (Exception e) {
            log.error("添加成绩接口异常", e);
            e.printStackTrace();
            return Result.error("添加成绩失败：" + e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<Boolean> updateScore(@RequestBody Score score) {
        try {
            if (score == null || score.getSno() == null || score.getCno() == null) {
                return Result.error("学号/课程号不能为空");
            }
            boolean flag = scoreService.updateScore(score);
            return flag ? Result.success(true) : Result.error("修改成绩失败：成绩记录不存在");
        } catch (Exception e) {
            log.error("修改成绩接口异常", e);
            e.printStackTrace();
            return Result.error("修改成绩失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{sno}/{cno}")
    public Result<Boolean> deleteScore(@PathVariable String sno, @PathVariable String cno) {
        try {
            if (sno == null || cno == null || sno.trim().isEmpty() || cno.trim().isEmpty()) {
                return Result.error("学号/课程号不能为空");
            }
            boolean flag = scoreService.deleteScore(sno.trim(), cno.trim());
            return flag ? Result.success(true) : Result.error("删除成绩失败：成绩记录不存在");
        } catch (Exception e) {
            log.error("删除成绩接口异常（学号：{}，课程号：{}）", sno, cno, e);
            e.printStackTrace();
            return Result.error("删除成绩失败：" + e.getMessage());
        }
    }

    // 新增：查询课程下的学生成绩
    @GetMapping("/course/{cno}")
    public Result<List<Map<String, Object>>> getScoresByCourse(@PathVariable String cno) {
        try {
            if (cno == null || cno.trim().isEmpty()) {
                log.warn("查询课程成绩接口：课程号为空");
                return Result.error("课程号不能为空");
            }
            List<Map<String, Object>> scoreList = scoreService.findScoresByCourse(cno.trim());
            return Result.success("查询成功", scoreList == null ? List.of() : scoreList);
        } catch (Exception e) {
            log.error("查询课程{}成绩接口异常", cno, e);
            e.printStackTrace();
            return Result.error("查询成绩失败：" + e.getMessage());
        }
    }

    // 新增：批量保存成绩（适配grade字段 + 补充semester，增强校验）
    @PostMapping("/batchSave")
    public Result<Boolean> batchSaveScore(@RequestBody List<Score> scoreList) {
        try {
            if (scoreList == null || scoreList.isEmpty()) {
                return Result.error("请至少传入一条成绩数据！");
            }
            boolean allSuccess = true;
            for (Score score : scoreList) {
                // 1. 校验必填字段（使用原有grade字段，兼容历史逻辑）
                if (score.getGrade() == null || score.getSno() == null || score.getCno() == null) {
                    log.warn("成绩数据无效：学号/课程号/成绩为空，数据：{}", score);
                    allSuccess = false;
                    continue;
                }
                // 2. 补充默认学期（适配sct表）
                if (score.getSemester() == null || score.getSemester().trim().isEmpty()) {
                    score.setSemester("2024-2025-1");
                }
                // 3. 执行新增/更新
                boolean exists = scoreService.checkScoreExists(score.getSno(), score.getCno());
                boolean success = exists ? scoreService.updateScore(score) : scoreService.addScore(score);
                if (!success) {
                    log.warn("成绩保存失败：学号{}，课程号{}", score.getSno(), score.getCno());
                    allSuccess = false;
                }
            }
            return allSuccess ? Result.success("批量保存成绩成功", true) : Result.error("部分成绩数据无效，保存失败");
        } catch (Exception e) {
            log.error("批量保存成绩接口异常", e);
            e.printStackTrace();
            return Result.error("批量保存成绩失败：" + e.getMessage());
        }
    }
}