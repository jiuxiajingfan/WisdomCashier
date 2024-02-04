package com.li.wisdomcashier.controller.shop.shopApply.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName ShopVo
 * @Description TODO
 * @Author Nine
 * @Date 2022/12/2 14:34
 * @Version 1.0
 */
@Data
public class ShopVO {
    @JsonSerialize(using = ToStringSerializer.class)
    Long id;
    String shopName;
    @Schema(name = "权限")
    Integer role;
    LocalDateTime createTime;
    String desc;
    Integer wx;
    Integer zfb;
}
