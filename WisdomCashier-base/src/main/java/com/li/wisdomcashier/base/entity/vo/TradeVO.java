package com.li.wisdomcashier.base.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName TradeDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/9 13:27
 * @Version 1.0
 */
@Data
public class TradeVO {
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    private LocalDateTime createTime;
    private Double income;
    private Integer type;
    private Integer status;
    private String msg;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long operater;
}
