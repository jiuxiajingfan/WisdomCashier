package com.li.WisdomCashier.controller.shop.shopApply;

import com.li.WisdomCashier.controller.shop.shopApply.dto.ShopApplyDTO;
import com.li.WisdomCashier.controller.shop.shopApply.vo.ShopApplyVO;
import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.service.ShopApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
public class ShopApplyControlly {
    @Resource
    private ShopApplyService shopApplyService;

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

}
