package com.li.wisdomcashier.enums.trade;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName TradeEnum
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/7 13:47
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum TradeEnum {

    WAITING(0,"等待付款"),
    FAIL(1,"失败"),
    CANCEL(2,"取消"),
    FINISH(3,"完成可退款"),
    REFUND(4,"部分退款"),
    REFUNDALL(5,"全额退款"),
    FINAL(6,"完成不可退款"),
    STOP(7,"未知错误交易停止");




    private Integer code;
    private String des;
}
