package com.scdb.studentcoursesystem.mapper;

import com.scdb.studentcoursesystem.entity.Score;
import com.scdb.studentcoursesystem.entity.vo.ScoreCourseVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScoreMapper {
    // 全量适配sct表（删除sc表依赖）
    @Select("SELECT sno, cno, score, semester FROM sct")
    List<Score> findAll();

    @Select("SELECT sno, cno, score, semester FROM sct WHERE sno = #{sno}")
    List<Score> findBySno(@Param("sno") String sno);

    @Insert("INSERT INTO sct(sno, cno, score, semester) VALUES(#{sno}, #{cno}, #{score}, #{semester})")
    int addScore(Score score);

    @Update("UPDATE sct SET score=#{score}, semester=#{semester} WHERE sno=#{sno} AND cno=#{cno}")
    int updateScore(Score score);

    @Delete("DELETE FROM sct WHERE sno = #{sno} AND cno = #{cno}")
    int deleteScore(@Param("sno") String sno, @Param("cno") String cno);

    // 关联sct表查询（删除sc表依赖）
    @Select("SELECT " +
            "sct.sno AS sno, " +
            "sct.cno AS cno, " +
            "IFNULL(sct.score, 0) AS score, " +
            "IFNULL(sct.semester, '2024-2025-1') AS semester, " +
            "IFNULL(c.cname, '未知课程') AS cname, " +
            "IFNULL(c.ccredit, 0) AS ccredit, " +
            "IFNULL(st.sname, '未知学生') AS sname " +
            "FROM sct " +
            "LEFT JOIN course c ON sct.cno = c.cno " +
            "LEFT JOIN student st ON sct.sno = st.sno " +
            "WHERE sct.sno = #{sno}")
    List<ScoreCourseVO> selectScoreWithCourseBySno(@Param("sno") String sno);

    // 适配sct表（删除sc表依赖）
    @Select("SELECT s.sno, s.sname, IFNULL(sct.score, '') as score " +
            "FROM student s " +
            "LEFT JOIN sct ON s.sno = sct.sno AND sct.cno = #{cno} " +
            "WHERE s.sno IN (SELECT sno FROM sct WHERE cno = #{cno})")
    List<Map<String, Object>> selectScoresByCourse(@Param("cno") String cno);

    @Select("SELECT COUNT(1) FROM sct WHERE sno = #{sno} AND cno = #{cno}")
    Integer countScoreBySnoAndCno(@Param("sno") String sno, @Param("cno") String cno);
}