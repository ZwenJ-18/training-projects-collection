package com.supermarket.points.service.impl;

import com.supermarket.points.mapper.CommodityMapper;
import com.supermarket.points.model.dto.CommodityAddDTO;
import com.supermarket.points.model.dto.CommodityEditDTO;
import com.supermarket.points.model.entity.Commodity;
import com.supermarket.points.model.vo.PageVO;
import com.supermarket.points.service.CommodityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 商品服务实现类
 * 实现所有商品相关业务逻辑
 */
@Service
public class CommodityServiceImpl implements CommodityService {

    private static final Logger logger = LoggerFactory.getLogger(CommodityServiceImpl.class);

    // 商品上架状态
    private static final Integer COMMODITY_ON_SALE = 1;

    @Autowired
    private CommodityMapper commodityMapper;

    /**
     * 管理员端 - 分页查询所有商品（包含下架）
     */
    @Override
    public PageVO<Commodity> getCommodityPage(Integer pageNum, Integer pageSize) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Commodity> list = commodityMapper.selectList();
            PageInfo<Commodity> pageInfo = new PageInfo<>(list);
            PageVO<Commodity> pageVO = new PageVO<>();
            pageVO.setTotal((int) pageInfo.getTotal());
            pageVO.setList(pageInfo.getList());
            return pageVO;
        } catch (Exception e) {
            logger.error("分页查询所有商品失败：pageNum={}, pageSize={}", pageNum, pageSize, e);
            throw new RuntimeException("商品查询失败", e);
        }
    }

    /**
     * 用户端 - 分页查询上架商品
     */
    @Override
    public PageVO<Commodity> getOnSaleCommodityPage(Integer pageNum, Integer pageSize) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            // 改：调用selectExchangeableCommodities（你Mapper里已有的方法）
            List<Commodity> list = commodityMapper.selectExchangeableCommodities();
            PageInfo<Commodity> pageInfo = new PageInfo<>(list);
            PageVO<Commodity> pageVO = new PageVO<>();
            pageVO.setTotal((int) pageInfo.getTotal());
            pageVO.setList(pageInfo.getList());
            return pageVO;
        } catch (Exception e) {
            logger.error("分页查询上架商品失败：pageNum={}, pageSize={}", pageNum, pageSize, e);
            throw new RuntimeException("上架商品查询失败", e);
        }
    }

    /**
     * 按ID查询商品（Integer）
     */
    @Override
    public Commodity getCommodityById(Integer id) {
        try {
            return commodityMapper.selectById(id);
        } catch (Exception e) {
            logger.error("按ID查询商品失败：id={}", id, e);
            throw new RuntimeException("商品查询失败", e);
        }
    }

    /**
     * 按ID查询商品（Long）
     */
    @Override
    public Commodity getById(Long id) {
        if (id == null) {
            logger.warn("商品ID为空，查询失败");
            return null;
        }
        return getCommodityById(id.intValue());
    }

    /**
     * 新增商品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCommodity(CommodityAddDTO dto) {
        try {
            Commodity commodity = new Commodity();
            BeanUtils.copyProperties(dto, commodity);
            commodity.setStatus(COMMODITY_ON_SALE);
            commodity.setCreateTime(new Date());
            int affectRows = commodityMapper.insert(commodity);
            logger.info("新增商品成功：name={}, id={}", commodity.getName(), commodity.getId());
            return affectRows > 0;
        } catch (Exception e) {
            logger.error("新增商品失败：dto={}", dto, e);
            throw new RuntimeException("商品新增失败", e);
        }
    }

    /**
     * 扣减库存（返回布尔值）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deductStock(Integer id, Integer num) {
        if (id == null || num == null || num <= 0) {
            logger.warn("库存扣减参数无效：id={}, num={}", id, num);
            return false;
        }
        int affectRows = updateStock(id, num);
        logger.info("商品库存扣减：id={}, num={}, 影响行数={}", id, num, affectRows);
        return affectRows > 0;
    }

    /**
     * 更新库存（返回影响行数）
     */
    @Override
    public int updateStock(Integer id, Integer num) {
        try {
            return commodityMapper.updateStock(id, num);
        } catch (Exception e) {
            logger.error("更新商品库存失败：id={}, num={}", id, num, e);
            throw new RuntimeException("库存更新失败", e);
        }
    }

    /**
     * 编辑商品（包含状态修改）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editCommodity(CommodityEditDTO dto) {
        try {
            Commodity commodity = new Commodity();
            BeanUtils.copyProperties(dto, commodity);
            int affectRows = commodityMapper.updateById(commodity);
            logger.info("编辑商品：id={}, status={}, 影响行数={}",
                    commodity.getId(), commodity.getStatus(), affectRows);
            return affectRows > 0;
        } catch (Exception e) {
            logger.error("编辑商品失败：dto={}", dto, e);
            throw new RuntimeException("商品编辑失败", e);
        }
    }

    /**
     * 删除商品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCommodity(Integer id) {
        try {
            int affectRows = commodityMapper.deleteById(id);
            logger.info("删除商品：id={}, 影响行数={}", id, affectRows);
            return affectRows > 0;
        } catch (Exception e) {
            logger.error("删除商品失败：id={}", id, e);
            throw new RuntimeException("商品删除失败", e);
        }
    }

    /**
     * 校验商品是否上架
     */
    @Override
    public boolean isCommodityOnSale(Long commodityId) {
        Commodity commodity = getById(commodityId);
        if (commodity == null) {
            logger.warn("商品不存在：id={}", commodityId);
            return false;
        }
        boolean isOnSale = COMMODITY_ON_SALE.equals(commodity.getStatus());
        logger.info("商品状态校验：id={}, status={}, 是否上架={}",
                commodityId, commodity.getStatus(), isOnSale);
        return isOnSale;
    }

    /**
     * 带悲观锁查询商品（防止并发超卖）
     */
    @Override
    public Commodity getByIdForUpdate(Long id) {
        try {
            // 改：调用selectByIdForUpdate(Long)（匹配你Mapper的参数）
            return commodityMapper.selectByIdForUpdate(id);
        } catch (Exception e) {
            logger.error("带锁查询商品失败：id={}", id, e);
            throw new RuntimeException("商品查询失败", e);
        }
    }
}