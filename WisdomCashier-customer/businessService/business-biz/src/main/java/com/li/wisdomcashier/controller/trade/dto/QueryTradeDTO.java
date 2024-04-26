package com.li.wisdomcashier.controller.trade.dto;

import com.li.wisdomcashier.entry.PageDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class QueryTradeDTO extends PageDTO {
    private String id;
    private LocalDate startTime;
    private LocalDate endTime;
    private List<Integer> status;
    @NotBlank(message = "店铺id不能为空")
    private String sid;
}
