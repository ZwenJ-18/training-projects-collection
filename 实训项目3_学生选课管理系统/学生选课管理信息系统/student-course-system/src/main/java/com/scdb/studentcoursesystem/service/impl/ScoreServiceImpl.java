package com.scdb.studentcoursesystem.service.impl;

import com.scdb.studentcoursesystem.entity.Score;
import com.scdb.studentcoursesystem.entity.vo.ScoreCourseVO;
import com.scdb.studentcoursesystem.mapper.ScoreMapper;
import com.scdb.studentcoursesystem.service.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 成绩服务实现类（修复：完善异常捕获/空值处理/日志输出）
 */
@Service
public class ScoreServiceImpl implements ScoreService {

    // 日志组件（优化：添加业务标识，便于排查）
    private static final Logger log = LoggerFactory.getLogger(ScoreServiceImpl.class);
    private final ScoreMapper scoreMapper;

    // 构造器注入（推荐方式，避免字段注入漏洞）
    @Autowired
    public ScoreServiceImpl(ScoreMapper scoreMapper) {
        this.scoreMapper = scoreMapper;
    }

    /**
     * 查询所有成绩（sc表）
     * 修复：异常时返回空列表，避免前端NPE
     */
    @Override
    public List<Score> findAllScores() {
        try {
            List<Score> scoreList = scoreMapper.findAll();
            log.info("查询所有成绩完成，共{}条数据", scoreList.size());
            return scoreList;
        } catch (Exception e) {
            log.error("【成绩服务】查询所有成绩失败", e);
            return Collections.emptyList();
        }
    }

    /**
     * 根据学号查询成绩（sc表）
     * 修复：空学号校验 + 异常兜底
     */
    @Override
    public List<Score> findScoreBySno(String sno) {
        // 前置校验：学号为空直接返回空列表
        if (!StringUtils.hasText(sno)) {
            log.warn("【成绩服务】根据学号查询成绩失败：学号为空");
            return Collections.emptyList();
        }
        try {
            List<Score> scoreList = scoreMapper.findBySno(sno.trim());
            log.info("【成绩服务】根据学号{}查询成绩完成，共{}条数据", sno, scoreList.size());
            return scoreList;
        } catch (Exception e) {
            log.error("【成绩服务】根据学号{}查询成绩失败", sno, e);
            return Collections.emptyList();
        }
    }

    /**
     * 核心修复：根据学号查询成绩+课程+学期完整信息
     * 优化：
     * 1. 严格的学号非空校验（使用Spring工具类）
     * 2. 结果null时返回空列表，避免前端NPE
     * 3. 详细日志便于定位数据问题
     */
    @Override
    public List<ScoreCourseVO> getScoreWithCourseBySno(String sno) {
        // 前置校验：学号为空/空白字符串直接返回空列表
        if (!StringUtils.hasText(sno)) {
            log.warn("【成绩服务】查询成绩+课程信息失败：学号为空或空白");
            return Collections.emptyList();
        }
        try {
            String validSno = sno.trim();
            List<ScoreCourseVO> scoreVOList = scoreMapper.selectScoreWithCourseBySno(validSno);
            // 兜底：Mapper返回null时转为空列表
            List<ScoreCourseVO> result = scoreVOList == null ? Collections.emptyList() : scoreVOList;
            log.info("【成绩服务】根据学号{}查询成绩+课程信息完成，共{}条数据", validSno, result.size());
            return result;
        } catch (Exception e) {
            log.error("【成绩服务】根据学号{}查询成绩+课程信息失败", sno, e);
            return Collections.emptyList();
        }
    }

    /**
     * 添加成绩（sc表）
     * 修复：空值校验 + 异常返回false
     */
    @Override
    public boolean addScore(Score score) {
        // 前置校验：参数为空/学号/课程号为空直接返回false
        if (score == null || !StringUtils.hasText(score.getSno()) || !StringUtils.hasText(score.getCno())) {
            log.warn("【成绩服务】添加成绩失败：参数为空或学号/课程号缺失");
            return false;
        }
        try {
            int affectRows = scoreMapper.addScore(score);
            boolean success = affectRows > 0;
            if (success) {
                log.info("【成绩服务】添加成绩成功（学号：{}，课程号：{}）", score.getSno(), score.getCno());
            } else {
                log.warn("【成绩服务】添加成绩失败：无数据插入（可能已存在该记录）");
            }
            return success;
        } catch (Exception e) {
            log.error("【成绩服务】添加成绩失败（学号：{}，课程号：{}）", score.getSno(), score.getCno(), e);
            return false;
        }
    }

    /**
     * 修改成绩（sc表）
     * 修复：参数校验 + 异常处理
     */
    @Override
    public boolean updateScore(Score score) {
        if (score == null || !StringUtils.hasText(score.getSno()) || !StringUtils.hasText(score.getCno())) {
            log.warn("【成绩服务】修改成绩失败：参数为空或学号/课程号缺失");
            return false;
        }
        try {
            int affectRows = scoreMapper.updateScore(score);
            boolean success = affectRows > 0;
            if (success) {
                log.info("【成绩服务】修改成绩成功（学号：{}，课程号：{}）", score.getSno(), score.getCno());
            } else {
                log.warn("【成绩服务】修改成绩失败：无数据更新（可能记录不存在）");
            }
            return success;
        } catch (Exception e) {
            log.error("【成绩服务】修改成绩失败（学号：{}，课程号：{}）", score.getSno(), score.getCno(), e);
            return false;
        }
    }

    /**
     * 删除成绩（sc表）
     * 修复：空值校验 + 详细日志
     */
    @Override
    public boolean deleteScore(String sno, String cno) {
        if (!StringUtils.hasText(sno) || !StringUtils.hasText(cno)) {
            log.warn("【成绩服务】删除成绩失败：学号或课程号为空");
            return false;
        }
        try {
            String validSno = sno.trim();
            String validCno = cno.trim();
            int affectRows = scoreMapper.deleteScore(validSno, validCno);
            boolean success = affectRows > 0;
            if (success) {
                log.info("【成绩服务】删除成绩成功（学号：{}，课程号：{}）", validSno, validCno);
            } else {
                log.warn("【成绩服务】删除成绩失败：无数据删除（可能记录不存在）");
            }
            return success;
        } catch (Exception e) {
            log.error("【成绩服务】删除成绩失败（学号：{}，课程号：{}）", sno, cno, e);
            return false;
        }
    }

    /**
     * 补=全：根据课程号查询学生成绩（适配录入成绩页面）
     */
    @Override
    public List<Map<String, Object>> findScoresByCourse(String cno) {
        // 前置校验：课程号为空直接返回空列表
        if (!StringUtils.hasText(cno)) {
            log.warn("【成绩服务】查询课程成绩失败：课程号为空");
            return Collections.emptyList();
        }
        try {
            String validCno = cno.trim();
            List<Map<String, Object>> scoreList = scoreMapper.selectScoresByCourse(validCno);
            // 兜底：Mapper返回null时转为空列表
            List<Map<String, Object>> result = scoreList == null ? Collections.emptyList() : scoreList;
            log.info("【成绩服务】根据课程号{}查询成绩完成，共{}条数据", validCno, result.size());
            return result;
        } catch (Exception e) {
            log.error("【成绩服务】根据课程号{}查询成绩失败", cno, e);
            return Collections.emptyList();
        }
    }

    /**
     * 补=全：校验成绩记录是否存在（用于批量保存时判断新增/更新）
     */
    @Override
    public boolean checkScoreExists(String sno, String cno) {
        // 前置校验：学号/课程号为空直接返回false
        if (!StringUtils.hasText(sno) || !StringUtils.hasText(cno)) {
            log.warn("【成绩服务】校验成绩记录失败：学号或课程号为空");
            return false;
        }
        try {
            String validSno = sno.trim();
            String validCno = cno.trim();
            Integer count = scoreMapper.countScoreBySnoAndCno(validSno, validCno);
            boolean exists = count != null && count > 0;
            log.info("【成绩服务】校验成绩记录（学号：{}，课程号：{}）：{}", validSno, validCno, exists ? "存在" : "不存在");
            return exists;
        } catch (Exception e) {
            log.error("【成绩服务】校验成绩记录失败（学号：{}，课程号：{}）", sno, cno, e);
            return false;
        }
    }
}