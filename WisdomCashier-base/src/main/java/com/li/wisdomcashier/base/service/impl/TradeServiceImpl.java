package com.li.wisdomcashier.base.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.QueryTradeDTO;
import com.li.wisdomcashier.base.entity.po.Goods;
import com.li.wisdomcashier.base.entity.po.Trade;
import com.li.wisdomcashier.base.entity.po.TradeGoods;
import com.li.wisdomcashier.base.entity.vo.TradeVO;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.mapper.TradeGoodsMapper;
import com.li.wisdomcashier.base.mapper.TradeMapper;
import com.li.wisdomcashier.base.service.TradeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.UserUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
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
    public R<List<TradeVO>> queryLeast(Long sid) {
        if(Objects.isNull(sid))
            return R.error("店铺ID不能为空！");
        if(!UserUtils.hasPermissions(sid, RoleEnum.SHOP.getCode())){
            throw new AuthorizationException("无权操作！");
        }
        List<Trade> trades = tradeMapper.selectLesat(sid,UserUtils.getUser().getId());
        List<TradeVO> collect = trades.stream().map(e ->
                        CglibUtil.copy(e, TradeVO.class)
        ).collect(Collectors.toList());
        return R.ok(collect);
    }

    public R<List<TradeGoods>> queryGoodsById(Long id){
        if(Objects.isNull(id))
            return R.error("订单号不能为空！");
        List<TradeGoods> tradeGoods = tradeGoodsMapper.selectList(Wrappers.lambdaQuery(TradeGoods.class)
                .eq(TradeGoods::getTradeId, id));
        return R.ok(tradeGoods);
    }

    @Override
    public R<IPage<TradeVO>> queryTradePage(QueryTradeDTO queryTradeDTO) {
        if(!UserUtils.hasPermissions(queryTradeDTO.getSid(), RoleEnum.SHOP.getCode())){
            throw new AuthorizationException("无权操作！");
        }
        if(!Objects.isNull(queryTradeDTO.getEndTime())){
            if (queryTradeDTO.getStartTime().compareTo(queryTradeDTO.getEndTime()) == 0) {
                queryTradeDTO.setEndTime(queryTradeDTO.getEndTime().plusDays(1));
            }
        }
        LambdaQueryWrapper<Trade> wrapper = Wrappers.lambdaQuery(Trade.class)
                .eq(Trade::getSid,queryTradeDTO.getSid())
                .likeRight(!Objects.isNull(queryTradeDTO.getId()), Trade::getId, queryTradeDTO.getId())
                .in(!CollectionUtils.isEmpty(queryTradeDTO.getStatus()), Trade::getStatus, queryTradeDTO.getStatus())
                .lt(!Objects.isNull(queryTradeDTO.getEndTime()), Trade::getCreateTime, queryTradeDTO.getEndTime())
                .ge(!Objects.isNull(queryTradeDTO.getStartTime()), Trade::getCreateTime, queryTradeDTO.getStartTime())
                .orderByDesc(Trade::getCreateTime);
        Page<Trade> page = new Page(queryTradeDTO.getCurrent(),queryTradeDTO.getPageSize());
        IPage<TradeVO> convert = tradeMapper.selectPage(page, wrapper).convert(e ->
                    CglibUtil.copy(e, TradeVO.class)
        );
        return R.ok(convert);

    }

}
