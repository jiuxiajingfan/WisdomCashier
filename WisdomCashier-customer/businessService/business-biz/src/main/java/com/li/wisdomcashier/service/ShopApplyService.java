package com.li.wisdomcashier.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.controller.shop.shopApply.dto.ShopApplyDTO;
import com.li.wisdomcashier.controller.shop.shopApply.vo.ShopApplyVO;
import com.li.wisdomcashier.entry.ShopApply;
import com.li.wisdomcashier.entry.R;

/**
* @author Li
* @description 针对表【t_shop_apply】的数据库操作Service
* @createDate 2023-11-08 20:15:38
*/
public interface ShopApplyService extends IService<ShopApply> {
    /**
     * 店铺申请进度列表
     * @return
     */
    R<ShopApplyVO> getApply();

    R<String> applyShop(ShopApplyDTO shopApplyDTO);
}
