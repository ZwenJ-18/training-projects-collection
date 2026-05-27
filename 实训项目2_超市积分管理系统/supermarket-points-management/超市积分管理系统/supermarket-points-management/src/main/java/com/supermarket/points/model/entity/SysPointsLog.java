package com.supermarket.points.model.entity;

import lombok.Data;
import java.util.Date;

@Data
public class SysPointsLog {
    // 关键修改：所有ID字段改成Integer，和代码里的user.getId()/commodityId匹配
    private Integer id;          // 日志ID
    private Integer userId;      // 用户ID（改Integer）
    private String username;
    private Integer points;
    private String type;
    private String remark;
    private Date createTime;
    private Integer commodityId; // 商品ID（改Integer）
    private String commodityName;
}