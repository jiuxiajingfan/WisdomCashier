package com.li.wisdomcashier.controller.goods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName GoodDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/2/27 12:47
 * @Version 1.0
 */
@Data
public class GoodsDTO {
    @NotBlank(message = "商品名不能为空！")
    @Schema(description = "商品名")
    private String name;

    @NotBlank(message = "商品条码号不能为空！")
    @Schema(description = "商品条码")
    private String gid;

    @Schema(description = "进价")
    private String priceIn;

    @NotNull(message = "商品售价不能为空！")
    @Schema(description = "售价")
    private String priceOut;

    @NotNull(message = "商品售价不能为空！")
    @Schema(description = "Vip售价")
    private String priceVip;

    @NotNull(message = "商店id不能为空!")
    @Schema(description = "商店id")

    private String sid;

    @Schema(description = "生产日期")
    private LocalDate date;

    @NotNull(message = "保质期不能为空！")
    @Schema(description = "保质期")
    private Integer shelfLife;

    @NotNull(message = "数量不能为空!")
    @Schema(description = "数量")
    private Integer num;

    @Schema(description = "图片地址")
    private String picUrl;

    @Schema(description = "利润")
    private String profit;

    @Schema(description = "计量")
    private String metrology;

    @Schema(description = "分类")
    private String type;
}
