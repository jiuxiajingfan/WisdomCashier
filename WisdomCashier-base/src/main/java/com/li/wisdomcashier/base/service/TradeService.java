package com.li.wisdomcashier.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.GoodQueryDTO;
import com.li.wisdomcashier.base.entity.dto.QueryMoneyDTO;
import com.li.wisdomcashier.base.entity.dto.QueryTradeDTO;
import com.li.wisdomcashier.base.entity.dto.RefundDTO;
import com.li.wisdomcashier.base.entity.po.Goods;
import com.li.wisdomcashier.base.entity.po.GoodsVO;
import com.li.wisdomcashier.base.entity.po.Trade;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.base.entity.po.TradeGoods;
import com.li.wisdomcashier.base.entity.vo.EChartVO;
import com.li.wisdomcashier.base.entity.vo.TradeVO;

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
     *
     * @param sid
     * @return
     */
    R<List<TradeVO>> queryLeast(Long sid);

    /**
     * 更具订单查询详情
     *
     * @param id
     * @return
     */
    R<List<TradeGoods>> queryGoodsById(Long id);

    R<IPage<TradeVO>> queryTradePage(QueryTradeDTO queryTradeDTO);

    R<String> cashTradeRefund(RefundDTO refundDTO);


    /**
     * 交易er图
     *
     * @return
     */
    R<List<List<EChartVO>>> currentTradeMoney(QueryMoneyDTO queryMoneyDTO);

    void AsyncSaveGood(List<Goods> goodsList, Long id,boolean isVip,String sid);


    /**
     * 商品排行
     * @param goodQueryDTO
     * @return
     */
    R<List<Goods>> getGoodRankPage(GoodQueryDTO goodQueryDTO);
}

