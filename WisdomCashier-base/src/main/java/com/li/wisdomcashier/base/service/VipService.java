package com.li.wisdomcashier.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.AddVipDTO;
import com.li.wisdomcashier.base.entity.dto.GoodQueryDTO;
import com.li.wisdomcashier.base.entity.dto.RenewalDTO;
import com.li.wisdomcashier.base.entity.dto.VipQueryDTO;
import com.li.wisdomcashier.base.entity.po.Vip;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.wisdomcashier.base.entity.vo.VipVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lsw
 * @since 2023-03-21
 */
public interface VipService extends IService<Vip> {

    R<IPage<VipVO>> getVipPage(GoodQueryDTO goodQueryDTO);

    R<String> addVip(AddVipDTO addVipDTO);

    R<String> renewalVip(RenewalDTO renewalDTO);

    R<Integer> isVip(String sid,String phone);

    /**
     * 会员积分
     * @param phone
     * @param sum
     * @param sid
     */
    void addIntegration(String phone,String sum,String sid);

    R<IPage<VipVO>> getVipPushPage(VipQueryDTO goodQueryDTO);
}
