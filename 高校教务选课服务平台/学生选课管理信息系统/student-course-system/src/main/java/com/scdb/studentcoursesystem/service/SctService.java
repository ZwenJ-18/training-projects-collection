package com.scdb.studentcoursesystem.service;

import com.scdb.studentcoursesystem.entity.Sct;
import java.util.List;
import java.util.Map;

public interface SctService {
    boolean addSct(Sct sct);
    List<Sct> getAllScts();
    // 过滤admin无效数据，返回纯净学生选课记录
    List<Map<String, Object>> getAllSctsWithName();
    // 按学号查询选课信息（自动过滤admin账号）
    List<Map<String, Object>> getSctBySno(String sno);
    Sct getSctBySnoAndCno(String sno, String cno);
    boolean deleteSctBySnoAndCno(String sno, String cno);
    boolean updateSct(Sct sct);

    // 新增：报表所需 - 查询所有选课记录（兼容ReportController的findAllSct）
    default List<Sct> findAllSct() {
        return getAllScts();
    }

    // 新增：报表所需 - 统计某课程的选课人数
    int countSctByCno(String cno);
}