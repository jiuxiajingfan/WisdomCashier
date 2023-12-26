package com.li.widomcashier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.li.widomcashier.entry.po.Trade;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lsw
 * @since 2023-03-03
 */
public interface TradeMapper extends BaseMapper<Trade> {

    @Select("select *\n" +
            "from t_trade\n" +
            "where sid = #{sid}\n" +
            "  and operater = #{userID}\n" +
            "order by create_time desc\n" +
            "limit 0,10")
    List<Trade> selectLesat(@Param("sid") Long sid, @Param("userID") Long userId);
}
