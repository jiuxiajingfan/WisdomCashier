package com.li.wisdomcashier.base.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName AddVipDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/21 14:02
 * @Version 1.0
 */

@Data
public class AddVipDTO {
    @NotBlank
    private String sid;
    @NotBlank
    private String phone;
    @NotBlank
    private String sex;
    @NotBlank
    private String age;
    @NotNull
    private Integer limit;
}
