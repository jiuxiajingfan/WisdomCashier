package com.li.WisdomCashier.controller.shop.shopApply.vo;

import com.li.WisdomCashier.entry.ShopApply;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ShopApplyVO
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/24 13:36
 * @Version 1.0
 */
@Data
public class ShopApplyVO {
    List<ShopApply> list;
    Boolean flag;
}
