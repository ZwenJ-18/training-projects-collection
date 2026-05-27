package com.supermarket.points.mapper;

import com.supermarket.points.model.entity.Commodity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

// 必须加@Mapper，否则Spring无法扫描到！
@Mapper
public interface CommodityMapper {
    // 1. 查全部商品（不分页）
    List<Commodity> selectList();

    // 2. 按关键词模糊查询（名称/编码）
    List<Commodity> selectByKeyword(@Param("keyword") String keyword);

    // 3. 按ID查商品（Integer类型ID）
    Commodity selectById(Integer id);

    // 4. 新增商品
    int insert(Commodity commodity);

    // 5. 按ID更新商品
    int updateById(Commodity commodity);

    // 6. 按ID删除商品
    int deleteById(Integer id);

    // 7. 分页查询（备用）
    List<Commodity> selectAll(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    // 8. 查询商品总数（备用）
    Integer selectTotal();

    // 9. 按编码查商品（检查重复）
    Commodity selectByCode(@Param("code") String code);

    // ========== 核心修复：库存扣减方法（加注解+明确SQL） ==========
    /**
     * 扣减/增加商品库存
     * @param id 商品ID
     * @param num 正数=加库存，负数=扣库存（比如-1=扣1个）
     * @return 影响行数
     */
    int updateStock(@Param("id") Integer id, @Param("num") Integer num);

    List<Commodity> selectExchangeableCommodities();

    // 根据Long类型ID查询商品
    @Select("SELECT * FROM commodity WHERE id = #{id}")
    Commodity selectByIdLong(Long id);

    // 带悲观锁查询商品（解决并发超卖）
    @Select("SELECT * FROM commodity WHERE id = #{id} FOR UPDATE")
    Commodity selectByIdForUpdate(Long id);
}