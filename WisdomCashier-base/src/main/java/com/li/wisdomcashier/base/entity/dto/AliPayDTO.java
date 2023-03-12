package com.li.wisdomcashier.base.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alipay.api.internal.mapping.ApiField;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
    String id;
    String price;

    /**
     * 商户操作员编号。
     */
    private String operatorId;

    /**
     * 付款用户ID
     */
    String userID;
}
