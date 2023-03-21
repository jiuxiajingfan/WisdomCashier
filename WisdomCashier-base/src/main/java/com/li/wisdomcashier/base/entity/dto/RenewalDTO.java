package com.li.wisdomcashier.base.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName RenewalDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/21 16:03
 * @Version 1.0
 */
@Data
public class RenewalDTO {
    @NotBlank
    String sid;

    @NotBlank
    String phone;

    @NotNull
    Integer limit;
}
