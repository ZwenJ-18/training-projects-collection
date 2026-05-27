package com.supermarket.points.mapper;

import com.supermarket.points.model.entity.SysPointsLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysPointsLogMapper {
    // 1. 原有：新增积分日志（保留，用户/管理员写入都用这个）
    int insert(SysPointsLog log);

    // 2. 同步改名：selectByUserId → selectByUserIdPage（分页）
    // 关键：userId参数改为Integer，匹配实体类/业务代码
    List<SysPointsLog> selectByUserIdPage(@Param("userId") Integer userId,
                                          @Param("start") Integer start,
                                          @Param("pageSize") Integer pageSize);
    // 3. 原有：查询用户日志总数（分页配套）
    // 关键：userId参数改为Integer，匹配上面的方法
    Integer selectLogTotalByUserId(Integer userId);

    // 4. 同步改名：selectAll → selectAllPage（分页）
    List<SysPointsLog> selectAllPage(@Param("start") Integer start,
                                     @Param("pageSize") Integer pageSize);

    // 5. 原有：查询所有日志总数（分页配套）
    Integer selectAllLogTotal();

    // 6. 新增：用户端查询——按用户ID查所有日志（不分页，兼容原有代码）
    // 关键：userId参数改为Integer，删除Long类型，统一类型
    @Select("SELECT * FROM sys_points_log WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<SysPointsLog> selectByUserId(@Param("userId") Integer userId);

    // 7. 新增：管理员/用户端通用——查询所有日志（不分页，备用）
    @Select("SELECT * FROM sys_points_log ORDER BY create_time DESC")
    List<SysPointsLog> selectAll();
}