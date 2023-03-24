package com.li.wisdomcashier.base.service;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.ShopApplyDTO;
import com.li.wisdomcashier.base.entity.po.ShopApply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.base.entity.vo.ShopApplyVO;

import java.util.List;

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


     /**
      * 申请进度
      * @return
      */
     R<ShopApplyVO> getApply();


     /**
      * 撤销申请
      * @param id
      * @return
      */
     R<String> cancelApply(String id);

}
