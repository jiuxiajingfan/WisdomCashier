package com.li.wisdomcashier.strategy.pay;

import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.PayInfo;
import com.li.wisdomcashier.entry.dto.PayVO;
import com.li.wisdomcashier.entry.dto.RefundDTO;
import com.li.wisdomcashier.entry.vo.StatusVO;
import com.li.wisdomcashier.enums.pay.PayEnums;
import com.li.wisdomcashier.enums.trade.TradeEnum;
import org.springframework.stereotype.Component;

/**
 * @ClassName CashPayStrategy
 * @Description 现金支付策略
 * @Author Nine
 * @Date 2024/3/13 19:14
 * @Version 1.0
 */

@Component
public class CashPayStrategy extends AbstractPayStrategy {
    @Override
    protected PayEnums getTypeEnum() {
        return PayEnums.XJ;
    }

    @Override
    public PayVO pay(PayDTO dto) {
        return PayVO.builder()
                .success(true)
                .remoteID(null)
                .shopID(dto.getShopId())
                .build();
    }

    @Override
    public StatusVO status(String tradeNo) {
        return StatusVO.builder()
                .code(TradeEnum.FINISH.getCode())
                .status(TradeEnum.FINISH.getDes())
                .build();
    }

    @Override
    public String cancel(String tradeNo) {
        return null;
    }

    @Override
    public String close(String tradeNo) {
        return null;
    }

    @Override
    public PayInfo detail(String tradeNo) {
        return null;
    }

    @Override
    public String refund(RefundDTO refundDTO) {
        return null;
    }
}
