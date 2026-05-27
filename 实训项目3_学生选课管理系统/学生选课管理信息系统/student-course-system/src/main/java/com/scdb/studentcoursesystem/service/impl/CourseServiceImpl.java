package com.scdb.studentcoursesystem.service.impl;

import com.scdb.studentcoursesystem.entity.Course;
import com.scdb.studentcoursesystem.mapper.CourseMapper;
import com.scdb.studentcoursesystem.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public boolean addCourse(Course course) {
        return courseMapper.insert(course) > 0;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseMapper.selectAll();
    }

    @Override
    public Course getCourseByCno(String cno) {
        return courseMapper.selectByCno(cno);
    }

    @Override
    public boolean deleteCourseByCno(String cno) {
        return courseMapper.deleteByCno(cno) > 0;
    }



    @Override
    public boolean updateCourse(Course course) {
        Course existing = getCourseByCno(course.getCno());
        if (existing == null) {
            return false;
        }
        return courseMapper.update(course) > 0;
    }
}