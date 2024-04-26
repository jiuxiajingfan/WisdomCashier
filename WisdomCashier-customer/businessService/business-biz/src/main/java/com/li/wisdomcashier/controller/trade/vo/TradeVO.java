package com.li.wisdomcashier.controller.trade.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TradeVO {
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    private LocalDateTime createTime;
    private String income;
    private Integer type;
    private Integer status;
    private String msg;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long operator;
}
