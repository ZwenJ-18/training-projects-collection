package com.scdb.studentcoursesystem.mapper;

import com.scdb.studentcoursesystem.entity.Department;
import com.scdb.studentcoursesystem.entity.vo.DepartmentReportVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface DepartmentMapper {
    // 新增院系
    @Insert("INSERT INTO department(dno, dname, dmanager) VALUES (#{dno}, #{dname}, #{dmanager})")
    int insert(Department department);

    // 查询所有院系
    @Select("SELECT * FROM department")
    List<Department> selectAll();

    // 根据编号查询院系
    @Select("SELECT * FROM department WHERE dno = #{dno}")
    Department selectByDno(String dno);

    // 新增：根据名称查询院系
    @Select("SELECT * FROM department WHERE dname = #{dname}")
    Department selectByDname(String dname);

    // 根据编号删除院系
    @Delete("DELETE FROM department WHERE dno = #{dno}")
    int deleteByDno(String dno);

    // 修改院系
    @Update("UPDATE department SET dname = #{dname}, dmanager = #{dmanager} WHERE dno = #{dno}")
    int update(Department dept);

    // ---------------------- 新增：根据院系名称查dno（用于自动同步） ----------------------
    @Select("SELECT dno FROM department WHERE dname = #{dname}")
    String getDnoByDname(String dname);

    // ---------------------- 联表统计院系报表数据（解决学生数为零+字符集冲突问题） ----------------------
    @Select("SELECT d.dno, d.dname, d.dmanager, COUNT(s.sno) AS studentNum, COUNT(DISTINCT c.cno) AS courseNum " +
            "FROM department d " +
            "LEFT JOIN student s ON d.dno COLLATE utf8mb4_general_ci = s.dno COLLATE utf8mb4_general_ci " +
            "LEFT JOIN course c ON d.dname COLLATE utf8mb4_general_ci = c.cdept COLLATE utf8mb4_general_ci " +
            "GROUP BY d.dno, d.dname, d.dmanager")
    List<DepartmentReportVO> selectDeptReportWithCount();
}