package com.li.wisdomcashier.base.service;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.AliPayDTO;

/**
 * @ClassName PayService
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/4 16:31
 * @Version 1.0
 */
public interface AlipayService {

    /**
     * 支付宝提交收款接口
     * @param aliPayDTO
     * @return
     */
    R<String> aliPay(AliPayDTO aliPayDTO);

    /**
     * 查询订单号状态
     * @param tradeNo 支付宝订单号
     * @return
     */
    R<String> queryAliPay(String tradeNo);
}
