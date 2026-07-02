package com.scdb.studentcoursesystem.service.impl;

import com.scdb.studentcoursesystem.entity.Course;
import com.scdb.studentcoursesystem.mapper.CourseMapper;
import com.scdb.studentcoursesystem.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    @CacheEvict(value = "courses", allEntries = true)
    public boolean addCourse(Course course) {
        return courseMapper.insert(course) > 0;
    }

    @Override
    @Cacheable(value = "courses", key = "'allCourses'")
    public List<Course> getAllCourses() {
        return courseMapper.selectAll();
    }

    @Override
    @Cacheable(value = "courses", key = "#cno")
    public Course getCourseByCno(String cno) {
        return courseMapper.selectByCno(cno);
    }

    @Override
    @CacheEvict(value = "courses", allEntries = true)
    public boolean deleteCourseByCno(String cno) {
        return courseMapper.deleteByCno(cno) > 0;
    }

    @Override
    @CacheEvict(value = "courses", allEntries = true)
    public boolean updateCourse(Course course) {
        Course existing = getCourseByCno(course.getCno());
        if (existing == null) {
            return false;
        }
        return courseMapper.update(course) > 0;
    }
}
