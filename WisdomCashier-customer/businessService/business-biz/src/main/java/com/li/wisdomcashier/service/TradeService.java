package com.li.wisdomcashier.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.controller.trade.dto.QueryTradeDTO;
import com.li.wisdomcashier.controller.trade.vo.TradeVO;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.Trade;
import com.li.wisdomcashier.entry.TradeGoods;

import java.util.List;

/**
 * @author Li
 * @description 针对表【t_trade】的数据库操作Service
 * @createDate 2023-11-08 20:15:56
 */
public interface TradeService extends IService<Trade> {

    /**
     * 最近交易
     * @param sid
     * @return
     */
    R<List<TradeVO>> queryLeast(Long sid);

    /**
     * 交易记录
     * @param dto
     * @return
     */
    R<IPage<TradeVO>> queryTradePage(QueryTradeDTO dto);

    R<List<TradeGoods>> queryGoodsById(Long id);
}
