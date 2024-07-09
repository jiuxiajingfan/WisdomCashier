package com.li.wisdomcashier.service;

import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.PayVO;
import com.li.wisdomcashier.entry.dto.PayInfo;
import com.li.wisdomcashier.entry.dto.RefundDTO;
import com.li.wisdomcashier.entry.vo.StatusVO;

/**
 * @ClassName PayService
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/4 16:31
 * @Version 1.0
 */
public interface DubboPayService {

    /**
     * 提交支付
     */
    PayVO pay(PayDTO payDTO);

    /**
     * 查询订单号状态
     * @param tradeNo 订单号
     */
    StatusVO status(Integer type, String tradeNo);

    /**
     * 支付未知错误撤销订单
     * @param tradeNo 订单号
     * @return
     */
    String cancel(Integer type,String tradeNo);

    /**
     * 超时主动取消
     *
     * @param tradeNo
     * @return
     */
    String close(Integer type,String tradeNo);

    PayInfo detail(Integer type,String tradeNo);

    /**
     * 退款接口
     *
     * @param refundDTO
     * @return
     */
    String refund(RefundDTO refundDTO);

}
