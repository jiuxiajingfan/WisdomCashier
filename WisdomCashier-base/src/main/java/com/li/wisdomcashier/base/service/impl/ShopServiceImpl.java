package com.li.wisdomcashier.base.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.po.Role;
import com.li.wisdomcashier.base.entity.po.Shop;
import com.li.wisdomcashier.base.mapper.RoleMapper;
import com.li.wisdomcashier.base.mapper.ShopMapper;
import com.li.wisdomcashier.base.service.ShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.entity.vo.ShopVo;
import com.li.wisdomcashier.base.util.UserUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Override
    public R<List<ShopVo>> getUserShop(String shopName) {
        List<Role> roles = roleMapper.selectList(Wrappers.lambdaQuery(Role.class).eq(Role::getUserId, UserUtils.getUser().getId()));
        if(roles.isEmpty()){
            return R.ok(new ArrayList<>());
        }
        List<Long> shopList = roles.stream().map(e -> e.getShopId()).collect(Collectors.toList());
        List<Shop> shops = shopMapper.selectList(Wrappers.lambdaQuery(Shop.class)
                .in(Shop::getId, shopList)
                .like(!StrUtil.isBlank(shopName),Shop::getShopName,shopName)
        );
        List<ShopVo> shopVos = shops.stream().map(e -> CglibUtil.copy(e, ShopVo.class)).collect(Collectors.toList());
        return R.ok(shopVos);
    }


}
