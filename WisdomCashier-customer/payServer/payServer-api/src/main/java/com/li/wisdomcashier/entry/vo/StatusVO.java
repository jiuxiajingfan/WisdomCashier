package com.li.wisdomcashier.entry.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class StatusVO implements Serializable {
    String code;
    String status;
}
