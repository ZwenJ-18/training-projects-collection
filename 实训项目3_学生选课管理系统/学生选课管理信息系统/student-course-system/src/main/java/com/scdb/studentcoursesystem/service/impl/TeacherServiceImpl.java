package com.scdb.studentcoursesystem.service.impl;

import com.scdb.studentcoursesystem.entity.Teacher;
import com.scdb.studentcoursesystem.mapper.TeacherMapper;
import com.scdb.studentcoursesystem.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherMapper teacherMapper;

    @Autowired
    public TeacherServiceImpl(TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    @Override
    public List<Teacher> findAllTeachers() {
        return teacherMapper.selectAll();
    }

    @Override
    public Teacher findTeacherByTno(String tno) {
        return teacherMapper.selectByTno(tno);
    }

    @Override
    public boolean addTeacher(Teacher teacher) {
        // 先校验教工号是否存在
        if (teacherMapper.selectByTno(teacher.getTno()) != null) {
            return false;
        }
        return teacherMapper.insert(teacher) > 0;
    }

    @Override
    public boolean updateTeacher(Teacher teacher) {
        return teacherMapper.updateById(teacher) > 0;
    }

    @Override
    public boolean deleteTeacher(String tno) {
        return teacherMapper.deleteByTno(tno) > 0;
    }

    @Override
    public Teacher findTeacherWithCourses(String tno) {
        Teacher teacher = teacherMapper.selectTeacherWithCourses(tno);
        // 空值兼容：避免前端课程列表渲染报错
        if (teacher != null) {
            if (teacher.getCno1() == null) {
                teacher.setCno1("");
                teacher.setCname1("");
            }
            if (teacher.getCno2() == null) {
                teacher.setCno2("");
                teacher.setCname2("");
            }
            if (teacher.getCno3() == null) {
                teacher.setCno3("");
                teacher.setCname3("");
            }
        }
        return teacher;
    }
}