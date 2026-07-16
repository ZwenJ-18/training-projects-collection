package com.scdb.studentcoursesystem.service;

import com.scdb.studentcoursesystem.entity.Student;
import java.util.List;

public interface StudentService {
    List<Student> findAllStudents();
    Student findStudentBySno(String sno);
    boolean addStudent(Student student);
    boolean updateStudent(Student student);
    boolean deleteStudent(String sno);
    int countStudentByDept(String dno);
    // 新增接口
    List<Student> findStudentsByCourse(String cno);

    // 补充：学号重复校验（如果实现类未包含，需在Impl中实现）
    default boolean existsBySno(String sno) {
        return findStudentBySno(sno) != null;
    }
}