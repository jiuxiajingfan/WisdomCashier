package com.li.wisdomcashier.base.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.AddVipDTO;
import com.li.wisdomcashier.base.entity.dto.GoodQueryDTO;
import com.li.wisdomcashier.base.entity.dto.RenewalDTO;
import com.li.wisdomcashier.base.entity.dto.VipQueryDTO;
import com.li.wisdomcashier.base.entity.po.Vip;
import com.li.wisdomcashier.base.entity.vo.VipVO;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.enums.VipEnum;
import com.li.wisdomcashier.base.mapper.VipMapper;
import com.li.wisdomcashier.base.service.VipService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.UserUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lsw
 * @since 2023-03-21
 */
@Service
public class VipServiceImpl extends ServiceImpl<VipMapper, Vip> implements VipService {

    @Resource
    private VipMapper vipMapper;

    @Override
    public R<IPage<VipVO>> getVipPage(GoodQueryDTO goodQueryDTO) {
        //普通接口
        UserUtils.hasPermissions(goodQueryDTO.getSid(), RoleEnum.SHOP.getCode());
        LambdaQueryWrapper<Vip> wrapper = Wrappers.lambdaQuery(Vip.class)
                .eq(Vip::getShopId, Long.parseLong(goodQueryDTO.getSid()))
                .eq(!StringUtils.isBlank(goodQueryDTO.getGid()), Vip::getPhone, goodQueryDTO.getGid());
        IPage<Vip> Page =new Page(goodQueryDTO.getCurrent(),goodQueryDTO.getPageSize());
        IPage<VipVO> convert = vipMapper.selectPage(Page, wrapper).convert(e -> {
            VipVO copy = new VipVO();
            copy.setStatus(e.getStatus());
            copy.setIntegration(e.getIntegration());
            copy.setId(e.getPhone());
            copy.setLimit(e.getGmtLimit());
            copy.setLevel(e.getLevel());
            return copy;
        });
        return R.ok(convert);
    }

    @Override
    public R<String> addVip(AddVipDTO addVipDTO) {
        //普通接口
        UserUtils.hasPermissions(addVipDTO.getSid(), RoleEnum.SHOP.getCode());
        Vip vip = vipMapper.selectOne(Wrappers.lambdaQuery(Vip.class)
                .eq(Vip::getPhone, addVipDTO.getPhone())
                .eq(Vip::getShopId, Long.parseLong(addVipDTO.getSid())));
        if(!Objects.isNull(vip)){
            return R.error("该会员已经存在！");
        }
        Vip newVip = new Vip();
        newVip.setAge(addVipDTO.getAge());
        newVip.setSex(addVipDTO.getSex());
        newVip.setGmtLimit(LocalDateTime.now().plusMonths(addVipDTO.getLimit()));
        newVip.setPhone(addVipDTO.getPhone());
        newVip.setStatus(VipEnum.ACTIVE.getCode());
        newVip.setShopId(Long.parseLong(addVipDTO.getSid()));
        return R.ok(vipMapper.insert(newVip) == 1 ? "新增成功！": "新增失败，请联系管理员！");
    }

    @Override
    public R<String> renewalVip(RenewalDTO renewalDTO) {
        //普通接口
        UserUtils.hasPermissions(renewalDTO.getSid(), RoleEnum.SHOP.getCode());
        Vip vip = vipMapper.selectOne(Wrappers.lambdaQuery(Vip.class)
                .eq(Vip::getPhone, renewalDTO.getPhone())
                .eq(Vip::getShopId, Long.parseLong(renewalDTO.getSid())));
        if(Objects.isNull(vip)){
            return R.error("不存在该会员！");
        }
        if(vip.getStatus().equals(VipEnum.LIMITED.getCode())) {
            vip.setGmtLimit(LocalDateTime.now().plusMonths(renewalDTO.getLimit()));
            vip.setStatus(VipEnum.ACTIVE.getCode());
        }else {
            vip.setGmtLimit(vip.getGmtLimit().plusMonths(renewalDTO.getLimit()));
        }
        return R.ok(vipMapper.updateById(vip)== 1 ? "更新成功！": "更新失败，请联系管理员！");
    }

    @Override
    public R<Integer> isVip(String sid, String phone) {
        if(StringUtils.isBlank(sid)||StringUtils.isBlank(phone)) {
            return R.error("参数错误！");
        }
        //普通接口
        UserUtils.hasPermissions(sid, RoleEnum.SHOP.getCode());
        return R.ok(vipMapper.selectCount(Wrappers.lambdaQuery(Vip.class)
                .eq(Vip::getPhone, phone)
                .eq(Vip::getShopId, Long.parseLong(sid))
                .eq(Vip::getStatus, VipEnum.ACTIVE.getCode())));
    }

    @Async
    @Override
    public void addIntegration(String phone, String sum,String sid) {
        BigDecimal bigDecimal = new BigDecimal(sum);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100));
        Vip vip = vipMapper.selectOne(Wrappers.lambdaQuery(Vip.class)
                .eq(Vip::getPhone,phone)
                .eq(Vip::getShopId, Long.parseLong(sid)));
        vip.setIntegration(vip.getIntegration() + multiply.intValue());
        vip.setLevel(vip.getIntegration()/100000);
        vipMapper.updateById(vip);
    }

    @Override
    public R<IPage<VipVO>> getVipPushPage(VipQueryDTO goodQueryDTO) {
        //普通接口
        UserUtils.hasPermissions(goodQueryDTO.getSid(), RoleEnum.SHOP.getCode());
        LambdaQueryWrapper<Vip> wrapper = Wrappers.lambdaQuery(Vip.class)
                .eq(Vip::getShopId, Long.parseLong(goodQueryDTO.getSid()))
                .in(!CollectionUtils.isEmpty(goodQueryDTO.getAge()), Vip::getAge, goodQueryDTO.getAge())
                .in(!CollectionUtils.isEmpty(goodQueryDTO.getSex()), Vip::getSex, goodQueryDTO.getSex())
                .in(!CollectionUtils.isEmpty(goodQueryDTO.getLevel()), Vip::getLevel, goodQueryDTO.getLevel())
                .in(!CollectionUtils.isEmpty(goodQueryDTO.getStatus()), Vip::getStatus, goodQueryDTO.getStatus());
        IPage<Vip> Page =new Page(goodQueryDTO.getCurrent(),goodQueryDTO.getPageSize());
        IPage<VipVO> convert = vipMapper.selectPage(Page, wrapper).convert(e -> {
            VipVO copy = CglibUtil.copy(e, VipVO.class);
            copy.setId(e.getPhone());
            return copy;
        });
        return R.ok(convert);
    }
}
