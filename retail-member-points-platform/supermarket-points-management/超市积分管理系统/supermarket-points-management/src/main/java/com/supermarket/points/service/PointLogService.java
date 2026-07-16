package com.supermarket.points.service;

import com.supermarket.points.model.entity.PointLog;
import com.supermarket.points.model.vo.PageVO; // 新增：导入分页VO
import java.util.List;

public interface PointLogService {
    // 原有新增日志的方法（保留）
    void addPointLog(PointLog pointLog);

    // 原有：按用户ID查日志（保留，供用户端使用）
    List<PointLog> getLogByUserId(Integer userId);

    // 新增1：查询所有积分日志（不分页，备用）
    List<PointLog> getAllPointLogs();

    // 新增2：分页查询所有积分日志（管理员端核心方法，关键修改：泛型指定为PointLog）
    PageVO<PointLog> getPointLogPage(Integer pageNum, Integer pageSize);
}