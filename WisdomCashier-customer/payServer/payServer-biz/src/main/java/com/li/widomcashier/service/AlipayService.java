package com.li.widomcashier.service;

import com.li.widomcashier.entry.dto.AliPayDTO;
import com.li.widomcashier.entry.dto.PayDTO;
import com.li.widomcashier.entry.dto.RefundDTO;
import com.li.widomcashier.entry.po.QueryTrade;
import com.li.wisdomcashier.entry.R;

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
    R<String> cancelPay(String tradeNo, Long sid);

    /**
     * 超时主动取消
     * @param tradeNo
     * @return
     */
    R<String> closePay(String tradeNo,Long sid);

    QueryTrade queryPayDetil(String tradeNo);

    /**
     * 退款接口
     * @param refundDTO
     * @return
     */
    R<String> refundPay(RefundDTO refundDTO);

    /**
     * 退款查询接口
     * @param refundDTO
     * @return
     */
    R<String> queryRefund(RefundDTO refundDTO);

}
