package com.li.wisdomcashier.base.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @ClassName GoodDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/2/27 12:47
 * @Version 1.0
 */
@Data
public class GoodDTO {
    @NotBlank(message = "商品名不能为空！")
    @ApiModelProperty("商品名")
    private String name;

    @NotBlank(message = "商品条码号不能为空！")
    @ApiModelProperty("商品条码")
    private String gid;

    @ApiModelProperty("进价")
    private Double priceIn;

    @NotNull(message = "商品售价不能为空！")
    @ApiModelProperty("售价")
    private Double priceOut;

    @NotNull(message = "商店id不能为空!")
    @ApiModelProperty("商店id")
    private Long sid;

    @ApiModelProperty("生产日期")
    private LocalDate date;

    @NotNull(message = "保质期不能为空！")
    @ApiModelProperty("保质期")
    private Integer shelfLife;

    @NotNull(message = "数量不能为空!")
    @ApiModelProperty("数量")
    private Long num;

    @ApiModelProperty("图片地址")
    private String picUrl;

    @ApiModelProperty("利润")
    private Double profit;

    @ApiModelProperty("计量")
    private String metrology;
}
