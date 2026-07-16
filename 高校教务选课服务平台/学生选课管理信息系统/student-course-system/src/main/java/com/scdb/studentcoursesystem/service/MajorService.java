package com.scdb.studentcoursesystem.service;
import com.scdb.studentcoursesystem.entity.Major;
import java.util.List;

public interface MajorService {
    boolean addMajor(Major major);
    List<Major> findAllMajors();
    List<Major> findByDeptName(String deptName);
    boolean deleteMajor(String majorName);
    boolean updateMajor(String oldMajorName, String newMajorName, String deptName);
}