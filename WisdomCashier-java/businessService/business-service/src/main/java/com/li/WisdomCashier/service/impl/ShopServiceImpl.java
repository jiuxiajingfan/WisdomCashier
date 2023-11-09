package com.li.WisdomCashier.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.WisdomCashier.controller.shop.shopApply.dto.ShopQueryDTO;
import com.li.WisdomCashier.controller.shop.shopApply.vo.ShopVO;
import com.li.WisdomCashier.entry.Shop;
import com.li.WisdomCashier.mapper.RoleMapper;
import com.li.WisdomCashier.mapper.ShopMapper;
import com.li.WisdomCashier.mapper.SysMenuMapper;
import com.li.WisdomCashier.po.Role;
import com.li.WisdomCashier.po.SysMenu;
import com.li.WisdomCashier.po.User;
import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.service.ShopService;
import com.li.WisdomCashier.utils.UserUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
            ShopVO copy = CglibUtil.copy(e, ShopVO.class);
            copy.setRole(roleMap.get(e.getId()));
            return copy;
        });
        return R.ok(result);
    }

    @Override
    @PreAuthorize("hasPermission(#id,1)||hasPermission(#id,2)||hasPermission(#id,3)")
    public R<List<Tree<String>>> getMenu(long id) {
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
            tree.setParentId(null == sysMenu.getParentId()?null:sysMenu.getParentId().toString());
            tree.setWeight(sysMenu.getSort());
            tree.putExtra("path", sysMenu.getPath());
            tree.putExtra("component", sysMenu.getComponent());
            tree.putExtra("icon", sysMenu.getIcon());
            tree.putExtra("hidden", sysMenu.getHidden());
            tree.putExtra("status", sysMenu.getStatus());
        }));
        return R.ok(build);
    }
}




