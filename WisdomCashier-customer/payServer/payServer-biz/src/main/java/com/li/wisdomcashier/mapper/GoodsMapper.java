package com.li.wisdomcashier.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.li.wisdomcashier.entry.po.Goods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lsw
 * @since 2023-02-26
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    @Update("update t_goods\n" +
            "set num = num - #{num}\n" +
            "where gid = #{gid} and sid = #{sid}")
    Integer reduceNum(@Param("sid")String sid, @Param("gid") String gid, @Param("num") Integer num);

}
