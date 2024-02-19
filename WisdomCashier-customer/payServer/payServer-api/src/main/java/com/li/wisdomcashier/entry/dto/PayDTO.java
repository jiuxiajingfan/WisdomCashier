package com.li.wisdomcashier.entry.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName PayDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/5 15:49
 * @Version 1.0
 */

@Data
public class PayDTO implements Serializable {

    /**
     * 店铺订单号；
     */
    private String shopID;

    /**
     * 远程订单号
     */
    private String remoteID;

    /**
     * 远程调用信息
     */
    private String msg;

}
