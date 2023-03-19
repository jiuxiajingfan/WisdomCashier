package com.li.wisdomcashier.base.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName QueryEmDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/19 13:31
 * @Version 1.0
 */
@Data
public class QueryEmDTO extends PageDTO {
    @NotBlank(message = "店铺ID不能为空")
    String sid;

    String name;
}
