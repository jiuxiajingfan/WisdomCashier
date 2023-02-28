package com.li.wisdomcashier.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.GoodDTO;
import com.li.wisdomcashier.base.entity.dto.GoodQueryDTO;
import com.li.wisdomcashier.base.entity.po.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.base.entity.po.GoodsVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lsw
 * @since 2023-02-26
 */
public interface GoodsService extends IService<Goods> {

    /**
     * 查询商品信息
     * @param gid
     * @return
     */
    R<Goods> reqGood(String gid);

    /**
     * 新增商品
     * @param good
     * @return
     */
    R<String> addGood(GoodDTO good);

    /**
     * 根据ID查询
     * @param goodQueryDTO
     * @return
     */
    R<IPage<Goods>> getGoodPage(GoodQueryDTO goodQueryDTO);

    /**
     * 修稿商品信息
     * @param good
     * @return
     */
    R<String> updateGood(GoodDTO good);
}