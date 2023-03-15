package com.li.wisdomcashier.base.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @ClassName QueryMoneyDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/14 20:08
 * @Version 1.0
 */
@Data
public class QueryMoneyDTO {
    @NotBlank(message = "店铺ID不能为空！")
    String sid;
    /**
     * 展示金额还是订单数 0金额 1订单数
     */
    @NotNull(message = "数据类型不能为空")
    Integer type;

    /**
     * 展示日还是月 0日 1月
     */
    @NotNull(message = "日期类型不能为空")
    Integer timeType;

    LocalDateTime timeStart;

    LocalDateTime timeEnd;

}
