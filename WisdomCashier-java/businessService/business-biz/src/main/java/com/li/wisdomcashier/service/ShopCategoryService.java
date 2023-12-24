package com.li.wisdomcashier.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.entry.ShopCategory;
import com.li.wisdomcashier.pojo.R;

import java.util.List;

/**
* @author Li
* @description 针对表【t_shop_category】的数据库操作Service
* @createDate 2023-11-08 20:15:43
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
