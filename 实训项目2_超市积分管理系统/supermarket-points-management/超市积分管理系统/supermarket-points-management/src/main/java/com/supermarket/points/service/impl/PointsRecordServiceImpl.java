package com.supermarket.points.service.impl;

import com.supermarket.points.mapper.PointsRecordMapper;
import com.supermarket.points.model.entity.PointsRecord;
import com.supermarket.points.model.vo.PageVO;
import com.supermarket.points.service.PointsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PointsRecordServiceImpl implements PointsRecordService {
    @Autowired
    private PointsRecordMapper pointsRecordMapper;

    @Override
    @Transactional
    public boolean addPointsRecord(PointsRecord record) {
        record.setCreateTime(new Date());
        return pointsRecordMapper.insert(record) > 0;
    }

    @Override
    public PageVO<PointsRecord> getUserPointsRecordPage(Integer userId, Integer pageNum, Integer pageSize) {
        pageNum = (pageNum - 1) * pageSize;
        List<PointsRecord> list = pointsRecordMapper.selectByUserId(userId, pageNum, pageSize);
        Integer total = pointsRecordMapper.selectTotalByUserId(userId);
        PageVO<PointsRecord> pageVO = new PageVO<>();
        pageVO.setList(list);
        pageVO.setTotal(total);
        pageVO.setPageNum(pageNum / pageSize + 1);
        pageVO.setPageSize(pageSize);
        pageVO.setTotalPage((total + pageSize - 1) / pageSize);
        return pageVO;
    }
}