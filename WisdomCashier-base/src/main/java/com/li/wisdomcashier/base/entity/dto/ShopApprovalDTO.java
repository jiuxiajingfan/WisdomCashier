package com.li.wisdomcashier.base.entity.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName ShopApprovalDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/30 18:53
 * @Version 1.0
 */
@Data
public class ShopApprovalDTO {

    @NotBlank(message = "ID不能为空！")
    String id;

    @Max(value = 3,message = "请填写正确结果")
    @Min(value = 2,message = "请填写正确结果")
    @NotNull(message = "结果不能为空！")
    Integer type;

    String msg;
}
