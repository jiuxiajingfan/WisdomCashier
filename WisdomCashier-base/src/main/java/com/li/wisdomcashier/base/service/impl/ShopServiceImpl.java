package com.li.wisdomcashier.base.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.ShopQueryDTO;
import com.li.wisdomcashier.base.entity.po.Role;
import com.li.wisdomcashier.base.entity.po.Shop;
import com.li.wisdomcashier.base.entity.po.SysMenu;
import com.li.wisdomcashier.base.enums.MenuEnum;
import com.li.wisdomcashier.base.enums.ResultStatus;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.mapper.RoleMapper;
import com.li.wisdomcashier.base.mapper.ShopMapper;
import com.li.wisdomcashier.base.mapper.SysMenuMapper;
import com.li.wisdomcashier.base.service.ShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.entity.vo.ShopVO;
import com.li.wisdomcashier.base.util.UserUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Nine
 * @since 2022-10-11
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {

    @Resource
    private ShopMapper shopMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public R<List<ShopVO>> getUserShop(String shopName) {
        List<Role> roles = roleMapper.selectList(Wrappers.lambdaQuery(Role.class).eq(Role::getUserId, UserUtils.getUser().getId()));
        if(roles.isEmpty()){
            return R.ok(new ArrayList<>());
        }
        List<Shop> shops = shopMapper.selectList(Wrappers.lambdaQuery(Shop.class)
                .in(Shop::getId, roles.stream().map(e -> e.getShopId()).collect(Collectors.toList()))
                .like(!StrUtil.isBlank(shopName),Shop::getShopName,shopName)
        );
        List<ShopVO> shopVOS = shops.stream().map(e -> CglibUtil.copy(e, ShopVO.class)).collect(Collectors.toList());
        return R.ok(shopVOS);
    }

    @Override
    public R<IPage<ShopVO>> getUserShopPage(ShopQueryDTO shopQueryDTO) {
        List<Role> roles = roleMapper.selectList(Wrappers.lambdaQuery(Role.class).eq(Role::getUserId, UserUtils.getUser().getId()));
        if(roles.isEmpty()){
            return R.ok(new Page<>());
        }
        //存储用户在某一店铺最高权限
        HashMap<Long, Integer> roleMap = new HashMap<>();
        List<Long> shopList = roles.stream().map(e -> {
            if(roleMap.getOrDefault(e.getShopId(),RoleEnum.GUEST.getCode())>e.getRole()){
                roleMap.put(e.getShopId(),e.getRole());
            }
           return e.getShopId();
        }).collect(Collectors.toList());
        LambdaQueryWrapper<Shop> wapper = Wrappers.lambdaQuery(Shop.class)
                .in(Shop::getId, roles.stream().map(e -> e.getShopId()).collect(Collectors.toList()))
                .like(!StrUtil.isBlank(shopQueryDTO.getName()), Shop::getShopName, shopQueryDTO.getName());
        Page<Shop> page = new Page<>(shopQueryDTO.getCurrent(), shopQueryDTO.getPageSize());
        IPage<ShopVO> result = shopMapper.selectPage(page, wapper).convert(e -> {
            ShopVO copy = CglibUtil.copy(e, ShopVO.class);
            copy.setRole(roleMap.get(e.getId()));
            return copy;
        });
        return R.ok(result);
    }

    @Override
    public R<List<SysMenu>> getMenu(Long shopId) {
        if(!UserUtils.hasPermissions(shopId,RoleEnum.SHOP.getCode())){
            return R.error(ResultStatus.ACCESS_DENIED);
        }
        Subject subject = SecurityUtils.getSubject();
        List<SysMenu> userCenterMenu = new ArrayList<>();
        Integer role;
        if (subject.hasRole("shopadmin")) {
            role = RoleEnum.SHOPADMIN.getCode();
        } else {
            role = RoleEnum.SHOP.getCode();
        }
        userCenterMenu = sysMenuMapper.getUserCenterMenu(role, MenuEnum.SHOPMENU.getCode());
        for (SysMenu centerMenu : userCenterMenu) {
            centerMenu.setChildren(sysMenuMapper.getChildrens(role, centerMenu.getMenuId(),MenuEnum.SHOPMENU.getCode()));
        }
        return R.ok(userCenterMenu);
    }


}
