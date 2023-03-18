package com.li.wisdomcashier.base.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName ShopMessageDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/18 15:35
 * @Version 1.0
 */
@Data
public class ShopMessageDTO {

    @NotBlank(message = "店铺ID不能为空")
    String sid;

    @NotBlank(message = "店铺名不能为空")
    String name;

    @NotBlank(message = "店铺描述不能为空")
    String desc;

}
