package com.li.wisdomcashier.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.entry.ShopCategory;
import com.li.wisdomcashier.mapper.ShopCategoryMapper;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.service.ShopCategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Li
 * @description 针对表【t_shop_category】的数据库操作Service实现
 * @createDate 2023-11-08 20:15:43
 */
@Service
public class ShopCategoryServiceImpl extends ServiceImpl<ShopCategoryMapper, ShopCategory> implements ShopCategoryService {

    @Resource
    private ShopCategoryMapper shopCategoryMapper;

    @Override
    @PreAuthorize("@ss.hasPermission(#sid,3,2,1)")
    public R<List<String>> getCategory(String sid) {
        if (StringUtils.isBlank(sid))
            return R.error("店铺ID不能为空！");
        List<ShopCategory> shopCategories = shopCategoryMapper.selectList(Wrappers.lambdaQuery(ShopCategory.class).eq(ShopCategory::getShopId, Long.parseLong(sid)));
        List<String> collect = shopCategories.stream().map(ShopCategory::getCategory).collect(Collectors.toList());
        return R.ok(collect);
    }

    @Override
    @PreAuthorize("@ss.hasPermission(#sid,1)||@ss.hasPermission(#sid,2)")
    public R<String> addCategory(String sid, String category) {
        if (StringUtils.isBlank(sid))
            return R.error("店铺ID不能为空！");
        if (StringUtils.isBlank(category))
            return R.error("分类不能为空！");
        //店铺管理员权限接口
        ShopCategory shopCategory = shopCategoryMapper.selectOne(Wrappers.lambdaQuery(ShopCategory.class).eq(ShopCategory::getShopId, Long.parseLong(sid)).eq(ShopCategory::getCategory, category));
        if (!Objects.isNull(shopCategory)) {
            return R.error("该分类已经存在，请勿重复添加！");
        }
        ShopCategory shopCategory1 = new ShopCategory();
        shopCategory1.setCategory(category);
        shopCategory1.setShopId(Long.parseLong(sid));
        return R.ok(shopCategoryMapper.insert(shopCategory1) == 1 ? "新增成功!" : "新增失败，请联系系统管理员！");
    }

    @Override
    @PreAuthorize("@ss.hasPermission(#sid,1)||@ss.hasPermission(#sid,2)")
    public R<String> delCategory(String sid, String category) {
        if (StringUtils.isBlank(sid))
            return R.error("店铺ID不能为空！");
        if (StringUtils.isBlank(category))
            return R.error("分类不能为空！");
        ShopCategory shopCategory = shopCategoryMapper.selectOne(Wrappers.lambdaQuery(ShopCategory.class).eq(ShopCategory::getShopId, Long.parseLong(sid)).eq(ShopCategory::getCategory, category));
        if (Objects.isNull(shopCategory)) {
            return R.error("不存在该分类！");
        }
        return R.ok(shopCategoryMapper.deleteById(shopCategory) == 1 ? "删除成功!" : "删除失败，请联系系统管理员！");
    }
}




