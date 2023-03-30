package com.li.wisdomcashier.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.PageDTO;
import com.li.wisdomcashier.base.entity.dto.ShopApplyDTO;
import com.li.wisdomcashier.base.entity.dto.ShopApprovalDTO;
import com.li.wisdomcashier.base.entity.po.ShopApply;
import com.li.wisdomcashier.base.entity.vo.ShopApplyVO;
import com.li.wisdomcashier.base.entity.vo.ShopApplyVO2;

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

     R<IPage<ShopApplyVO2>> getApplyPage(PageDTO pageDTO);

     R<String> changeApplyStatus(ShopApprovalDTO shopApprovalDTO);

}
