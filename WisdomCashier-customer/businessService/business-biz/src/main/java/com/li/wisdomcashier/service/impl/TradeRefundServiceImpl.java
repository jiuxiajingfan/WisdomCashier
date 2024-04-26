package com.li.wisdomcashier.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.TradeRefund;
import com.li.wisdomcashier.mapper.TradeRefundMapper;
import com.li.wisdomcashier.service.TradeRefundService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Li
 * @description 针对表【t_trade_refund】的数据库操作Service实现
 * @createDate 2023-11-08 20:16:06
 */
@Service
public class TradeRefundServiceImpl extends ServiceImpl<TradeRefundMapper, TradeRefund>
        implements TradeRefundService {

    @Resource
    private TradeRefundMapper tradeRefundMapper;

    @Override
    public R<List<TradeRefund>> queryRefund(Long sid, Long id) {
        List<TradeRefund> tradeRefunds = tradeRefundMapper.selectList(Wrappers.lambdaQuery(TradeRefund.class)
                .eq(TradeRefund::getSid, id)
        );
        return R.ok(tradeRefunds);
    }
}




