package com.li.wisdomcashier.customer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.*;
import com.li.wisdomcashier.base.entity.po.SysMenu;
import com.li.wisdomcashier.base.entity.vo.ApplyVO;
import com.li.wisdomcashier.base.entity.vo.UserVo;
import com.li.wisdomcashier.base.entity.vo.VipVO;
import com.li.wisdomcashier.base.service.*;
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
    private ApplyService applyService;

    @Resource
    private ShopCategoryService shopCategoryService;

    @Resource
    private VipService vipService;

    @Resource
    private ShopApplyService shopApplyService;

    @ApiOperation(value = "获取用户相关店铺信息")
    @GetMapping("/getUserShop")
    private R<List<ShopVO>> getUserShop(String shopName){
        return shopService.getUserShop(shopName);
    }
    @ApiOperation(value = "分页获取用户相关店铺")
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

    @ApiOperation(value = "新增店铺人员")
    @GetMapping("/addEmploree")
    public R<String> addEmploree(String sid, String pid){
        return shopService.addEmploree(sid,pid);
    }

    @ApiOperation(value = "员工申请列表")
    @GetMapping("/getApplyList")
    public R<List<ApplyVO>> getApplyList(String sid){
        return applyService.getApplyList(sid);
    }

    @ApiOperation(value = "审批申请")
    @PostMapping("/approval")
    public R<String> approval(@RequestBody @Validated ApprovalDTO approvalDTO){
        return applyService.approval(approvalDTO);
    }

    @ApiOperation(value = "权限更改")
    @PostMapping("/changeRole")
    public R<String> changeRole(@RequestBody @Validated ApprovalDTO approvalDTO){
        return shopService.changeRole(approvalDTO);
    }

    @ApiOperation(value = "删除员工")
    @PostMapping("/deleteEmploree")
    public R<String> deleteEmploree(@RequestBody @Validated DeleteDTO deleteDTO) {
        return shopService.deleteEmploree(deleteDTO);
    }

    @ApiOperation(value = "会员列表（部分信息）")
    @PostMapping("/getVipPage")
    public R<IPage<VipVO>> getVipPage(@RequestBody @Validated GoodQueryDTO goodQueryDTO){
        return vipService.getVipPage(goodQueryDTO);
    }
    @ApiOperation(value = "新增会员")
    @PostMapping("/addVip")
    public R<String> addVip(@RequestBody @Validated AddVipDTO addVipDTO){
        return vipService.addVip(addVipDTO);
    }

    @ApiOperation(value = "会员续费")
    @PostMapping("/renewalVip")
    public R<String> renewalVip(@RequestBody @Validated RenewalDTO renewalDTO){
        return vipService.renewalVip(renewalDTO);
    }

    @ApiOperation(value = "判断是否是会员")
    @GetMapping("/isVip")
    public R<Integer> isVip(String sid, String phone) {
        return vipService.isVip(sid, phone);
    }

    @ApiOperation(value = "会员推送列表")
    @PostMapping("/getVipPushPage")
    public R<IPage<VipVO>> getVipPushPage(@RequestBody @Validated VipQueryDTO goodQueryDTO){
        return vipService.getVipPushPage(goodQueryDTO);
    }

    @ApiOperation(value = "申请店铺")
    @PostMapping("/applyShop")
    public R<String> applyShop(@RequestBody @Validated ShopApplyDTO shopApplyDTO){
        return shopApplyService.applyShop(shopApplyDTO);
    }
}
