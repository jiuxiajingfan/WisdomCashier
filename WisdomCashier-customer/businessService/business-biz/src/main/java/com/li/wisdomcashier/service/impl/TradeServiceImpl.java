package com.li.wisdomcashier.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.controller.trade.dto.QueryTradeDTO;
import com.li.wisdomcashier.controller.trade.vo.TradeVO;
import com.li.wisdomcashier.convert.TradeConvert;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.Trade;
import com.li.wisdomcashier.entry.TradeGoods;
import com.li.wisdomcashier.mapper.TradeGoodsMapper;
import com.li.wisdomcashier.mapper.TradeMapper;
import com.li.wisdomcashier.service.TradeService;
import com.li.wisdomcashier.utils.UserUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Li
 * @description 针对表【t_trade】的数据库操作Service实现
 * @createDate 2023-11-08 20:15:56
 */
@Service
public class TradeServiceImpl extends ServiceImpl<TradeMapper, Trade> implements TradeService {

    @Resource
    private TradeMapper tradeMapper;

    @Resource
    private TradeGoodsMapper tradeGoodsMapper;

    @Override
    public R<List<TradeVO>> queryLeast(Long sid) {
        List<Trade> trades = tradeMapper.selectLeast(sid, UserUtils.getUser().getId());
        List<TradeVO> collect = trades.stream().map(e -> {
                    TradeVO copy = TradeConvert.INSTANCE.tradeToTradeVO(e);
                    copy.setIncome(String.format("%.2f", e.getIncome()));
                    return copy;
                }
        ).toList();
        return R.ok(collect);
    }

    @Override
    public R<IPage<TradeVO>> queryTradePage(QueryTradeDTO queryTradeDTO) {
        if (!Objects.isNull(queryTradeDTO.getEndTime())) {
            if (queryTradeDTO.getStartTime().compareTo(queryTradeDTO.getEndTime()) == 0) {
                queryTradeDTO.setEndTime(queryTradeDTO.getEndTime().plusDays(1));
            }
        }
        LambdaQueryWrapper<Trade> wrapper = Wrappers.lambdaQuery(Trade.class)
                .eq(Trade::getSid, queryTradeDTO.getSid())
                .likeRight(!Objects.isNull(queryTradeDTO.getId()), Trade::getId, queryTradeDTO.getId())
                .in(!CollectionUtils.isEmpty(queryTradeDTO.getStatus()), Trade::getStatus, queryTradeDTO.getStatus())
                .lt(!Objects.isNull(queryTradeDTO.getEndTime()), Trade::getCreateTime, queryTradeDTO.getEndTime())
                .ge(!Objects.isNull(queryTradeDTO.getStartTime()), Trade::getCreateTime, queryTradeDTO.getStartTime())
                .orderByDesc(Trade::getCreateTime);
        Page<Trade> page = new Page<>(queryTradeDTO.getCurrent(), queryTradeDTO.getPageSize());
        IPage<TradeVO> convert = tradeMapper.selectPage(page, wrapper).convert(e ->
                {
                    TradeVO copy = TradeConvert.INSTANCE.tradeToTradeVO(e);
                    copy.setIncome(String.format("%.2f", e.getIncome()));
                    return copy;
                }
        );
        return R.ok(convert);
    }

    public R<List<TradeGoods>> queryGoodsById(Long id) {
        List<TradeGoods> tradeGoods = tradeGoodsMapper.selectList(Wrappers.lambdaQuery(TradeGoods.class)
                .eq(TradeGoods::getTradeId, id));
        return R.ok(tradeGoods);
    }
}




