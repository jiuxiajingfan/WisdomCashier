package com.li.wisdomcashier.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.controller.shop.shopApply.dto.ShopApplyDTO;
import com.li.wisdomcashier.controller.shop.shopApply.vo.ShopApplyVO;
import com.li.wisdomcashier.convert.ShopApplyConvert;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.ShopApply;
import com.li.wisdomcashier.enums.shop.ApplyEnum;
import com.li.wisdomcashier.mapper.ShopApplyMapper;
import com.li.wisdomcashier.service.ShopApplyService;
import com.li.wisdomcashier.utils.UserUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Li
 * @description 针对表【t_shop_apply】的数据库操作Service实现
 * @createDate 2023-11-08 20:15:38
 */
@Service
public class ShopApplyServiceImpl extends ServiceImpl<ShopApplyMapper, ShopApply> implements ShopApplyService {

    @Resource
    private ShopApplyMapper shopApplyMapper;

    @Override
    public R<ShopApplyVO> getApply() {
        Long id = UserUtils.getUser().getId();
        ShopApplyVO shopApplyVO = new ShopApplyVO();
        shopApplyVO.setFlag(false);
        List<ShopApply> shopApplies = shopApplyMapper.selectList(Wrappers.lambdaQuery(ShopApply.class)
                .eq(ShopApply::getApplyId, id)
                .orderByDesc(ShopApply::getGmtCreate)
        );
        List<ShopApply> collect = shopApplies.stream().map(e -> {
            ShopApply apply = new ShopApply();
            if (e.getStatus().equals(ApplyEnum.WAIT.getCode())) {
                shopApplyVO.setFlag(true);
            }
            apply.setStatus(e.getStatus());
            apply.setName(e.getName());
            apply.setGmtCreate(e.getGmtCreate());
            apply.setId(e.getId());
            return apply;
        }).toList();
        if (shopApplies.isEmpty()) {
            collect = new ArrayList<>();
        }
        shopApplyVO.setList(collect);
        return R.ok(shopApplyVO);
    }

    @Override
    public R<String> applyShop(ShopApplyDTO shopApplyDTO) {
        Long id = UserUtils.getUser().getId();
        ShopApply shopApply = shopApplyMapper.selectOne(Wrappers.lambdaQuery(ShopApply.class)
                .eq(ShopApply::getApplyId, id)
                .eq(ShopApply::getStatus, ApplyEnum.WAIT.getCode())
        );
        if (!Objects.isNull(shopApply)) {
            return R.error("存在一份待审批的申请，请耐心等待！");
        }
        ShopApply apply = ShopApplyConvert.INSTANCE.toShopApply(shopApplyDTO);
        apply.setStatus(ApplyEnum.WAIT.getCode());
        apply.setApplyId(id);
        return R.ok(shopApplyMapper.insert(apply) == 1 ? "申请成功！" : "申请失败！请重新提交");
    }
}




