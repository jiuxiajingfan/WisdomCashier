package com.li.wisdomcashier.controller.shop.shopApply.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName ApplyVO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/20 12:33
 * @Version 1.0
 */
@Data
public class ApplyVO {
    String name;
    @JsonSerialize(using= ToStringSerializer.class)
    Long userId;
    String phone;
    LocalDateTime gmtCreate;
}
