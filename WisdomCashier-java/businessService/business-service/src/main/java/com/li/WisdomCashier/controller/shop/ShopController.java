package com.li.WisdomCashier.controller.shop.shopApply;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.WisdomCashier.controller.shop.shopApply.dto.ShopApplyDTO;
import com.li.WisdomCashier.controller.shop.shopApply.dto.ShopQueryDTO;
import com.li.WisdomCashier.controller.shop.shopApply.vo.ShopApplyVO;
import com.li.WisdomCashier.controller.shop.shopApply.vo.ShopVO;
import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.service.ShopApplyService;
import com.li.WisdomCashier.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ShopApplyControlly
 * @Description TODO
 * @Author Nine
 * @Date 2023/11/8 20:41
 * @Version 1.0
 */

@Api(tags = {"店铺相关"})
@RestController
@RequestMapping("/shop")
public class ShopController {
    @Resource
    private ShopApplyService shopApplyService;

    @Resource
    private ShopService shopService;

    @ApiOperation(value = "申请进度")
    @GetMapping("/getApply")
    public R<ShopApplyVO> getApply(){
        return shopApplyService.getApply();
    }

    @ApiOperation(value = "申请店铺")
    @PostMapping("/applyShop")
    public R<String> applyShop(@RequestBody @Validated ShopApplyDTO shopApplyDTO){
        return shopApplyService.applyShop(shopApplyDTO);
    }

    @ApiOperation(value = "分页获取用户相关店铺")
    @PostMapping("/getUserShopPage")
    private R<IPage<ShopVO>> getUserShopPage(@RequestBody ShopQueryDTO shopQueryDTO){
        return shopService.getUserShopPage(shopQueryDTO);
    }

    @ApiOperation(value = "获取店铺菜单")
    @GetMapping("/getShopMenu")
    public R<List<Tree<String>>> getMenu(String shopId){
        return shopService.getMenu(Long.parseLong(shopId));
    }

}
