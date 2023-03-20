package com.li.wisdomcashier.base.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName ApprovalDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/20 15:20
 * @Version 1.0
 */
@Data
public class ApprovalDTO {
    /**
     * 店铺id
     */
    @NotBlank(message = "店铺ID不能为空")
    String sid;

    /**
     * 用户id
     */
    @NotBlank(message = "用户ID不能为空")
    String pid;
    /**
     * 结果
     */
    @NotNull(message = "结果不能为空")
    Integer type;
}
