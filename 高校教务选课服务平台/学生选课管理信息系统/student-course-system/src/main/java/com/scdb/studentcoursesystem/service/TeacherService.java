package com.scdb.studentcoursesystem.service;

import com.scdb.studentcoursesystem.entity.Teacher;
import java.util.List;

public interface TeacherService {
    List<Teacher> findAllTeachers();
    Teacher findTeacherByTno(String tno);
    boolean addTeacher(Teacher teacher);
    boolean updateTeacher(Teacher teacher);
    boolean deleteTeacher(String tno);
    Teacher findTeacherWithCourses(String tno);
}