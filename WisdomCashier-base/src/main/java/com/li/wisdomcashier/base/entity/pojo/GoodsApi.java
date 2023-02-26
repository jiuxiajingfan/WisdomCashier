package com.li.wisdomcashier.base.entity.pojo;

import lombok.Data;

/**
 * @ClassName GoodsApi
 * @Description 商品条形码api查询返回信息
 * @Author Nine
 * @Date 2023/2/26 16:20
 * @Version 1.0
 */
@Data
public class GoodsApi {
    private String goodsName;

    private String barcode;

    private String price;

    private String brand;

    private String supplier;

    private String standard;
}
