package com.li.wisdomcashier.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.QueryEmDTO;
import com.li.wisdomcashier.base.entity.dto.ShopMessageDTO;
import com.li.wisdomcashier.base.entity.dto.ShopQueryDTO;
import com.li.wisdomcashier.base.entity.po.Shop;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.base.entity.po.SysMenu;
import com.li.wisdomcashier.base.entity.vo.ShopVO;
import com.li.wisdomcashier.base.entity.vo.UserVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Nine
 * @since 2022-10-11
 */
public interface ShopService extends IService<Shop> {
    /**
     * 获取用户相关店铺
     * @return
     */
   R<List<ShopVO>> getUserShop(String shopName);

   R<IPage<ShopVO>> getUserShopPage(ShopQueryDTO shopQueryDTO);

    /**
     * 获取店铺菜单
     * @param shopId
     * @return
     */
    R<List<SysMenu>> getMenu(Long shopId);

    R<ShopVO> getShopMessageByID(String sid);

    /**
     * 更新
     * @param shopMessageDTO
     * @return
     */
    R<String> updateShopMessage(ShopMessageDTO shopMessageDTO);

    /**
     * 员工列表
     * @param queryEmDTO
     * @return
     */
    R<IPage<UserVo>> getEmploree(QueryEmDTO queryEmDTO);

    /**
     * 新增员工
     * @param sid 店铺id
     * @param pid 员工id
     * @return
     */
    R<String> addEmploree(String sid,String pid);



}
