package com.scdb.studentcoursesystem.mapper;
import com.scdb.studentcoursesystem.entity.Student;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface StudentMapper {
    // 新增学生（含班级、专业）
    @Insert("INSERT INTO student(sno, sname, ssex, sage, class_name, major, sdept, dno) " +
            "VALUES(#{sno}, #{sname}, #{ssex}, #{sage}, #{className}, #{major}, #{sdept}, #{dno})")
    int addStudent(Student student);

    @Select("SELECT * FROM student")
    List<Student> findAll();

    @Select("SELECT * FROM student WHERE sno = #{sno}")
    Student findBySno(String sno);

    // 修正update语句（匹配实体类字段）
    @Update("UPDATE student SET sname=#{sname}, ssex=#{ssex}, sage=#{sage}, class_name=#{className}, major=#{major}, sdept=#{sdept}, dno=#{dno} WHERE sno=#{sno}")
    int updateStudent(Student student);

    @Delete("DELETE FROM student WHERE sno = #{sno}")
    int deleteStudent(String sno);

    @Select("SELECT COUNT(*) FROM student WHERE dno = #{dno}")
    Integer countByDept(String dno);

    /**
     * 查询选该课程的所有学生
     */
    @Select("SELECT s.* FROM student s WHERE s.sno IN (SELECT sno FROM sct WHERE cno = #{cno})")
    List<Student> findStudentsByCourse(@Param("cno") String cno);
}