package com.li.wisdomcashier.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.TradeRefund;

import java.util.List;

/**
 * @author Li
 * @description 针对表【t_trade_refund】的数据库操作Service
 * @createDate 2023-11-08 20:16:06
 */
public interface TradeRefundService extends IService<TradeRefund> {

    R<List<TradeRefund>> queryRefund(Long sid, Long id);
}
