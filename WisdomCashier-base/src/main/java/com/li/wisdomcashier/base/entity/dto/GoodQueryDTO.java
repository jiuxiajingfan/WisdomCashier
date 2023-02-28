package com.li.wisdomcashier.base.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName GoodQueryDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/2/27 15:54
 * @Version 1.0
 */
@Data
public class GoodQueryDTO extends PageDTO {
    @NotNull
    Long sid;

    String gid;

}