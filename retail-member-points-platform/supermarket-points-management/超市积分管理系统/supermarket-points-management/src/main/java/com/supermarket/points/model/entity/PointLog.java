package com.supermarket.points.model.entity;

import lombok.Data;
import java.util.Date;

@Data
public class PointLog {
    private Integer userId;      // 改Integer
    private String username;
    private Integer commodityId; // 改Integer
    private String commodityName;
    private Integer pointChange;
    private String type;
    private Date createTime;
}