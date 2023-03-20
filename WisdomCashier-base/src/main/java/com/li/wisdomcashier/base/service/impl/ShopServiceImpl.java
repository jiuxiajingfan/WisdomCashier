package com.li.wisdomcashier.base.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.QueryEmDTO;
import com.li.wisdomcashier.base.entity.dto.ShopMessageDTO;
import com.li.wisdomcashier.base.entity.dto.ShopQueryDTO;
import com.li.wisdomcashier.base.entity.po.*;
import com.li.wisdomcashier.base.entity.vo.UserVo;
import com.li.wisdomcashier.base.enums.MenuEnum;
import com.li.wisdomcashier.base.enums.ResultStatus;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.mapper.*;
import com.li.wisdomcashier.base.service.ShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.entity.vo.ShopVO;
import com.li.wisdomcashier.base.util.UserUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
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

    @Resource
    private UserMapper userMapper;

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
        if(shopId == null)
            return R.error("参数错误！");
        UserUtils.hasPermissions(shopId.toString(), RoleEnum.SHOP.getCode());
        Subject subject = SecurityUtils.getSubject();
        List<SysMenu> userCenterMenu = new ArrayList<>();
        Integer role;
        if (subject.isPermitted(shopId.toString()+"2")) {
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

    @Override
    public R<ShopVO> getShopMessageByID(String sid) {
        //店主接口
        Shop shop = UserUtils.hasPermissions(sid, RoleEnum.SHOPMASTER.getCode());
        ShopVO shopVO = new ShopVO();
        shopVO.setShopName(shop.getShopName());
        shopVO.setDesc(shop.getTip());
        shopVO.setCreateTime(shop.getGmtCreate());
        shopVO.setId(shop.getId());
        shopVO.setWx(shop.getWxStatus());
        shopVO.setZfb(shop.getZfbStatus());
        return R.ok(shopVO);
    }

    @Override
    public R<String> updateShopMessage(ShopMessageDTO shopMessageDTO) {
        //店主接口
        Shop shop = UserUtils.hasPermissions(shopMessageDTO.getSid(), RoleEnum.SHOPMASTER.getCode());
        shop.setShopName(shopMessageDTO.getName());
        shop.setTip(shopMessageDTO.getDesc());
        return R.ok(shopMapper.updateById(shop)==1?"更新成功！":"更新失败，请联系系统管理员！");
    }

    @Override
    public R<IPage<UserVo>> getEmploree(QueryEmDTO queryEmDTO) {
        //店主接口
        UserUtils.hasPermissions(queryEmDTO.getSid(), RoleEnum.SHOPMASTER.getCode());
        Integer total = roleMapper.selectPersonCount(queryEmDTO.getSid());
        if(total == 0)
        {
            return R.ok(new Page<>());
        }
        List<UserVo> userVos = roleMapper.selectPerson(Long.parseLong(queryEmDTO.getSid()), (queryEmDTO.getCurrent()-1) * queryEmDTO.getPageSize(), queryEmDTO.getPageSize());
        List<Long> userID = userVos.stream().map(UserVo::getId).collect(Collectors.toList());
        Map<Long, User> userMap = userMapper.selectList(Wrappers.lambdaQuery(User.class).in(User::getId, userID))
                .stream().collect(Collectors.toMap(User::getId, Function.identity()));
        List<UserVo> collect = userVos.stream().map(e -> {
            Integer tmp = e.getRoleEnum();
            UserVo copy = CglibUtil.copy(userMap.get(e.getId()), UserVo.class);
            copy.setRoleEnum(tmp);
            return copy;
        }).collect(Collectors.toList());
        Page<UserVo> userVoPage = new Page<>();
        userVoPage.setTotal(total);
        userVoPage.setCurrent(queryEmDTO.getCurrent());
        userVoPage.setRecords(collect);
        userVoPage.setSize(queryEmDTO.getPageSize());
        return R.ok(userVoPage);
    }

    @Override
    public R<String> addEmploree(String sid, String pid) {
        if(StringUtils.isBlank(sid)||StringUtils.isBlank(pid))
        {
            return R.error("参数为空！");
        }
        //店主接口
        UserUtils.hasPermissions(sid, RoleEnum.SHOPMASTER.getCode());
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getId, Long.parseLong(pid)));
        if(Objects.isNull(user)){
            return R.error("查无此人！请检查用户ID是否错误！");
        }
        List<Role> roles = roleMapper.selectList(Wrappers.lambdaQuery(Role.class).eq(Role::getUserId, Long.parseLong(pid)).eq(Role::getShopId,Long.parseLong(sid)));
        if(!roles.isEmpty()){
            return R.ok("新增成功！");
        }
        Role role = new Role();
        role.setShopId(Long.parseLong(sid));
        role.setRole(RoleEnum.SHOP.getCode());
        role.setUserId(user.getId());
        return R.ok(roleMapper.insert(role)==1?"新增成功":"新增失败！请联系系统管理员！") ;
    }


}
