package com.li.wisdomcashier.entry.dto;

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
    String shopId;
    String price;

    /**
     * 商户操作员编号。
     */
    private String operatorId;

    /**
     * 付款用户ID
     */
    String userID;
    /**
     * 订单id
     */
    String id;
}
