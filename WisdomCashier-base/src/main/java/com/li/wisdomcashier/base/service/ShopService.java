package com.li.wisdomcashier.base.service;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.po.Shop;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Nine
 * @since 2022-10-11
 */
public interface ShopService extends IService<Shop> {
    /**
     * 获取用户相关店铺
     * @return
     */
   R<List<ShopVo>> getUserShop(String shopName);

}
