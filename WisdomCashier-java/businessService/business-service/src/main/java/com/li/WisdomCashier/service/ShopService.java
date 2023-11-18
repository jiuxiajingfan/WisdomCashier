package com.li.WisdomCashier.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.WisdomCashier.controller.shop.shopApply.dto.ShopQueryDTO;
import com.li.WisdomCashier.controller.shop.shopApply.vo.ShopVO;
import com.li.WisdomCashier.entry.Shop;
import com.li.WisdomCashier.pojo.R;

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

    R<List<Tree<String>>> getMenu(long id);

    /**
     * 获取交易方式状态
     * @param sid
     * @return
     */
    R<List<Integer>> getTradeStatus(String sid);
}
