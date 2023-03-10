package com.li.wisdomcashier.base.entity.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * @ClassName QueryTradeDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/10 16:07
 * @Version 1.0
 */

@Data
public class QueryTradeDTO extends PageDTO {
    private Long id;
    private LocalDate startTime;
    private LocalDate endTime;
    private List<Integer> status;
    @NotNull(message = "店铺id不能为空")
    private Long sid;
}
