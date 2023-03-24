package com.li.wisdomcashier.base.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.ShopApplyDTO;
import com.li.wisdomcashier.base.entity.po.ShopApply;
import com.li.wisdomcashier.base.entity.po.User;
import com.li.wisdomcashier.base.entity.vo.ShopApplyVO;
import com.li.wisdomcashier.base.mapper.ShopApplyMapper;
import com.li.wisdomcashier.base.service.ShopApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.UserUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
            if (e.getStatus() == ApplyEnum.WAIT.getCode())
                shopApplyVO.setFlag(true);
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

    @Override
    public R<String> cancelApply(String id) {
        User user = UserUtils.getUser();
        ShopApply apply = shopApplyMapper.selectOne(Wrappers.lambdaQuery(ShopApply.class).eq(ShopApply::getId, Long.parseLong(id)));
        if(Objects.isNull(apply))
            return R.error("无此申请");
        if(apply.getApplyId().compareTo(user.getId()) != 0){
            return R.error("无权限撤销!");
        }
        apply.setStatus(ApplyEnum.CHANEL.getCode());
       return R.ok(shopApplyMapper.updateById(apply) ==1?"撤销成功":"撤销失败，请重试");
    }

}
