package com.supermarket.points.mapper;

import com.supermarket.points.model.entity.PointsRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PointsRecordMapper {
    // 新增积分记录
    int insert(PointsRecord pointsRecord);

    // 根据用户ID查积分记录（分页）
    List<PointsRecord> selectByUserId(@Param("userId") Integer userId, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    // 查询用户积分记录总数
    Integer selectTotalByUserId(Integer userId);
}