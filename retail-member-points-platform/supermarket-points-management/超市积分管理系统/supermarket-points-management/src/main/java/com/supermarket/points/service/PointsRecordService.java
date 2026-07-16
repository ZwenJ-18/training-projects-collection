package com.supermarket.points.service;

import com.supermarket.points.model.entity.PointsRecord;
import com.supermarket.points.model.vo.PageVO;

public interface PointsRecordService {
    // 新增积分记录
    boolean addPointsRecord(PointsRecord record);

    // 分页查询用户积分记录
    PageVO<PointsRecord> getUserPointsRecordPage(Integer userId, Integer pageNum, Integer pageSize);
}