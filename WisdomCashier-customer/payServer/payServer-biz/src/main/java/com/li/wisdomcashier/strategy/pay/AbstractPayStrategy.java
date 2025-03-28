package com.li.wisdomcashier.strategy.pay;

import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.PayInfo;
import com.li.wisdomcashier.entry.dto.PayVO;
import com.li.wisdomcashier.entry.dto.RefundDTO;
import com.li.wisdomcashier.entry.vo.StatusVO;
import com.li.wisdomcashier.enums.pay.PayEnums;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 支付策略
 */
public abstract class AbstractPayStrategy {

    /**
     * 获取自身枚举用于映射
     *
     * @return
     */
    protected abstract PayEnums getTypeEnum();

    /**
     * 向工厂注册自身
     */
    @PostConstruct
    private void init() {
        PayStrategyFactory.register(getTypeEnum().getType(), this);
    }

    /**
     * 支付
     *
     * @param payDTO dto
     */
    public abstract PayVO pay(PayDTO payDTO);

    /**
     * 支付状态
     *
     * @param tradeNo 订单号
     */
    public abstract StatusVO status(String tradeNo);

    /**
     * 取消支付
     *
     * @param tradeNo 订单号
     */
    public abstract String cancel(String tradeNo);

    /**
     * 关闭订单
     *
     * @param tradeNo 订单号
     */
    public abstract String close(String tradeNo);

    /**
     * 订单信息
     * @param tradeNo 订单号
     */
    public abstract PayInfo detail(String tradeNo);

    /**
     * 退款
     * @param refundDTO dto
     */
    public abstract String refund(RefundDTO refundDTO);

    /**
     * 支付回调
     * @param request
     * @return
     */
    public abstract Object payCallback(HttpServletRequest request);


    /**
     * 退款回调
     * @param request
     * @return
     */
    public abstract Object refundCallback(HttpServletRequest request);
}
