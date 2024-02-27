package com.li.wisdomcashier.entry.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName AliPayDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/4 16:33
 * @Version 1.0
 */
@Data
public class PayDTO implements Serializable {
    @NotNull(message = "支付类型不能为空！")
    int type;
    @NotBlank(message = "店铺id不能为空！")
    String shopId;

    String shopName;

    String price;

    /**
     * 商户操作员编号。
     */
    String operatorId;

    /**
     * 付款用户ID
     */
    String userID;
    /**
     * 订单id
     */
    String id;


    String remoteId;
}
