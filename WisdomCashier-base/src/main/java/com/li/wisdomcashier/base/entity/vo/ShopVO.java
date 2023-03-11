package com.li.wisdomcashier.base.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ShopVo
 * @Description TODO
 * @Author Nine
 * @Date 2022/12/2 14:34
 * @Version 1.0
 */
@Data
public class ShopVO {
    @JsonSerialize(using= ToStringSerializer.class)
    Long id;
    String shopName;
    @ApiModelProperty(value = "权限")
    Integer role;
}
