package com.li.wisdomcashier.convert;

import com.li.wisdomcashier.controller.shop.shopApply.dto.ShopApplyDTO;
import com.li.wisdomcashier.entry.ShopApply;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShopApplyConvert {
    ShopApplyConvert INSTANCE = Mappers.getMapper(ShopApplyConvert.class);

    ShopApply toShopApply(ShopApplyDTO shopApplyDTO);
}
