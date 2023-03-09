package com.li.wisdomcashier.base.service;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.TradeDTO;
import com.li.wisdomcashier.base.entity.po.Trade;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.base.entity.po.TradeGoods;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lsw
 * @since 2023-03-03
 */
public interface TradeService extends IService<Trade> {

    /**
     * 查询店铺最近十笔交易记录
     * @param sid
     * @return
     */
    R<List<TradeDTO>> queryLeast(Long sid);

    /**
     * 更具订单查询详情
     * @param id
     * @return
     */
    R<List<TradeGoods>> queryGoodsById(Long id);
}
