package com.li.wisdomcashier.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.po.Trade;
import com.li.wisdomcashier.base.entity.po.TradeRefund;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.enums.TradeEnum;
import com.li.wisdomcashier.base.mapper.TradeMapper;
import com.li.wisdomcashier.base.mapper.TradeRefundMapper;
import com.li.wisdomcashier.base.service.TradeRefundService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.UserUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lsw
 * @since 2023-03-11
 */
@Service
public class TradeRefundServiceImpl extends ServiceImpl<TradeRefundMapper, TradeRefund> implements TradeRefundService {

    @Resource
    private TradeRefundMapper tradeRefundMapper;

    @Resource
    private TradeMapper tradeMapper;

    @Override
    @Async
    public void TradeRefundRecord(TradeRefund tradeRefund) {
        tradeRefundMapper.insert(tradeRefund);
        Trade trade = tradeMapper.selectById(tradeRefund.getSid());
        List<TradeRefund> tradeRefunds = tradeRefundMapper.selectList(Wrappers.lambdaQuery(TradeRefund.class).eq(TradeRefund::getSid, tradeRefund.getSid()));
        BigDecimal reduce = tradeRefunds.stream().filter(e->
            e.getStatus()==1
        ).map(TradeRefund::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        if(reduce.compareTo(trade.getIncome())==0){
            trade.setStatus(TradeEnum.REFUNDALL.getCode());
        }
        else{
            trade.setStatus(TradeEnum.REFUND.getCode());
        }
        tradeMapper.updateById(trade);
    }

    @Override
    public R<List<TradeRefund>> queryRefund(Long sid, Long id) {
        if(Objects.isNull(sid)||Objects.isNull(id))
            return R.error("参数错误！");
        if(!UserUtils.hasPermissions(sid, RoleEnum.SHOP.getCode())){
            throw new AuthorizationException("无权操作！");
        }
        List<TradeRefund> tradeRefunds = tradeRefundMapper.selectList(Wrappers.lambdaQuery(TradeRefund.class)
                .eq(TradeRefund::getSid, id)
        );
        return R.ok(tradeRefunds);
    }

    @Override
    @Scheduled(cron = "")
    public void checkDate() {

    }
}
