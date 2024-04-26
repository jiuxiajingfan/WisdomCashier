package com.li.wisdomcashier.controller.shop;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.controller.shop.shopApply.dto.ShopApplyDTO;
import com.li.wisdomcashier.controller.shop.shopApply.dto.ShopQueryDTO;
import com.li.wisdomcashier.controller.shop.shopApply.vo.ShopApplyVO;
import com.li.wisdomcashier.controller.shop.shopApply.vo.ShopVO;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.service.ShopApplyService;
import com.li.wisdomcashier.service.ShopCategoryService;
import com.li.wisdomcashier.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import java.util.List;

/**
 * @ClassName ShopApplyControlly
 * @Description TODO
 * @Author Nine
 * @Date 2023/11/8 20:41
 * @Version 1.0
 */

@Tag(name = "店铺相关")
@RestController
@RequestMapping("/shop")
public class ShopController {
    @Resource
    private ShopApplyService shopApplyService;

    @Resource
    private ShopService shopService;

    @Resource
    private ShopCategoryService shopCategoryService;


    @Operation(summary = "申请进度")
    @GetMapping("/getApply")
    public R<ShopApplyVO> getApply() {
        return shopApplyService.getApply();
    }

    @Operation(summary = "申请店铺")
    @PostMapping("/applyShop")
    public R<String> applyShop(@RequestBody @Validated ShopApplyDTO shopApplyDTO) {
        return shopApplyService.applyShop(shopApplyDTO);
    }

    @Operation(summary = "分页获取用户相关店铺")
    @PostMapping("/getUserShopPage")
    private R<IPage<ShopVO>> getUserShopPage(@RequestBody ShopQueryDTO shopQueryDTO) {
        return shopService.getUserShopPage(shopQueryDTO);
    }

    @Operation(summary = "获取店铺菜单")
    @GetMapping("/getShopMenu")
    @PreAuthorize("@ss.hasPermission(#sid,3,2,1)")
    public R<List<Tree<String>>> getMenu(String shopId) {
        return shopService.getMenu(Long.parseLong(shopId));
    }

    @Operation(summary = "获取店铺分类")
    @GetMapping("/getCategory")
    @PreAuthorize("@ss.hasPermission(#sid,3,2,1)")
    R<List<String>> getCategory(String sid) {
        return shopCategoryService.getCategory(sid);
    }

    @Operation(summary = "支持支付状态")
    @GetMapping("/getTradeStatus")
    @PreAuthorize("@ss.hasPermission(#sid,3,2,1)")
    R<List<Integer>> getTradeStatus(String sid) {
        return shopService.getTradeStatus(sid);
    }

    @Operation(summary = "判断是否是店铺会员")
    @GetMapping("/isVip")
    @PreAuthorize("@ss.hasPermission(#sid,3,2,1)")
    public R<Long> isVip(String sid, String phone) {
        return shopService.isVip(sid, phone);
    }

}
