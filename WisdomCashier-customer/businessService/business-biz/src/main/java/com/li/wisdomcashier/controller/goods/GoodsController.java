package com.li.wisdomcashier.controller.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.controller.goods.dto.BuyDTO;
import com.li.wisdomcashier.controller.goods.dto.DeleteDTO;
import com.li.wisdomcashier.controller.goods.dto.GoodQueryDTO;
import com.li.wisdomcashier.controller.goods.dto.GoodsDTO;
import com.li.wisdomcashier.controller.goods.vo.GoodsVO;
import com.li.wisdomcashier.entry.Goods;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.dto.PayVO;
import com.li.wisdomcashier.service.GoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName GoodsController
 * @Description TODO
 * @Author Nine
 * @Date 2023/2/26 15:46
 * @Version 1.0
 */
@RestController
@RequestMapping("/goods")
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
    @PreAuthorize("@ss.hasPermission(#dto.sid,1,2,3)")
    public R<String> addGood(@Validated @RequestBody GoodsDTO dto) {
        return goodsService.addGood(dto);
    }

    @PostMapping("/getGoodPage")
    @Operation(summary = "根据条形码获取商品信息(分页）")
    @PreAuthorize("@ss.hasPermission(#dto.sid,1,2,3)")
    public R<IPage<Goods>> getGoodPage(@Validated @RequestBody GoodQueryDTO dto) {
        return goodsService.getGoodPage(dto);
    }

    @PostMapping("/updateGood")
    @Operation(summary = "更新商品")
    @PreAuthorize("@ss.hasPermission(#dto.sid,1,2,3)")
    public R<String> updateGood(@Validated @RequestBody GoodsDTO dto) {
        return goodsService.updateGood(dto);
    }

    @GetMapping("/getGoods")
    @Operation(summary = "根据条形码查询商品信息")
    @PreAuthorize("@ss.hasPermission(#sid,1,2,3)")
    public R<GoodsVO> getGood(@RequestParam("gid") String gid,@RequestParam("sid") String sid) {
        return goodsService.getGoods(gid, Long.parseLong(sid));
    }

    @PostMapping("/buy")
    @Operation(summary = "商品交易")
    @PreAuthorize("@ss.hasPermission(#dto.sid,1,2,3)")
    R<String> buy(@RequestBody @Validated BuyDTO dto) {
        return goodsService.buy(dto);
    }

    @PostMapping("/deleteGood")
    @Operation(summary = "删除商品")
    @PreAuthorize("@ss.hasPermission(#dto.sid,1,2,3)")
    public R<String> deleteGood(@RequestBody @Validated DeleteDTO dto) {
        return goodsService.deleteGood(dto);
    }


    @PostMapping("/getGoodTemporaryPage")
    @Operation(summary = "获取临期商品")
    @PreAuthorize("@ss.hasPermission(#dto.sid,1,2,3)")
    R<IPage<Goods>> getGoodTemporaryPage(@RequestBody @Validated GoodQueryDTO dto) {
        return goodsService.getGoodTemporaryPage(dto);
    }

    @PostMapping("/updateGoodImg")
    @Operation(summary = "更新商品图片")
    @PreAuthorize("@ss.hasPermission(#dto.shopID,1,2,3)")
    public R<String> updateGoodImg(@RequestBody @Validated PayVO dto) {
        return goodsService.updateGoodImg(dto);
    }


}
