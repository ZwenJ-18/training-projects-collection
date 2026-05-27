package com.scdb.studentcoursesystem.mapper;

import com.scdb.studentcoursesystem.entity.Course;
import com.scdb.studentcoursesystem.entity.vo.CourseReportVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper // 核心添加：MyBatis接口必须加@Mapper注解
public interface CourseMapper {
    // 新增课程
    @Insert("INSERT INTO course(cno, cname, cpno, ccredit, ctime, cdept, teacher) VALUES (#{cno}, #{cname}, #{cpno}, #{ccredit}, #{ctime}, #{cdept}, #{teacher})")
    int insert(Course course);

    // 查询所有课程
    @Select("SELECT * FROM course")
    List<Course> selectAll();

    // 根据课程号查询（精准匹配，修复查询逻辑）
    @Select("SELECT * FROM course WHERE cno = #{cno}")
    Course selectByCno(String cno);

    // 根据课程号删除
    @Delete("DELETE FROM course WHERE cno = #{cno}")
    int deleteByCno(String cno);

    // 修改课程
    @Update("UPDATE course SET cname=#{cname},cpno=#{cpno},ccredit=#{ccredit},ctime=#{ctime},cdept=#{cdept},teacher=#{teacher} WHERE cno=#{cno}")
    int update(Course course);

    // ---------------------- 报表专用方法（移除default，改为普通方法+Service层实现）----------------------
    // 注：MyBatis Mapper接口不推荐写default方法，移到CourseService中实现
    @Select("SELECT * FROM course WHERE 1=1 " +
            "AND (#{cno} IS NULL OR #{cno} = '' OR cno LIKE CONCAT('%', #{cno}, '%')) " +
            "AND (#{cname} IS NULL OR #{cname} = '' OR cname LIKE CONCAT('%', #{cname}, '%')) " +
            "AND (#{cdept} IS NULL OR #{cdept} = '' OR cdept = #{cdept})")
    List<Course> selectCourseReport(String cno, String cname, String cdept);

    @Select("SELECT COUNT(*) FROM course WHERE 1=1 " +
            "AND (#{cno} IS NULL OR #{cno} = '' OR cno LIKE CONCAT('%', #{cno}, '%')) " +
            "AND (#{cname} IS NULL OR #{cname} = '' OR cname LIKE CONCAT('%', #{cname}, '%')) " +
            "AND (#{cdept} IS NULL OR #{cdept} = '' OR cdept = #{cdept})")
    Integer countCourseReport(String cno, String cname, String cdept);

    @Select("SELECT COUNT(*) FROM course WHERE cdept = #{cdept}")
    Integer countByCdept(String cdept);
}