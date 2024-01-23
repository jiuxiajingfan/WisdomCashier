package com.li.widomcashier.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.widomcashier.entry.po.TradeRefund;
import com.li.wisdomcashier.entry.R;

import java.util.List;

public interface TradeRefundService extends IService<TradeRefund> {
    /**
     * 退款记录
     *
     * @param tradeRefund
     */
    void TradeRefundRecord(TradeRefund tradeRefund);

    /**
     * 单号查询退款记录
     *
     * @param sid 店铺id
     * @param id  单号
     * @return
     */
    R<List<TradeRefund>> queryRefund(Long sid, Long id);

    /**
     * 检查并更改订单退款周期状态
     */
    void checkDate();
}
