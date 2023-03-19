package com.li.wisdomcashier.customer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.QueryEmDTO;
import com.li.wisdomcashier.base.entity.dto.ShopMessageDTO;
import com.li.wisdomcashier.base.entity.dto.ShopQueryDTO;
import com.li.wisdomcashier.base.entity.po.SysMenu;
import com.li.wisdomcashier.base.entity.vo.UserVo;
import com.li.wisdomcashier.base.service.ShopCategoryService;
import com.li.wisdomcashier.base.service.ShopService;
import com.li.wisdomcashier.base.entity.vo.ShopVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Resource
    private ShopCategoryService shopCategoryService;

    @ApiOperation(value = "获取用户相关店铺信息")
    @GetMapping("/getUserShop")
    private R<List<ShopVO>> getUserShop(String shopName){
        return shopService.getUserShop(shopName);
    }
    @ApiModelProperty(value = "分页获取用户相关店铺")
    @PostMapping("/getUserShopPage")
    private R<IPage<ShopVO>> getUserShopPage(@RequestBody ShopQueryDTO shopQueryDTO){
        return shopService.getUserShopPage(shopQueryDTO);
    }

    @ApiOperation(value = "获取店铺菜单")
    @GetMapping("/getShopMenu")
    public R<List<SysMenu>> getMenu(String shopId){
        return shopService.getMenu(Long.parseLong(shopId));
    }

    @ApiOperation(value = "获取店铺分类")
    @GetMapping("/getCategory")
    R<List<String>> getCategory(String sid){
        return shopCategoryService.getCategory(sid);
    }

    @ApiOperation(value = "新增店铺分类")
    @GetMapping("/addCategory")
    R<String> addCategory(String sid, String category){
        return shopCategoryService.addCategory(sid,category);
    }

    @ApiOperation(value = "删除店铺分类")
    @GetMapping("/delCategory")
    R<String> delCategory(String sid, String category){
        return shopCategoryService.delCategory(sid, category);
    }

    @ApiOperation(value = "店铺信息")
    @GetMapping("/getShopMessageByID")
    public R<ShopVO> getShopMessageByID(String sid){
        return shopService.getShopMessageByID(sid);
    }

    @ApiOperation(value = "更新店铺信息")
    @PostMapping("/updateShopMessage")
    public R<String> updateShopMessage(@RequestBody @Validated ShopMessageDTO shopMessageDTO){
        return shopService.updateShopMessage(shopMessageDTO);
    }

    @ApiOperation(value = "店铺人员列表")
    @PostMapping("/getEmploree")
    public R<IPage<UserVo>> getEmploree(@RequestBody @Validated QueryEmDTO queryEmDTO){
        return shopService.getEmploree(queryEmDTO);
    }
}
