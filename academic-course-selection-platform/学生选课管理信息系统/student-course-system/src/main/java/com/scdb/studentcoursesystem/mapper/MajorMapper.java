package com.scdb.studentcoursesystem.mapper;
import com.scdb.studentcoursesystem.entity.Major;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MajorMapper {
    @Insert("INSERT INTO major(major_name, dept_name) VALUES(#{majorName}, #{deptName})")
    int insertMajor(Major major);

    @Select("SELECT * FROM major")
    List<Major> findAll();

    // 支持空值查询全部专业
    @Select("<script>" +
            "SELECT * FROM major " +
            "<where>" +
            "   <if test='deptName != null and deptName != \"\"'>" +
            "       dept_name = #{deptName}" +
            "   </if>" +
            "</where>" +
            "</script>")
    List<Major> findByDeptName(@Param("deptName") String deptName);

    @Delete("DELETE FROM major WHERE major_name = #{majorName}")
    int deleteMajor(String majorName);

    @Update("UPDATE major SET major_name = #{newMajorName}, dept_name = #{deptName} WHERE major_name = #{oldMajorName}")
    int updateMajor(@Param("oldMajorName") String oldMajorName,
                    @Param("newMajorName") String newMajorName,
                    @Param("deptName") String deptName);
}