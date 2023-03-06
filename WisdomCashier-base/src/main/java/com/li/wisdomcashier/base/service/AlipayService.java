package com.li.wisdomcashier.base.service;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.AliPayDTO;
import com.li.wisdomcashier.base.entity.dto.PayDTO;

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
    R<PayDTO> aliPay(AliPayDTO aliPayDTO);

    /**
     * 查询订单号状态
     * @param tradeNo 支付宝订单号
     * @return
     */
    R<String> queryAliPay(String tradeNo);

    /**
     * 支付未知错误撤销订单
     * @param tradeNo 支付宝订单号
     * @return
     */
    R<String> cancelPay(String tradeNo);

    /**
     * 超时主动取消
     * @param tradeNo
     * @return
     */
    R<String> closePay(String tradeNo);
}