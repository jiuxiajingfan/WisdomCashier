package com.li.wisdomcashier.entry.dto;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName AliPayDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/4 16:33
 * @Version 1.0
 */
@Data
public class PayDTO implements Serializable {
    String shopId;
    String shopName;
    String price;
    /**
     * 代调token
     */
    String authToken;
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
