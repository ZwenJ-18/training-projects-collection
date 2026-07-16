package com.scdb.studentcoursesystem.service.impl;
import com.scdb.studentcoursesystem.entity.Major;
import com.scdb.studentcoursesystem.mapper.MajorMapper;
import com.scdb.studentcoursesystem.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {
    @Autowired
    private MajorMapper majorMapper;

    @Override
    public boolean addMajor(Major major) {
        return majorMapper.insertMajor(major) > 0;
    }

    @Override
    public List<Major> findAllMajors() {
        return majorMapper.findAll();
    }

    @Override
    public List<Major> findByDeptName(String deptName) {
        return majorMapper.findByDeptName(deptName);
    }

    @Override
    public boolean deleteMajor(String majorName) {
        return majorMapper.deleteMajor(majorName) > 0;
    }

    @Override
    public boolean updateMajor(String oldMajorName, String newMajorName, String deptName) {
        return majorMapper.updateMajor(oldMajorName, newMajorName, deptName) > 0;
    }
}