package com.li.wisdomcashier.service;

import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.PayVO;

public interface PayService {
    /**
     * 支付
     * @param type
     * @param aliPayDTO
     * @return
     */
    R<PayVO> pay(Integer type, PayDTO aliPayDTO);
}
