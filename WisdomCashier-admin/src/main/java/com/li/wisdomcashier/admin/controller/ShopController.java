package com.li.wisdomcashier.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.DeleteDTO;
import com.li.wisdomcashier.base.entity.dto.PageDTO;
import com.li.wisdomcashier.base.entity.dto.ShopApprovalDTO;
import com.li.wisdomcashier.base.entity.dto.ShopQueryDTO;
import com.li.wisdomcashier.base.entity.vo.ShopApplyVO2;
import com.li.wisdomcashier.base.entity.vo.ShopVO;
import com.li.wisdomcashier.base.service.ShopApplyService;
import com.li.wisdomcashier.base.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName ShopController
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/30 15:12
 * @Version 1.0
 */
@RestController
@RequestMapping("/shop")
@Api(tags = {"店铺相关"})
public class ShopController {

    @Resource
    private ShopService shopService;

    @Resource
    private ShopApplyService shopApplyService;


    @PostMapping("/getShopPage")
    @ApiOperation(value = "获取当前店铺列表")
    public R<IPage<ShopVO>> getShopPage(@Validated @RequestBody ShopQueryDTO queryDTO){
        return shopService.getShopPage(queryDTO);
    }

    @PostMapping("/changeShopStatus")
    @ApiOperation(value = "更改店铺状态")
    public R<String> changeUserStatus(@Validated @RequestBody DeleteDTO deleteDTO){
        return shopService.changeUserStatus(deleteDTO);
    }
    @PostMapping("/getApplyPage")
    @ApiOperation(value = "申请列表")
    public R<IPage<ShopApplyVO2>> getApplyPage(@Validated @RequestBody PageDTO pageDTO){
        return shopApplyService.getApplyPage(pageDTO);
    }

    @PostMapping("/changeApplyStatus")
    @ApiOperation(value = "申请批准")
    public R<String> changeApplyStatus(@Validated @RequestBody ShopApprovalDTO shopApprovalDTO){
        return shopApplyService.changeApplyStatus(shopApprovalDTO);
    }
}
