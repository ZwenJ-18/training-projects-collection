package com.scdb.studentcoursesystem.service.impl;

import com.scdb.studentcoursesystem.entity.Student;
import com.scdb.studentcoursesystem.mapper.StudentMapper;
import com.scdb.studentcoursesystem.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    // 核心替换：使用MyBatis的StudentMapper（而非JdbcTemplate）
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> findAllStudents() {
        try {
            List<Student> students = studentMapper.findAll();
            log.info("查询所有学生完成，共{}条数据", students.size());
            return students;
        } catch (Exception e) {
            log.error("【学生服务】查询所有学生失败", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Student findStudentBySno(String sno) {
        if (!StringUtils.hasText(sno)) {
            log.warn("【学生服务】根据学号查询学生失败：学号为空");
            return null;
        }
        try {
            Student student = studentMapper.findBySno(sno.trim());
            if (student == null) {
                log.info("【学生服务】学号{}不存在", sno);
            } else {
                log.info("【学生服务】查询学号{}学生信息成功", sno);
            }
            return student;
        } catch (Exception e) {
            log.error("【学生服务】根据学号{}查询学生失败", sno, e);
            return null;
        }
    }

    @Override
    public boolean addStudent(Student student) {
        if (student == null || !StringUtils.hasText(student.getSno()) || !StringUtils.hasText(student.getSname())) {
            log.warn("【学生服务】添加学生失败：参数为空或学号/姓名缺失");
            return false;
        }
        try {
            // 调用Mapper插入（自动填充所有字段）
            int rows = studentMapper.addStudent(student);
            boolean success = rows > 0;
            if (success) {
                log.info("【学生服务】添加学生成功（学号：{}）", student.getSno());
            } else {
                log.warn("【学生服务】添加学生失败（学号：{}）：无数据插入", student.getSno());
            }
            return success;
        } catch (Exception e) {
            log.error("【学生服务】添加学生失败（学号：{}）", student.getSno(), e);
            return false;
        }
    }

    @Override
    public boolean updateStudent(Student student) {
        if (student == null || !StringUtils.hasText(student.getSno())) {
            log.warn("【学生服务】修改学生失败：参数为空或学号缺失");
            return false;
        }
        try {
            int rows = studentMapper.updateStudent(student);
            boolean success = rows > 0;
            if (success) {
                log.info("【学生服务】修改学号{}学生信息成功", student.getSno());
            } else {
                log.warn("【学生服务】修改学号{}学生失败：记录不存在", student.getSno());
            }
            return success;
        } catch (Exception e) {
            log.error("【学生服务】修改学号{}学生失败", student.getSno(), e);
            return false;
        }
    }

    @Override
    public boolean deleteStudent(String sno) {
        if (!StringUtils.hasText(sno)) {
            log.warn("【学生服务】删除学生失败：学号为空");
            return false;
        }
        try {
            int rows = studentMapper.deleteStudent(sno.trim());
            boolean success = rows > 0;
            if (success) {
                log.info("【学生服务】删除学号{}学生成功", sno);
            } else {
                log.warn("【学生服务】删除学号{}学生失败：记录不存在", sno);
            }
            return success;
        } catch (Exception e) {
            log.error("【学生服务】删除学号{}学生失败", sno, e);
            return false;
        }
    }

    @Override
    public int countStudentByDept(String dno) {
        if (!StringUtils.hasText(dno)) {
            log.warn("【学生服务】统计院系学生数失败：院系编号为空");
            return 0;
        }
        try {
            Integer count = studentMapper.countByDept(dno.trim());
            log.info("【学生服务】院系{}学生数：{}", dno, count);
            return count == null ? 0 : count;
        } catch (Exception e) {
            log.error("【学生服务】统计院系{}学生数失败", dno, e);
            return 0;
        }
    }

    @Override
    public List<Student> findStudentsByCourse(String cno) {
        if (!StringUtils.hasText(cno)) {
            log.warn("【学生服务】查询课程学生失败：课程号为空");
            return Collections.emptyList();
        }
        try {
            String validCno = cno.trim();
            List<Student> students = studentMapper.findStudentsByCourse(validCno);
            log.info("【学生服务】课程号{}选课学生数：{}", validCno, students.size());
            return students;
        } catch (Exception e) {
            log.error("【学生服务】查询课程号{}选课学生失败", cno, e);
            return Collections.emptyList();
        }
    }
}