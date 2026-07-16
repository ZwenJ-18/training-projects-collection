package com.scdb.studentcoursesystem.service;

import com.scdb.studentcoursesystem.entity.Score;
import com.scdb.studentcoursesystem.entity.vo.ScoreCourseVO;

import java.util.List;
import java.util.Map;

public interface ScoreService {
    List<Score> findAllScores();
    List<Score> findScoreBySno(String sno);
    List<ScoreCourseVO> getScoreWithCourseBySno(String sno);
    boolean addScore(Score score);
    boolean updateScore(Score score);
    boolean deleteScore(String sno, String cno);
    // 新增接口
    List<Map<String, Object>> findScoresByCourse(String cno);
    boolean checkScoreExists(String sno, String cno);
}