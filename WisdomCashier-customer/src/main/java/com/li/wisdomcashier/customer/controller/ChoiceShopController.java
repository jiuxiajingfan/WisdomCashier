package com.li.wisdomcashier.customer.controller;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.service.ShopService;
import com.li.wisdomcashier.base.service.ShopVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ChioceShopController
 * @Description TODO
 * @Author Nine
 * @Date 2022/12/2 14:08
 * @Version 1.0
 */
@Api(tags = {"选择店铺"})
@RestController
@RequestMapping("/choiceShop")
public class ChoiceShopController {

    @Resource
    private ShopService shopService;

    @ApiOperation(value = "获取用户相关店铺信息")
    @GetMapping("/getUserShop")
    private R<List<ShopVo>> getUserShop(String shopName){
        return shopService.getUserShop(shopName);
    }

}
