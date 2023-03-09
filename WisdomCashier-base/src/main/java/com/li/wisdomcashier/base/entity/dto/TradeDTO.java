package com.li.wisdomcashier.base.entity.dto;

import com.li.wisdomcashier.base.entity.po.TradeGoods;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName TradeDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/9 13:27
 * @Version 1.0
 */
@Data
public class TradeDTO {
    private Long id;
    private LocalDateTime createTime;
    private Double income;
    private Integer type;
    private Integer status;
    private String msg;
}
