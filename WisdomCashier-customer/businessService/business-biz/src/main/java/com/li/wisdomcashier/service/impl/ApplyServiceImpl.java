package com.li.wisdomcashier.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.controller.shop.shopApply.dto.ApprovalDTO;
import com.li.wisdomcashier.controller.shop.shopApply.vo.ApplyVO;
import com.li.wisdomcashier.entry.Apply;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.Role;
import com.li.wisdomcashier.entry.User;
import com.li.wisdomcashier.enums.shop.ApplyEnum;
import com.li.wisdomcashier.enums.shop.RoleEnum;
import com.li.wisdomcashier.mapper.ApplyMapper;
import com.li.wisdomcashier.mapper.RoleMapper;
import com.li.wisdomcashier.mapper.UserMapper;
import com.li.wisdomcashier.service.ApplyService;
import com.li.wisdomcashier.utils.UserUtils;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private UserMapper userMapper;

    @Resource
    private ApplyMapper applyMapper;

    @Resource
    private RoleMapper roleMapper;


    @PreAuthorize("@ss.hasPermission(#sid,3)")
    @Override
    public R<List<ApplyVO>> getApplyList(String sid) {
        if(StringUtils.isBlank(sid)){
            return R.error("参数错误！");
        }
        //店主接口
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
    @PreAuthorize("@ss.hasPermission(#approvalDTO.sid,3)")
    public R<String> approval(ApprovalDTO approvalDTO) {
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

    @Override
    public R<String> applyShop(String sid) {
        if(StringUtils.isBlank(sid))
            return R.error("参数错误！");
        User user = UserUtils.getUser();
        List<Apply> applies = applyMapper.selectList(Wrappers.lambdaQuery(Apply.class)
                .eq(Apply::getShopId, Long.parseLong(sid))
                .eq(Apply::getUserId, user.getId())
                .eq(Apply::getStatus,ApplyEnum.WAIT.getCode()));
        if(!applies.isEmpty()){
            return R.error("请等待上份申请完成！");
        }
        Apply apply = new Apply();
        apply.setStatus(ApplyEnum.WAIT.getCode());
        apply.setUserId(user.getId());
        apply.setShopId(Long.parseLong(sid));
        apply.setGmtCreate(LocalDateTime.now());
        return R.ok(applyMapper.insert(apply)==1?"申请成功":"申请失败！");
    }

    @Override
    public R<List<ApplyVO>> getApplyListPer() {
        User user = UserUtils.getUser();
        List<Apply> applies = applyMapper.selectList(Wrappers.lambdaQuery(Apply.class)
                .eq(Apply::getUserId, user.getId())
                .orderByDesc(Apply::getGmtCreate)
        );
        if(applies.isEmpty()){
            return R.ok(new ArrayList<>());
        }
        List<ApplyVO> result = applies.stream().map(e -> {
            ApplyVO applyVO = new ApplyVO();
            applyVO.setName(e.getShopId().toString());
            applyVO.setGmtCreate(e.getGmtCreate());
            applyVO.setUserId(Long.parseLong(e.getStatus().toString()));
            return applyVO;
        }).collect(Collectors.toList());
        return R.ok(result);
    }
}
