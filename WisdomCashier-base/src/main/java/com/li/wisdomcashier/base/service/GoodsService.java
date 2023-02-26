package com.li.wisdomcashier.base.service;

import com.li.wisdomcashier.base.common.R;
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
}
