package com.li.wisdomcashier.entry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName QueryTrade
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/8 19:30
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayInfo implements Serializable {
    String payUser;
    String sum;
    String tradeNo;
    String remoteNo;
    String payUserId;
}
