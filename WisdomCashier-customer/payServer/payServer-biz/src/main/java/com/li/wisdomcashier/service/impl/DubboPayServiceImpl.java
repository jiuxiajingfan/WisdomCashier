package com.li.wisdomcashier.service.impl;

import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.PayInfo;
import com.li.wisdomcashier.entry.dto.PayVO;
import com.li.wisdomcashier.entry.dto.RefundDTO;
import com.li.wisdomcashier.entry.vo.StatusVO;
import com.li.wisdomcashier.service.DubboPayService;
import com.li.wisdomcashier.strategy.pay.AbstractPayStrategy;
import com.li.wisdomcashier.strategy.pay.PayStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @ClassName PayServiceImpl
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/4 16:32
 * @Version 1.0
 */
@Service
@Slf4j
@DubboService(version = "1.0")
public class DubboPayServiceImpl implements DubboPayService {

    @Override
    public PayVO pay(PayDTO payDTO) {
        AbstractPayStrategy payStrategy = PayStrategyFactory.getPayStrategy(payDTO.getType());
        if (Objects.isNull(payStrategy)) {
            log.error("支付接口调用错误！无该类型{}", payDTO.getType());
            return null;
        }
        return payStrategy.pay(payDTO);
    }

    @Override
    public StatusVO status(Integer type, String tradeNo) {
        AbstractPayStrategy payStrategy = PayStrategyFactory.getPayStrategy(type);
        if (Objects.isNull(payStrategy)) {
            log.error("支付接口调用错误！无该类型{}", type);
            return null;
        }
        return payStrategy.status(tradeNo);
    }

    @Override
    public String cancel(Integer type, String tradeNo) {
        AbstractPayStrategy payStrategy = PayStrategyFactory.getPayStrategy(type);
        if (Objects.isNull(payStrategy)) {
            log.error("支付接口调用错误！无该类型{}", type);
            return null;
        }
        return payStrategy.cancel(tradeNo);
    }

    @Override
    public String close(Integer type, String tradeNo) {
        AbstractPayStrategy payStrategy = PayStrategyFactory.getPayStrategy(type);
        if (Objects.isNull(payStrategy)) {
            log.error("支付接口调用错误！无该类型{}", type);
            return null;
        }
        return payStrategy.close(tradeNo);
    }

    @Override
    public PayInfo detail(Integer type, String tradeNo) {
        AbstractPayStrategy payStrategy = PayStrategyFactory.getPayStrategy(type);
        if (Objects.isNull(payStrategy)) {
            log.error("支付接口调用错误！无该类型{}", type);
            return null;
        }
        return payStrategy.detail(tradeNo);
    }

    @Override
    public String refund(RefundDTO refundDTO) {
        AbstractPayStrategy payStrategy = PayStrategyFactory.getPayStrategy(refundDTO.getType());
        if (Objects.isNull(payStrategy)) {
            log.error("支付接口调用错误！无该类型{}", refundDTO.getType());
            return null;
        }
        return payStrategy.refund(refundDTO);
    }


}
