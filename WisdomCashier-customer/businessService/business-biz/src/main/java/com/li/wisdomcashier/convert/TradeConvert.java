package com.li.wisdomcashier.convert;

import com.li.wisdomcashier.controller.trade.vo.TradeVO;
import com.li.wisdomcashier.entry.Trade;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TradeConvert {
    TradeConvert INSTANCE = Mappers.getMapper(TradeConvert.class);

    TradeVO tradeToTradeVO(Trade trade);
}
