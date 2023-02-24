package com.li.wisdomcashier.customer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.ShopQueryDTO;
import com.li.wisdomcashier.base.service.ShopService;
import com.li.wisdomcashier.base.entity.vo.ShopVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@Api(tags = {"店铺相关"})
@RestController
@RequestMapping("/Shop")
public class ShopController {
    @Resource
    private ShopService shopService;

    @ApiOperation(value = "获取用户相关店铺信息")
    @GetMapping("/getUserShop")
    private R<List<ShopVO>> getUserShop(String shopName){
        return shopService.getUserShop(shopName);
    }
    @ApiModelProperty(value = "分页获取用户相关店铺")
    @PostMapping("/getUserShopPage")
    private R<IPage<ShopVO>> getUserShopPage(ShopQueryDTO shopQueryDTO){
        return shopService.getUserShopPage(shopQueryDTO);
    }
}
