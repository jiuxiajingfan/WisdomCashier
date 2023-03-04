package com.li.wisdomcashier.base.entity.dto;

import com.alipay.api.internal.mapping.ApiField;
import lombok.Data;

/**
 * @ClassName AliPayDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/4 16:33
 * @Version 1.0
 */
@Data
public class AliPayDTO {
    String shopName;
    Long id;
    Double price;

    /**
     * 商户操作员编号。
     */
    private String operatorId;

    /**
     * 付款用户ID
     */
    String userID;
}
