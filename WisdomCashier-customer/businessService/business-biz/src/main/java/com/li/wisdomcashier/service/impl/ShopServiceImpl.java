package com.li.wisdomcashier.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.controller.shop.shopApply.dto.ShopQueryDTO;
import com.li.wisdomcashier.controller.shop.shopApply.vo.ShopVO;
import com.li.wisdomcashier.convert.ShopConvert;
import com.li.wisdomcashier.entry.*;
import com.li.wisdomcashier.enums.VipEnum;
import com.li.wisdomcashier.mapper.RoleMapper;
import com.li.wisdomcashier.mapper.ShopMapper;
import com.li.wisdomcashier.mapper.SysMenuMapper;
import com.li.wisdomcashier.mapper.VipMapper;
import com.li.wisdomcashier.service.ShopService;
import com.li.wisdomcashier.utils.UserUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Li
 * @description 针对表【t_shop】的数据库操作Service实现
 * @createDate 2023-11-08 20:06:06
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private ShopMapper shopMapper;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Resource
    private VipMapper vipMapper;

    @Override
    public R<IPage<ShopVO>> getUserShopPage(ShopQueryDTO shopQueryDTO) {
        List<Role> roles = roleMapper.selectList(Wrappers.lambdaQuery(Role.class).eq(Role::getUserId, UserUtils.getUser().getId()));
        if (roles.isEmpty()) {
            return R.ok(new Page<>());
        }
        //存储用户在某一店铺最高权限
        HashMap<Long, Integer> roleMap = new HashMap<>();
        LambdaQueryWrapper<Shop> wapper = Wrappers.lambdaQuery(Shop.class)
                .in(Shop::getId, roles.stream().map(Role::getShopId).collect(Collectors.toList()))
                .like(!StringUtils.isBlank(shopQueryDTO.getName()), Shop::getShopName, shopQueryDTO.getName());
        Page<Shop> page = new Page<>(shopQueryDTO.getCurrent(), shopQueryDTO.getPageSize());
        IPage<ShopVO> result = shopMapper.selectPage(page, wapper).convert(e -> {
            ShopVO copy = ShopConvert.INSTANCE.toShopVo(e);
            copy.setRole(roleMap.get(e.getId()));
            return copy;
        });
        return R.ok(result);
    }

    @Override
    @PreAuthorize("@ss.hasPermission(#id,3,2,1)")
    public R<List<Tree<String>>> getMenu(Long id) {
        User user = UserUtils.getUser();
        Role role = roleMapper.selectOne(Wrappers.lambdaQuery(Role.class)
                .eq(Role::getShopId, id)
                .eq(Role::getUserId, user.getId())
        );
        List<SysMenu> userCenterMenu = sysMenuMapper.getShopMenu(role.getRole());
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("sort");
        treeNodeConfig.setIdKey("menuId");
        treeNodeConfig.setNameKey("name");
        treeNodeConfig.setChildrenKey("children");
        List<Tree<String>> build = TreeUtil.build(userCenterMenu, null, treeNodeConfig, ((sysMenu, tree) -> {
            tree.setId(sysMenu.getMenuId().toString());
            tree.setName(sysMenu.getName());
            tree.setParentId(null == sysMenu.getParentId() ? null : sysMenu.getParentId().toString());
            tree.setWeight(sysMenu.getSort());
            tree.putExtra("path", sysMenu.getPath());
            tree.putExtra("component", sysMenu.getComponent());
            tree.putExtra("icon", sysMenu.getIcon());
            tree.putExtra("hidden", sysMenu.getHidden());
            tree.putExtra("status", sysMenu.getStatus());
        }));
        return R.ok(build);
    }

    @Override
    @PreAuthorize("@ss.hasPermission(#sid,3,2,1)")
    public R<List<Integer>> getTradeStatus(String sid) {
        ArrayList<Integer> integers = new ArrayList<>();
        Shop shop = shopMapper.selectOne(Wrappers.lambdaQuery(Shop.class)
                .eq(Shop::getId, sid));
        integers.add(shop.getWxStatus());
        integers.add(shop.getZfbStatus());
        return R.ok(integers);
    }

    @Override
    @PreAuthorize("@ss.hasPermission(#sid,3,2,1)")
    public R<Long> isVip(String sid, String phone) {
        return R.ok(
                vipMapper.selectCount(Wrappers.lambdaQuery(Vip.class)
                .eq(Vip::getPhone, phone)
                .eq(Vip::getShopId, Long.parseLong(sid))
                .eq(Vip::getStatus, VipEnum.ACTIVE.getCode())
                )
        );
    }

}




