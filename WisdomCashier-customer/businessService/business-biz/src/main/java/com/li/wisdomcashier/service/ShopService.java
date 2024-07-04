package com.li.wisdomcashier.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.controller.shop.shopApply.dto.ShopQueryDTO;
import com.li.wisdomcashier.controller.shop.shopApply.vo.ShopVO;
import com.li.wisdomcashier.entry.Shop;
import com.li.wisdomcashier.entry.R;

import java.util.List;

/**
 * @author Li
 * @description 针对表【t_shop】的数据库操作Service
 * @createDate 2023-11-08 20:06:06
 */
public interface ShopService extends IService<Shop> {
    /**
     * 获取用户相关店铺
     *
     * @param shopQueryDTO
     * @return
     */
    R<IPage<ShopVO>> getUserShopPage(ShopQueryDTO shopQueryDTO);

    R<List<Tree<String>>> getMenu(Long id);

    /**
     * 获取交易方式状态
     *
     * @param sid
     * @return
     */
    R<List<Integer>> getTradeStatus(String sid);

    /**
     * 判断是否是店铺会员
     * @param sid
     * @param phone
     * @return
     */
    R<Long> isVip(String sid, String phone);
}
