package com.li.wisdomcashier.base.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.ApprovalDTO;
import com.li.wisdomcashier.base.entity.po.Apply;
import com.li.wisdomcashier.base.entity.po.Role;
import com.li.wisdomcashier.base.entity.po.Shop;
import com.li.wisdomcashier.base.entity.po.User;
import com.li.wisdomcashier.base.entity.vo.ApplyVO;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.mapper.ApplyMapper;
import com.li.wisdomcashier.base.mapper.RoleMapper;
import com.li.wisdomcashier.base.mapper.ShopMapper;
import com.li.wisdomcashier.base.mapper.UserMapper;
import com.li.wisdomcashier.base.service.ApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.UserUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lsw
 * @since 2023-03-20
 */
@Service
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, Apply> implements ApplyService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ApplyMapper applyMapper;


    @Override
    public R<List<ApplyVO>> getApplyList(String sid) {
        if(StringUtils.isBlank(sid)){
            return R.error("参数错误！");
        }
        //店主接口
        UserUtils.hasPermissions(sid, RoleEnum.SHOPMASTER.getCode());
        List<Apply> applies = applyMapper.selectList(Wrappers.lambdaQuery(Apply.class)
                .eq(Apply::getShopId, Long.parseLong(sid))
                .eq(Apply::getStatus,ApplyEnum.WAIT.getCode()));
        if(applies.isEmpty()){
            return R.ok(new ArrayList<>());
        }
        List<Long> userID = applies.stream().map(Apply::getUserId).collect(Collectors.toList());
        Map<Long, User> userMap = userMapper.selectList(Wrappers.lambdaQuery(User.class)
                .in(User::getId, userID))
                .stream().collect(Collectors.toMap(User::getId, Function.identity()));
        List<ApplyVO> result = applies.stream().map(e -> {
            ApplyVO copy = CglibUtil.copy(e, ApplyVO.class);
            copy.setName(userMap.get(e.getUserId()).getUserNickname());
            copy.setPhone(userMap.get(e.getUserId()).getPhone());
            return copy;
        }).collect(Collectors.toList());
        return R.ok(result);
    }

    @Override
    public R<String> approval(ApprovalDTO approvalDTO) {
        //店主接口
        UserUtils.hasPermissions(approvalDTO.getSid(), RoleEnum.SHOPMASTER.getCode());
        Apply apply = applyMapper.selectOne(Wrappers.lambdaQuery(Apply.class)
                .eq(Apply::getUserId, Long.parseLong(approvalDTO.getPid()))
                .eq(Apply::getShopId, Long.parseLong(approvalDTO.getSid())));
        if(Objects.isNull(apply)) {
            return R.error("不存在该申请！");
        }
        apply.setStatus(approvalDTO.getType());
        applyMapper.updateById(apply);
        if(apply.getStatus() == ApplyEnum.SUCCESS.getCode())
        {
            Role role = new Role();
            role.setUserId(Long.parseLong(approvalDTO.getPid()));
            role.setShopId(Long.parseLong(approvalDTO.getSid()));
            role.setRole(RoleEnum.SHOP.getCode());
            roleMapper.insert(role);
        }
        return R.ok("执行成功！");
    }
}
