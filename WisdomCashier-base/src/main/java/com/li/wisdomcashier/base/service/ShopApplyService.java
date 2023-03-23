package com.li.wisdomcashier.base.service;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.ShopApplyDTO;
import com.li.wisdomcashier.base.entity.po.ShopApply;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lsw
 * @since 2023-03-23
 */
public interface ShopApplyService extends IService<ShopApply> {


     /**
      * 店铺申请
      * @param shopApplyDTO
      * @return
      */
     R<String> applyShop(ShopApplyDTO shopApplyDTO);
}
