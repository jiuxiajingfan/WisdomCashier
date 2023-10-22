package com.li.WisdomCashier.controller.account.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenVO {
    String token;
    String refresh;
}
