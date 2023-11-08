package com.li.WisdomCashier.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.WisdomCashier.controller.shop.shopApply.vo.ShopApplyVO;
import com.li.WisdomCashier.entry.ShopApply;
import com.li.WisdomCashier.enums.shop.ApplyEnum;
import com.li.WisdomCashier.mapper.ShopApplyMapper;
import com.li.WisdomCashier.pojo.R;
import com.li.WisdomCashier.service.ShopApplyService;
import com.li.WisdomCashier.utils.UserUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        }).collect(Collectors.toList());
        if (shopApplies.isEmpty()){
            collect = new ArrayList<>();
        }
        shopApplyVO.setList(collect);
        return R.ok(shopApplyVO);
    }

}




