package com.li.wisdomcashier.entry.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * @ClassName RefundDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/11 14:20
 * @Version 1.0
 */
@Data
public class RefundDTO {
    @NotBlank(message = "店铺ID不能为空")
    String sid;

    @NotNull(message = "退款金额不能为空")
    String money;

    @NotBlank(message = "订单号不能为空")
    String tradeNo;

    @NotNull(message = "退款原因不能为空")
    String msg;

    @NotNull(message = "标识号不能为空")
    String no;

}
