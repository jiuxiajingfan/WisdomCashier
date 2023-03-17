package com.li.wisdomcashier.base.service;

import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.po.ShopCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lsw
 * @since 2023-03-17
 */
public interface ShopCategoryService extends IService<ShopCategory> {

    /**
     * 获取店铺分类
     * @param sid 店铺id
     * @return
     */
    R<List<String>> getCategory(String sid);

    /**
     * 新增标签
     * @param sid
     * @param category
     * @return
     */
    R<String> addCategory(String sid, String category);

    /**
     * 删除标签
     * @param sid
     * @param category
     * @return
     */
    R<String> delCategory(String sid, String category);
}
