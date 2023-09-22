package com.li.WisdomCashier.entry.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO {
    String token;
    String refresh;
}
