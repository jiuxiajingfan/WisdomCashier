package com.li.wisdomcashier.base.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName ShopApplyDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/23 18:53
 * @Version 1.0
 */
@Data
public class ShopApplyDTO {

    @NotBlank
    String name;
    @NotBlank
    String descript;
    @NotBlank
    String imgShop;
    @NotBlank
    String imgIdcard1;
    @NotBlank
    String imgIdcard2;
    @NotBlank
    String code;
}
