package com.li.wisdomcashier.controller.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.controller.goods.dto.*;
import com.li.wisdomcashier.controller.goods.vo.GoodsVO;
import com.li.wisdomcashier.entry.Goods;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.service.GoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * @ClassName GoodsController
 * @Description TODO
 * @Author Nine
 * @Date 2023/2/26 15:46
 * @Version 1.0
 */
@RestController
@RequestMapping("/Goods")
@Tag(name = "商品相关")
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    @GetMapping("/reqGood")
    @Operation(summary = "根据条形码查询商品信息（爬虫）")
    public R<Goods> reqGood(String gid) {
        return goodsService.reqGood(gid);
    }

    @PostMapping("/addGood")
    @Operation(summary = "新增商品")
    public R<String> addGood(@Validated @RequestBody GoodsDTO good) {
        return goodsService.addGood(good);
    }

    @PostMapping("/getGoodPage")
    @Operation(summary = "根据条形码获取商品信息(分页）")
    public R<IPage<Goods>> getGoodPage(@Validated @RequestBody GoodQueryDTO goodQueryDTO) {
        return goodsService.getGoodPage(goodQueryDTO);
    }

    @PostMapping("/updateGood")
    @Operation(summary = "更新商品")
    public R<String> updateGood(@Validated @RequestBody GoodsDTO good) {
        return goodsService.updateGood(good);
    }

    @GetMapping("/getGood")
    @Operation(summary = "根据条形码查询商品信息")
    public R<GoodsVO> getGood(String gid, String sid) {
        return goodsService.getGood(gid, Long.parseLong(sid));
    }

    @PostMapping("/buyGood")
    @Operation(summary = "商品交易")
    R<String> buyGood(@RequestBody @Validated BuyGoodDTO buyGoodDTO) {
        return goodsService.buyGood(buyGoodDTO);
    }

    @GetMapping("/getRandID")
    @Operation(summary = "返回一个随机订单号")
    R<String> getRandID() {
        return goodsService.getRandID();
    }

    @PostMapping("/deleteGood")
    @Operation(summary = "删除商品")
    public R<String> deleteGood(@RequestBody @Validated DeleteDTO deleteDTO) {
        return goodsService.deleteGood(deleteDTO);
    }


    @PostMapping("/getGoodTemporaryPage")
    @Operation(summary = "获取临期商品")
    R<IPage<Goods>> getGoodTemporaryPage(@RequestBody @Validated GoodQueryDTO goodQueryDTO) {
        return goodsService.getGoodTemporaryPage(goodQueryDTO);
    }

    @PostMapping("/updateGoodImg")
    @Operation(summary = "更新商品图片")
    public R<String> updateGoodImg(@RequestBody @Validated PayDTO payDTO) {
        return goodsService.updateGoodImg(payDTO);
    }


}
