package com.li.wisdomcashier.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.po.ShopCategory;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.mapper.ShopCategoryMapper;
import com.li.wisdomcashier.base.service.ShopCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lsw
 * @since 2023-03-17
 */
@Service
public class ShopCategoryServiceImpl extends ServiceImpl<ShopCategoryMapper, ShopCategory> implements ShopCategoryService {

    @Resource
    private ShopCategoryMapper shopCategoryMapper;

    @Override
    public R<List<String>> getCategory(String sid) {
        if(StringUtils.isBlank(sid))
            return R.error("店铺ID不能为空！");
        //店铺管理员权限接口
        UserUtils.hasPermissions(sid, RoleEnum.SHOPADMIN.getCode());
        List<ShopCategory> shopCategories = shopCategoryMapper.selectList(Wrappers.lambdaQuery(ShopCategory.class).eq(ShopCategory::getShopId, Long.parseLong(sid)));
        List<String> collect = shopCategories.stream().map(ShopCategory::getCategory).collect(Collectors.toList());
        return R.ok(collect);
    }

    @Override
    public R<String> addCategory(String sid, String category) {
        if(StringUtils.isBlank(sid))
            return R.error("店铺ID不能为空！");
        if(StringUtils.isBlank(category))
            return R.error("分类不能为空！");
        //店铺管理员权限接口
        UserUtils.hasPermissions(sid, RoleEnum.SHOPADMIN.getCode());
        ShopCategory shopCategory = shopCategoryMapper.selectOne(Wrappers.lambdaQuery(ShopCategory.class).eq(ShopCategory::getShopId, Long.parseLong(sid)).eq(ShopCategory::getCategory, category));
        if(!Objects.isNull(shopCategory)){
            return R.error("该分类已经存在，请勿重复添加！");
        }
        ShopCategory shopCategory1 = new ShopCategory();
        shopCategory1.setCategory(category);
        shopCategory1 .setShopId(Long.parseLong(sid));
       return R.ok(shopCategoryMapper.insert(shopCategory1) == 1 ? "新增成功!":"新增失败，请联系系统管理员！");
    }

    @Override
    public R<String> delCategory(String sid, String category) {
        if(StringUtils.isBlank(sid))
            return R.error("店铺ID不能为空！");
        if(StringUtils.isBlank(category))
            return R.error("分类不能为空！");
        //店铺管理员权限接口
        UserUtils.hasPermissions(sid, RoleEnum.SHOPADMIN.getCode());
        ShopCategory shopCategory = shopCategoryMapper.selectOne(Wrappers.lambdaQuery(ShopCategory.class).eq(ShopCategory::getShopId, Long.parseLong(sid)).eq(ShopCategory::getCategory, category));
        if(Objects.isNull(shopCategory)){
            return R.error("不存在该分类！");
        }
        return R.ok(shopCategoryMapper.deleteById(shopCategory) == 1 ? "删除成功!":"删除失败，请联系系统管理员！");
    }
}
