package com.li.wisdomcashier.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName PageDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/2/24 16:34
 * @Version 1.0
 */
@Data
public class PageDTO {
    @NotNull(message = "页码不可为空")
    @ApiModelProperty(value = "页码")
    private Integer current=1;

    @NotNull(message = "页大小不可为空")
    @ApiModelProperty(value = "页大小")
    private Integer pageSize=10;
}
