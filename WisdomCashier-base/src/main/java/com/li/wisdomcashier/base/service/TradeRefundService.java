package com.li.wisdomcashier.base.service;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.po.TradeRefund;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lsw
 * @since 2023-03-11
 */
public interface TradeRefundService extends IService<TradeRefund> {
    /**
     * 退款记录
     * @param tradeRefund
     */
    void TradeRefundRecord(TradeRefund tradeRefund);

    /**
     * 单号查询退款记录
     * @param sid 店铺id
     * @param id 单号
     * @return
     */
    R<List<TradeRefund>> queryRefund(Long sid,Long id);

    /**
     * 检查并更改订单退款周期状态
     */
    void checkDate();
}
