package com.li.wisdomcashier.base.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.ShopApplyDTO;
import com.li.wisdomcashier.base.entity.po.ShopApply;
import com.li.wisdomcashier.base.mapper.ShopApplyMapper;
import com.li.wisdomcashier.base.service.ShopApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.UserUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lsw
 * @since 2023-03-23
 */
@Service
public class ShopApplyServiceImpl extends ServiceImpl<ShopApplyMapper, ShopApply> implements ShopApplyService {

    @Resource
    private ShopApplyMapper shopApplyMapper;

    @Override
    public R<String> applyShop(ShopApplyDTO shopApplyDTO) {
        Long id = UserUtils.getUser().getId();
        ShopApply shopApply = shopApplyMapper.selectOne(Wrappers.lambdaQuery(ShopApply.class)
                .eq(ShopApply::getApplyId, id)
                .eq(ShopApply::getStatus,ApplyEnum.WAIT.getCode())
        );
        if(!Objects.isNull(shopApply)){
            return R.error("存在一份待审批的申请，请耐心等待！");
        }
        ShopApply apply = CglibUtil.copy(shopApplyDTO, ShopApply.class);
        apply.setStatus(ApplyEnum.WAIT.getCode());
        apply.setApplyId(id);
        return R.ok(shopApplyMapper.insert(apply)==1?"申请成功！":"申请失败！请重新提交");
    }
}
