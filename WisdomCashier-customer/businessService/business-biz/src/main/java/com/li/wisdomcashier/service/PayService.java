package com.li.wisdomcashier.service;

import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.PayVO;
import com.li.wisdomcashier.entry.dto.RefundDTO;
import com.li.wisdomcashier.entry.vo.StatusVO;

public interface PayService {

    /**
     * 支付
     * @param payDTO
     * @return
     */
    R<PayVO> pay(PayDTO payDTO);

    /**
     * 查询订单
     * @param type 支付类型
     * @param tradeNo 远程订单号
     * @return
     */
    R<StatusVO> status(Integer type, String tradeNo);

    R<String> refundPay(RefundDTO refundDTO);
}
