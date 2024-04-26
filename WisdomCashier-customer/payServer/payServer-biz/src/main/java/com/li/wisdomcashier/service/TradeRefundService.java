package com.li.wisdomcashier.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.entry.po.TradeRefund;
import com.li.wisdomcashier.entry.R;

import java.util.List;

public interface TradeRefundService extends IService<TradeRefund> {

    /**
     * 检查并更改订单退款周期状态
     */
    void checkDate();
}
