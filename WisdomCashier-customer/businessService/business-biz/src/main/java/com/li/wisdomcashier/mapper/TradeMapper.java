package com.li.wisdomcashier.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.li.wisdomcashier.entry.Trade;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Li
 * @description 针对表【t_trade】的数据库操作Mapper
 * @createDate 2023-11-08 20:15:56
 * @Entity .entry.Trade
 */
public interface TradeMapper extends BaseMapper<Trade> {

    @Select("select *\n" +
            "from t_trade\n" +
            "where sid = #{sid}\n" +
            "  and operator = #{userID}\n" +
            "order by create_time desc\n" +
            "limit 0,10")
    List<Trade> selectLeast(@Param("sid") Long sid, @Param("userID") Long userId);

}




