package com.li.wisdomcashier.customer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.common.UnCheck;
import com.li.wisdomcashier.base.entity.dto.BuyGoodDTO;
import com.li.wisdomcashier.base.entity.dto.GoodDTO;
import com.li.wisdomcashier.base.entity.dto.GoodQueryDTO;
import com.li.wisdomcashier.base.entity.po.Goods;
import com.li.wisdomcashier.base.entity.po.GoodsVO;
import com.li.wisdomcashier.base.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName GoodsController
 * @Description TODO
 * @Author Nine
 * @Date 2023/2/26 15:46
 * @Version 1.0
 */
@RestController
@RequestMapping("/Goods")
@Api(tags = {"商品相关"})
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    @GetMapping("/reqGood")
    @ApiOperation(value = "根据条形码查询商品信息（爬虫）")
    public R<Goods> reqGood(String gid) {
        return goodsService.reqGood(gid);
    }

    @PostMapping("/addGood")
    @ApiOperation(value = "新增商品")
    public R<String> addGood(@Validated @RequestBody GoodDTO good) {
        return goodsService.addGood(good);
    }

    @PostMapping("/getGoodPage")
    @ApiOperation(value = "根据条形码获取商品信息(分页）")
    public R<IPage<Goods>> getGoodPage(@Validated @RequestBody GoodQueryDTO goodQueryDTO) {
        return goodsService.getGoodPage(goodQueryDTO);
    }

    @PostMapping("/updateGood")
    @ApiOperation(value = "更新商品")
    public R<String> updateGood(@Validated @RequestBody GoodDTO good) {
        return goodsService.updateGood(good);
    }

    @GetMapping("/getGood")
    @ApiOperation(value = "根据条形码查询商品信息")
    public R<GoodsVO> getGood(String gid, String sid) {
        return goodsService.getGood(gid, Long.parseLong(sid));
    }

    @PostMapping("/buyGood")
    @ApiOperation(value = "商品交易")
    R<String> buyGood(@RequestBody @Validated BuyGoodDTO buyGoodDTO){
        return goodsService.buyGood(buyGoodDTO);
    }

    @GetMapping("/getRandID")
    @ApiOperation(value = "返回一个随机订单号")
    R<String> getRandID(){
        return goodsService.getRandID();
    }


}
