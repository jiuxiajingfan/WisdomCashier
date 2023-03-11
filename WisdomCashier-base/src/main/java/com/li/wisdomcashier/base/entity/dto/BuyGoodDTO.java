package com.li.wisdomcashier.base.entity.dto;

import com.li.wisdomcashier.base.entity.po.Goods;
import com.li.wisdomcashier.base.entity.po.GoodsVO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName BuyGoodDTO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/3 12:49
 * @Version 1.0
 */
@Data
public class BuyGoodDTO {

    @ApiModelProperty("商品数组")
    @NotEmpty(message = "商品不能为空！")
    List<Goods> goods;

    @ApiModelProperty("店铺id")
    @NotNull(message = "店铺id不能为空！")
    String sid;

    @ApiModelProperty("支付类型 1现金 2wx 3zfb")
    @NotNull(message = "支付类型不能为空！")
    int type;

    @ApiModelProperty("总价")
    @NotNull(message = "总价不能为空")
    String sum;

    @ApiModelProperty("远程订单号")
    String remoteNo;

    @ApiModelProperty("订单状态")
    Integer status;

    @ApiModelProperty("订单消息")
    String msg;

    String id;
}
