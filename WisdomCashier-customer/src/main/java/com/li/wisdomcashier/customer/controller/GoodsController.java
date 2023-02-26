package com.li.wisdomcashier.customer.controller;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.common.UnCheck;
import com.li.wisdomcashier.base.entity.po.Goods;
import com.li.wisdomcashier.base.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName GoodsController
 * @Description TODO
 * @Author Nine
 * @Date 2023/2/26 15:46
 * @Version 1.0
 */
@RestController
@RequestMapping("/goods")
@Api(tags = {"商品相关"})
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    @GetMapping("/reqGood")
    @UnCheck
    @ApiOperation(value = "根据条形码查询商品信息")
    R<Goods> reqGood(String gid){
        return goodsService.reqGood(gid);
    }

}
