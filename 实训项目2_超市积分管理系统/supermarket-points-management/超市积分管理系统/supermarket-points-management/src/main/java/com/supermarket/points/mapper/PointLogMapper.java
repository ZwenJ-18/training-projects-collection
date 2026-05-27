package com.supermarket.points.mapper;

import com.supermarket.points.model.entity.PointLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface PointLogMapper {
    // 原有新增日志的方法（保留）
    @Insert("INSERT INTO point_log (user_id, username, commodity_id, commodity_name, point_change, type) " +
            "VALUES (#{userId}, #{username}, #{commodityId}, #{commodityName}, #{pointChange}, #{type})")
    int insert(PointLog pointLog);

    // 原有：按用户ID查日志（保留，供用户端使用）
    @Select("SELECT * FROM point_log WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<PointLog> selectByUserId(Integer userId);

    // 新增：查询所有用户的积分日志（管理员端核心）
    @Select("SELECT * FROM point_log ORDER BY create_time DESC")
    List<PointLog> selectAll();
}