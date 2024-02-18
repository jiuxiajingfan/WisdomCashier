package com.li.wisdomcashier.convert;

import com.li.wisdomcashier.controller.shop.shopApply.vo.ShopVO;
import com.li.wisdomcashier.entry.Shop;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShopConvert {
    ShopConvert INSTANCE = Mappers.getMapper(ShopConvert.class);

    ShopVO toShopVo(Shop shop);
}
