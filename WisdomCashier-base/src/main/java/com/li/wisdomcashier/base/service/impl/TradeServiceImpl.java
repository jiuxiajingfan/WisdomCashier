package com.li.wisdomcashier.base.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.TradeDTO;
import com.li.wisdomcashier.base.entity.po.Trade;
import com.li.wisdomcashier.base.entity.po.TradeGoods;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.mapper.TradeGoodsMapper;
import com.li.wisdomcashier.base.mapper.TradeMapper;
import com.li.wisdomcashier.base.service.TradeGoodsService;
import com.li.wisdomcashier.base.service.TradeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.UserUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lsw
 * @since 2023-03-03
 */
@Service
public class TradeServiceImpl extends ServiceImpl<TradeMapper, Trade> implements TradeService {

    @Resource
    private TradeMapper tradeMapper;

    @Resource
    private TradeGoodsMapper tradeGoodsMapper;

    @Override
    public R<List<TradeDTO>> queryLeast(Long sid) {
        if(!UserUtils.hasPermissions(sid, RoleEnum.SHOP.getCode())){
            throw new AuthorizationException("无权操作！");
        }
        List<Trade> trades = tradeMapper.selectLesat(sid,UserUtils.getUser().getId());
        List<TradeDTO> collect = trades.stream().map(e ->
                        CglibUtil.copy(e, TradeDTO.class)
        ).collect(Collectors.toList());
        return R.ok(collect);
    }

    public R<List<TradeGoods>> queryGoodsById(Long id){
        List<TradeGoods> tradeGoods = tradeGoodsMapper.selectList(Wrappers.lambdaQuery(TradeGoods.class)
                .eq(TradeGoods::getTradeId, id));
        return R.ok(tradeGoods);
    }
}
