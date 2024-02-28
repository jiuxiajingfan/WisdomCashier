package com.li.wisdomcashier.controller.goods.dto;

import com.li.wisdomcashier.entry.dto.BuyGoodsDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @ClassName BuyGoodDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/3 12:49
 * @Version 1.0
 */
@Data
public class BuyDTO {

    @Schema(description = "店铺id")
    @NotNull(message = "店铺id不能为空！")
    String sid;

    @Schema(description = "总价")
    @NotNull(message = "总价不能为空")
    String sum;

    @Schema(description = "订单消息")
    String msg;

    Integer vip;

    String phone;

    @NotEmpty(message = "商品不能为空！")
    List<BuyGoodsDTO> goods;
}
