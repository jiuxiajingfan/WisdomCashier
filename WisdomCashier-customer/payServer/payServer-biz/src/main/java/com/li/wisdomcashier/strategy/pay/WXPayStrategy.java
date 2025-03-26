package com.li.wisdomcashier.strategy.pay;

import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.PayInfo;
import com.li.wisdomcashier.entry.dto.PayVO;
import com.li.wisdomcashier.entry.dto.RefundDTO;
import com.li.wisdomcashier.entry.vo.StatusVO;
import com.li.wisdomcashier.enums.pay.PayEnums;

/**
 * @ClassName WXPayStrategy
 * @Description TODO
 * @Author Nine
 * @Date 2025/3/26 14:46
 * @Version 1.0
 */
public class WXPayStrategy extends AbstractPayStrategy{
    @Override
    protected PayEnums getTypeEnum() {
        return PayEnums.WX;
    }

    @Override
    public PayVO pay(PayDTO payDTO) {
        return null;
    }

    @Override
    public StatusVO status(String tradeNo) {
        return null;
    }

    @Override
    public String cancel(String tradeNo) {
        return "";
    }

    @Override
    public String close(String tradeNo) {
        return "";
    }

    @Override
    public PayInfo detail(String tradeNo) {
        return null;
    }

    @Override
    public String refund(RefundDTO refundDTO) {
        return "";
    }
}
