package com.scdb.studentcoursesystem.service;

import com.scdb.studentcoursesystem.entity.Course;
import java.util.List;

public interface CourseService {
    // 新增课程
    boolean addCourse(Course course);
    // 查询所有课程（缺失）
    List<Course> getAllCourses();
    // 根据课程号查询（缺失）
    Course getCourseByCno(String cno);
    // 根据课程号删除（缺失）
    boolean deleteCourseByCno(String cno);
    // 修改课程
    boolean updateCourse(Course course);
}