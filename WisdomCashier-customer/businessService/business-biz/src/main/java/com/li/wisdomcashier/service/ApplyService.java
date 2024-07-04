package com.li.wisdomcashier.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.controller.shop.shopApply.dto.ApprovalDTO;
import com.li.wisdomcashier.controller.shop.shopApply.vo.ApplyVO;
import com.li.wisdomcashier.entry.Apply;
import com.li.wisdomcashier.entry.R;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lsw
 * @since 2023-03-20
 */
public interface ApplyService extends IService<Apply> {

        /**
         * 获取店铺申请列表
         * @param sid
         * @return
         */
        R<List<ApplyVO>> getApplyList(String sid);

        /**
         * 审批申请
         */
        R<String> approval(ApprovalDTO approvalDTO);

        /**
         * 申请加入
         * @return
         */
        R<String> applyShop(String sid);

        R<List<ApplyVO>> getApplyListPer();
}
