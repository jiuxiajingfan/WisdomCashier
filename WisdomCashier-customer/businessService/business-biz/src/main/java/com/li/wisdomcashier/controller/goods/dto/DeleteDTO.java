package com.li.wisdomcashier.controller.goods.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * @ClassName DeleteDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/20 19:20
 * @Version 1.0
 */
@Data
public class DeleteDTO {
    @NotBlank(message = "店铺ID不能为空")
    private String sid;

    @NotBlank(message = "ID不能为空！")
    private String id;
}
