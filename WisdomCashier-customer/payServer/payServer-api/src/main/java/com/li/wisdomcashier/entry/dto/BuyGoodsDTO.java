package com.li.wisdomcashier.entry.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BuyGoodsDTO {
    String name;
    String gid;
    BigDecimal priceOut;
    BigDecimal priceIn;
    BigDecimal priceVip;
    Integer num;
    String type;
}
