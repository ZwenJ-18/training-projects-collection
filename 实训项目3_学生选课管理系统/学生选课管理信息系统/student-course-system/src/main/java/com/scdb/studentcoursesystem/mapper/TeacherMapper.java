package com.scdb.studentcoursesystem.mapper;

import com.scdb.studentcoursesystem.entity.Teacher;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface TeacherMapper {
    // 查询所有教师
    @Select("SELECT tno, tname, tsex, tage, teb, tpt, cno1, cno2, cno3 FROM teacher")
    List<Teacher> selectAll();

    // 根据教工号查询
    @Select("SELECT tno, tname, tsex, tage, teb, tpt, cno1, cno2, cno3 FROM teacher WHERE tno = #{tno}")
    Teacher selectByTno(String tno);

    // 添加教师
    @Insert("INSERT INTO teacher (tno, tname, tsex, tage, teb, tpt, cno1, cno2, cno3) " +
            "VALUES (#{tno}, #{tname}, #{tsex}, #{tage}, #{teb}, #{tpt}, #{cno1}, #{cno2}, #{cno3})")
    int insert(Teacher teacher);

    // 修改教师
    @Update("UPDATE teacher SET tname=#{tname}, tsex=#{tsex}, tage=#{tage}, teb=#{teb}, tpt=#{tpt}, cno1=#{cno1}, cno2=#{cno2}, cno3=#{cno3} WHERE tno=#{tno}")
    int updateById(Teacher teacher);

    // 删除教师
    @Delete("DELETE FROM teacher WHERE tno = #{tno}")
    int deleteByTno(String tno);

    // 教师关联课程查询（关联course表获取课程名称）
    @Select("SELECT t.*, c1.cname AS cname1, c2.cname AS cname2, c3.cname AS cname3 " +
            "FROM teacher t " +
            "LEFT JOIN course c1 ON t.cno1 = c1.cno " +
            "LEFT JOIN course c2 ON t.cno2 = c2.cno " +
            "LEFT JOIN course c3 ON t.cno3 = c3.cno " +
            "WHERE t.tno = #{tno}")
    Teacher selectTeacherWithCourses(String tno);
}