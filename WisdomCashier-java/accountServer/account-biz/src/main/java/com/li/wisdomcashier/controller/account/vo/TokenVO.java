package com.li.wisdomcashier.controller.account.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenVO {
    String token;
    String refresh;
}
