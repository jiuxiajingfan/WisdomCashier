package com.li.wisdomcashier.service;

import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.PayVO;

public interface PayService {

    /**
     * 支付
     * @param payDTO
     * @return
     */
    R<PayVO> pay(PayDTO payDTO);

}
