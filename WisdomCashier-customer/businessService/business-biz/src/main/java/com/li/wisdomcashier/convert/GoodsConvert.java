package com.li.wisdomcashier.convert;

import com.li.wisdomcashier.controller.goods.vo.GoodsVO;
import com.li.wisdomcashier.entry.Goods;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName GoodsConvert
 * @Description TODO
 * @Author Nine
 * @Date 2024/2/27 20:52
 * @Version 1.0
 */
@Mapper
public interface GoodsConvert {
    GoodsConvert INSTANCE = Mappers.getMapper(GoodsConvert.class);

    GoodsVO toGoodsVO(Goods goods);
}
