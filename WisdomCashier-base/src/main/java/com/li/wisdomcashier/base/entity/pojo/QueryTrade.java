package com.li.wisdomcashier.base.entity.pojo;

import lombok.Data;

/**
 * @ClassName QueryTrade
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/8 19:30
 * @Version 1.0
 */
@Data
public class QueryTrade {
    String payUser;
    Double sum;
    String tradeNo;
    String remoteNo;
    String payUserId;
}
