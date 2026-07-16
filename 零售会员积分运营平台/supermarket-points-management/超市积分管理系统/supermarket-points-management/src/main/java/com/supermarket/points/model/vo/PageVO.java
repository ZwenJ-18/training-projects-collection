package com.supermarket.points.model.vo;

import lombok.Data;
import java.util.List;

/**
 * 分页返回VO（通用）
 * @param <T> 数据类型
 */
@Data
public class PageVO<T> {
    private List<T> list;       // 当前页数据
    private Integer total;      // 总条数
    private Integer pageNum;    // 当前页码
    private Integer pageSize;   // 每页条数
    private Integer totalPage;  // 总页数
}