package com.scdb.studentcoursesystem.mapper;

import com.scdb.studentcoursesystem.entity.Sct;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

public interface SctMapper {

    @Insert("INSERT INTO sct(sno, cno, semester, score) VALUES (#{sno}, #{cno}, #{semester}, #{score})")
    int insert(Sct sct);

    @Select("SELECT * FROM sct")
    List<Sct> selectAllSctEntity();

    // 处理score空值，避免前端显示undefined
    @Select("SELECT " +
            "s.sno, s.cno, s.semester, " +
            "IFNULL(s.score, '未录入') AS score, " +
            "IFNULL(st.sname, '未实名') AS studentName, " +
            "IFNULL(c.cname, '未配置课程') AS courseName " +
            "FROM sct s " +
            "LEFT JOIN student st ON s.sno = st.sno " +
            "LEFT JOIN course c ON s.cno = c.cno")
    List<Map<String, Object>> selectAll();

    // 按学号查询选课信息（关联学生/课程名）
    @Select("SELECT " +
            "s.sno, s.cno, s.semester, " +
            "IFNULL(s.score, '未录入') AS score, " +
            "IFNULL(st.sname, '未实名') AS studentName, " +
            "IFNULL(c.cname, '未配置课程') AS courseName " +
            "FROM sct s " +
            "LEFT JOIN student st ON s.sno = st.sno " +
            "LEFT JOIN course c ON s.cno = c.cno " +
            "WHERE s.sno = #{sno}")
    List<Map<String, Object>> selectBySno(@Param("sno") String sno);

    @Select("SELECT * FROM sct WHERE sno = #{sno} AND cno = #{cno}")
    Sct selectBySnoAndCno(@Param("sno") String sno, @Param("cno") String cno);

    @Delete("DELETE FROM sct WHERE sno = #{sno} AND cno = #{cno}")
    int deleteBySnoAndCno(@Param("sno") String sno, @Param("cno") String cno);

    @Update("UPDATE sct SET semester = #{semester}, score = #{score} WHERE sno = #{sno} AND cno = #{cno}")
    int update(Sct sct);

    // 统计某课程的选课人数（报表用）
    @Select("SELECT COUNT(1) FROM sct WHERE cno = #{cno}")
    int countByCno(@Param("cno") String cno);
}