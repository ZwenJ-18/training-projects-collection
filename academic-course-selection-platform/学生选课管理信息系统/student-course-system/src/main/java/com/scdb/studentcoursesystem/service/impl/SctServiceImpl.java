package com.scdb.studentcoursesystem.service.impl;

import com.scdb.studentcoursesystem.entity.Sct;
import com.scdb.studentcoursesystem.mapper.SctMapper;
import com.scdb.studentcoursesystem.service.SctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SctServiceImpl implements SctService {

    @Autowired
    private SctMapper sctMapper;

    @Override
    public boolean addSct(Sct sct) {
        // 先判断是否已选
        if (sctMapper.selectBySnoAndCno(sct.getSno(), sct.getCno()) != null) {
            return false;
        }
        return sctMapper.insert(sct) > 0;
    }

    @Override
    public List<Sct> getAllScts() {
        return sctMapper.selectAllSctEntity();
    }

    @Override
    public List<Map<String, Object>> getAllSctsWithName() {
        // 过滤admin无效数据
        return sctMapper.selectAll().stream()
                .filter(item -> !"admin".equals(item.get("sno")))
                .collect(Collectors.toList());
    }

    // 新增：按学号查询选课信息（关联学生/课程名）
    @Override
    public List<Map<String, Object>> getSctBySno(String sno) {
        return sctMapper.selectBySno(sno);
    }

    @Override
    public Sct getSctBySnoAndCno(String sno, String cno) {
        return sctMapper.selectBySnoAndCno(sno, cno);
    }

    @Override
    public boolean deleteSctBySnoAndCno(String sno, String cno) {
        return sctMapper.deleteBySnoAndCno(sno, cno) > 0;
    }

    @Override
    public boolean updateSct(Sct sct) {
        return sctMapper.update(sct) > 0;
    }

    // 实现：报表所需 - 统计某课程的选课人数
    @Override
    public int countSctByCno(String cno) {
        return sctMapper.countByCno(cno);
    }
}