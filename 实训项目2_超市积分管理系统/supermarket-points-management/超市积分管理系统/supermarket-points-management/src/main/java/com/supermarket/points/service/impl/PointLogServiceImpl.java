package com.supermarket.points.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermarket.points.mapper.SysPointsLogMapper;
import com.supermarket.points.model.entity.SysPointsLog;
import com.supermarket.points.model.entity.PointLog;
import com.supermarket.points.model.vo.PageVO;
import com.supermarket.points.service.PointLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class PointLogServiceImpl implements PointLogService {

    @Autowired
    private SysPointsLogMapper sysPointsLogMapper;

    // 工具方法：统一转换操作类型（英文→中文）
    private String convertType(String type) {
        if (type == null) return "";
        return switch (type.trim().toUpperCase()) {
            case "CONSUME", "消费" -> "消费";
            case "EXCHANGE", "兑换" -> "兑换";
            case "ADMIN", "管理员操作" -> "管理员操作";
            default -> type;
        };
    }

    // 写入统一表（全部用Integer）
    @Override
    public void addPointLog(PointLog oldPointLog) {
        if (oldPointLog == null) return;
        SysPointsLog sysPointsLog = new SysPointsLog();

        sysPointsLog.setUserId(oldPointLog.getUserId() != null ? oldPointLog.getUserId() : 0);
        sysPointsLog.setUsername(oldPointLog.getUsername() != null ? oldPointLog.getUsername() : "未知用户");
        sysPointsLog.setPoints(oldPointLog.getPointChange() != null ? oldPointLog.getPointChange() : 0);

        String originalType = oldPointLog.getType() != null ? oldPointLog.getType() : "";
        sysPointsLog.setType(convertType(originalType));

        String commodityName = oldPointLog.getCommodityName() != null ? oldPointLog.getCommodityName() : "未知商品";
        int pointChange = oldPointLog.getPointChange() != null ? oldPointLog.getPointChange() : 0;
        String pointDesc = pointChange > 0 ? "新增积分：" + pointChange : (pointChange < 0 ? "扣减积分：" + Math.abs(pointChange) : "积分无变动");
        sysPointsLog.setRemark("商品：" + commodityName + " | " + pointDesc);

        sysPointsLog.setCreateTime(new Date());
        sysPointsLog.setCommodityId(oldPointLog.getCommodityId() != null ? oldPointLog.getCommodityId() : 0);
        sysPointsLog.setCommodityName(commodityName);
        sysPointsLogMapper.insert(sysPointsLog);
    }

    // 查询用户日志（全部用Integer）
    @Override
    public List<PointLog> getLogByUserId(Integer userId) {
        if (userId == null) return new ArrayList<>();
        List<SysPointsLog> sysLogList = sysPointsLogMapper.selectByUserId(userId);
        List<PointLog> resultList = new ArrayList<>();

        for (SysPointsLog sysLog : sysLogList) {
            if (sysLog == null) continue;
            PointLog oldLog = new PointLog();
            oldLog.setUserId(sysLog.getUserId() != null ? sysLog.getUserId() : 0);
            oldLog.setUsername(sysLog.getUsername() != null ? sysLog.getUsername() : "未知用户");
            oldLog.setCommodityId(sysLog.getCommodityId() != null ? sysLog.getCommodityId() : 0);
            oldLog.setCommodityName(sysLog.getCommodityName() != null ? sysLog.getCommodityName() : "未知商品");
            oldLog.setPointChange(sysLog.getPoints() != null ? sysLog.getPoints() : 0);
            oldLog.setType(convertType(sysLog.getType()));
            oldLog.setCreateTime(sysLog.getCreateTime());
            resultList.add(oldLog);
        }
        return resultList;
    }

    // 查询所有日志（全部用Integer）
    @Override
    public List<PointLog> getAllPointLogs() {
        List<SysPointsLog> sysLogList = sysPointsLogMapper.selectAll();
        List<PointLog> resultList = new ArrayList<>();

        for (SysPointsLog sysLog : sysLogList) {
            if (sysLog == null) continue;
            PointLog oldLog = new PointLog();
            oldLog.setUserId(sysLog.getUserId() != null ? sysLog.getUserId() : 0);
            oldLog.setUsername(sysLog.getUsername() != null ? sysLog.getUsername() : "未知用户");
            oldLog.setCommodityId(sysLog.getCommodityId() != null ? sysLog.getCommodityId() : 0);
            oldLog.setCommodityName(sysLog.getCommodityName() != null ? sysLog.getCommodityName() : "未知商品");
            oldLog.setPointChange(sysLog.getPoints() != null ? sysLog.getPoints() : 0);
            oldLog.setType(convertType(sysLog.getType()));
            oldLog.setCreateTime(sysLog.getCreateTime());
            resultList.add(oldLog);
        }
        return resultList;
    }

    // 分页查询日志（全部用Integer）
    @Override
    public PageVO<PointLog> getPointLogPage(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;

        PageHelper.startPage(pageNum, pageSize);
        List<SysPointsLog> sysLogList = sysPointsLogMapper.selectAll();
        PageInfo<SysPointsLog> pageInfo = new PageInfo<>(sysLogList);

        List<PointLog> oldLogList = new ArrayList<>();
        for (SysPointsLog sysLog : sysLogList) {
            if (sysLog == null) continue;
            PointLog oldLog = new PointLog();
            oldLog.setUserId(sysLog.getUserId() != null ? sysLog.getUserId() : 0);
            oldLog.setUsername(sysLog.getUsername() != null ? sysLog.getUsername() : "未知用户");
            oldLog.setCommodityId(sysLog.getCommodityId() != null ? sysLog.getCommodityId() : 0);
            oldLog.setCommodityName(sysLog.getCommodityName() != null ? sysLog.getCommodityName() : "未知商品");
            oldLog.setPointChange(sysLog.getPoints() != null ? sysLog.getPoints() : 0);
            oldLog.setType(convertType(sysLog.getType()));
            oldLog.setCreateTime(sysLog.getCreateTime());
            oldLogList.add(oldLog);
        }

        PageVO<PointLog> pageVO = new PageVO<>();
        pageVO.setList(oldLogList);
        pageVO.setTotal((int) pageInfo.getTotal());
        pageVO.setPageNum(pageInfo.getPageNum());
        pageVO.setPageSize(pageInfo.getPageSize());
        pageVO.setTotalPage(pageInfo.getPages());

        return pageVO;
    }

    // 管理员修改积分写日志（全部用Integer）
    public void addAdminPointLog(Integer userId, String username, Integer points, String type, String remark) {
        if (userId == null) userId = 0;
        if (username == null) username = "未知用户";
        if (points == null) points = 0;

        String logType = convertType(type != null ? type : "管理员操作");
        if (logType.isEmpty()) logType = "管理员操作";

        String logRemark = remark != null ? remark : "";
        int pointChange = points != null ? points : 0;
        String pointDesc = pointChange > 0 ? "新增积分：" + pointChange : (pointChange < 0 ? "扣减积分：" + Math.abs(pointChange) : "积分无变动");
        if (logRemark.isEmpty()) {
            logRemark = pointDesc;
        } else {
            logRemark = logRemark + " | " + pointDesc;
        }

        SysPointsLog sysLog = new SysPointsLog();
        sysLog.setUserId(userId);
        sysLog.setUsername(username);
        sysLog.setPoints(points);
        sysLog.setType(logType);
        sysLog.setRemark(logRemark);
        sysLog.setCreateTime(new Date());
        sysLog.setCommodityId(0);
        sysLog.setCommodityName("管理员操作");
        sysPointsLogMapper.insert(sysLog);
    }
}