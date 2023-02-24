package com.li.wisdomcashier.base.entity.vo;

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
    Long id;
    String shopName;
    @ApiModelProperty(value = "权限")
    Integer role;
}
