package com.supermarket.points.service;

import com.supermarket.points.model.dto.CommodityAddDTO;
import com.supermarket.points.model.dto.CommodityEditDTO;
import com.supermarket.points.model.entity.Commodity;
import com.supermarket.points.model.vo.PageVO;

/**
 * 商品服务接口
 * 包含商品CRUD、库存操作、状态过滤等核心功能
 */
public interface CommodityService {

    /**
     * 管理员端 - 分页查询所有商品（包含下架）
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页商品列表
     */
    PageVO<Commodity> getCommodityPage(Integer pageNum, Integer pageSize);

    /**
     * 用户端 - 分页查询上架商品（仅status=1）
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页上架商品列表
     */
    PageVO<Commodity> getOnSaleCommodityPage(Integer pageNum, Integer pageSize);

    /**
     * 按ID查询商品（通用）
     * @param id 商品ID（Integer）
     * @return 商品信息
     */
    Commodity getCommodityById(Integer id);

    /**
     * 按ID查询商品（兼容Long类型）
     * @param id 商品ID（Long）
     * @return 商品信息
     */
    Commodity getById(Long id);

    /**
     * 新增商品
     * @param dto 商品新增DTO
     * @return 是否成功
     */
    boolean addCommodity(CommodityAddDTO dto);

    /**
     * 扣减库存（通用）
     * @param id 商品ID
     * @param num 扣减数量
     * @return 是否成功
     */
    boolean deductStock(Integer id, Integer num);

    /**
     * 更新库存（返回影响行数）
     * @param id 商品ID
     * @param num 扣减数量
     * @return 数据库影响行数
     */
    int updateStock(Integer id, Integer num);

    /**
     * 编辑商品（包含状态修改：上架/下架）
     * @param dto 商品编辑DTO
     * @return 是否成功
     */
    boolean editCommodity(CommodityEditDTO dto);

    /**
     * 删除商品
     * @param id 商品ID
     * @return 是否成功
     */
    boolean deleteCommodity(Integer id);

    /**
     * 校验商品是否上架（status=1）
     * @param commodityId 商品ID
     * @return true=上架，false=下架/不存在
     */
    boolean isCommodityOnSale(Long commodityId);

    /**
     * 带悲观锁查询商品（防止并发超卖）
     * @param id 商品ID
     * @return 商品信息（加锁）
     */
    Commodity getByIdForUpdate(Long id);
}