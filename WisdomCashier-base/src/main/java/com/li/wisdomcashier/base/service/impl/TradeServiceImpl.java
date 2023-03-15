package com.li.wisdomcashier.base.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.QueryMoneyDTO;
import com.li.wisdomcashier.base.entity.dto.QueryTradeDTO;
import com.li.wisdomcashier.base.entity.dto.RefundDTO;
import com.li.wisdomcashier.base.entity.po.Trade;
import com.li.wisdomcashier.base.entity.po.TradeGoods;
import com.li.wisdomcashier.base.entity.po.TradeRefund;
import com.li.wisdomcashier.base.entity.vo.EChartVO;
import com.li.wisdomcashier.base.entity.vo.TradeVO;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.mapper.TradeGoodsMapper;
import com.li.wisdomcashier.base.mapper.TradeMapper;
import com.li.wisdomcashier.base.mapper.TradeRefundMapper;
import com.li.wisdomcashier.base.service.TradeRefundService;
import com.li.wisdomcashier.base.service.TradeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.UserUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
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

    @Resource
    private TradeRefundService tradeRefundService;

    @Resource
    private TradeRefundMapper tradeRefundMapper;

    @Override
    public R<List<TradeVO>> queryLeast(Long sid) {
        if (Objects.isNull(sid))
            return R.error("店铺ID不能为空！");
        if (!UserUtils.hasPermissions(sid, RoleEnum.SHOP.getCode())) {
            throw new AuthorizationException("无权操作！");
        }
        List<Trade> trades = tradeMapper.selectLesat(sid, UserUtils.getUser().getId());
        List<TradeVO> collect = trades.stream().map(e -> {
                    TradeVO copy = CglibUtil.copy(e, TradeVO.class);
                    copy.setIncome(String.format("%.2f", e.getIncome()));
                    return copy;
                }
        ).collect(Collectors.toList());
        return R.ok(collect);
    }

    public R<List<TradeGoods>> queryGoodsById(Long id) {
        if (Objects.isNull(id))
            return R.error("订单号不能为空！");
        List<TradeGoods> tradeGoods = tradeGoodsMapper.selectList(Wrappers.lambdaQuery(TradeGoods.class)
                .eq(TradeGoods::getTradeId, id));
        return R.ok(tradeGoods);
    }

    @Override
    public R<IPage<TradeVO>> queryTradePage(QueryTradeDTO queryTradeDTO) {
        if (!UserUtils.hasPermissions(Long.parseLong(queryTradeDTO.getSid()), RoleEnum.SHOP.getCode())) {
            throw new AuthorizationException("无权操作！");
        }
        if (!Objects.isNull(queryTradeDTO.getEndTime())) {
            if (queryTradeDTO.getStartTime().compareTo(queryTradeDTO.getEndTime()) == 0) {
                queryTradeDTO.setEndTime(queryTradeDTO.getEndTime().plusDays(1));
            }
        }
        LambdaQueryWrapper<Trade> wrapper = Wrappers.lambdaQuery(Trade.class)
                .eq(Trade::getSid, queryTradeDTO.getSid())
                .likeRight(!Objects.isNull(queryTradeDTO.getId()), Trade::getId, queryTradeDTO.getId())
                .in(!CollectionUtils.isEmpty(queryTradeDTO.getStatus()), Trade::getStatus, queryTradeDTO.getStatus())
                .lt(!Objects.isNull(queryTradeDTO.getEndTime()), Trade::getCreateTime, queryTradeDTO.getEndTime())
                .ge(!Objects.isNull(queryTradeDTO.getStartTime()), Trade::getCreateTime, queryTradeDTO.getStartTime())
                .orderByDesc(Trade::getCreateTime);
        Page<Trade> page = new Page(queryTradeDTO.getCurrent(), queryTradeDTO.getPageSize());
        IPage<TradeVO> convert = tradeMapper.selectPage(page, wrapper).convert(e ->
                {
                    TradeVO copy = CglibUtil.copy(e, TradeVO.class);
                    copy.setIncome(String.format("%.2f", e.getIncome()));
                    return copy;
                }
        );
        return R.ok(convert);

    }

    @Override
    public R<String> cashTradeRefund(RefundDTO refundDTO) {
        //店铺管理员才能退款
        if(!UserUtils.hasPermissions(Long.parseLong(refundDTO.getSid()), RoleEnum.SHOPADMIN.getCode())){
            throw new AuthorizationException("无权操作！");
        }
        Trade trade = tradeMapper.selectById(Long.parseLong(refundDTO.getTradeNo()));
        List<TradeRefund> tradeRefunds = tradeRefundMapper.selectList(Wrappers.lambdaQuery(TradeRefund.class).eq(TradeRefund::getSid, Long.parseLong(refundDTO.getTradeNo())));
        BigDecimal reduce = tradeRefunds.stream().filter(e->
                e.getStatus()==1
        ).map(TradeRefund::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        TradeRefund tradeRefund = new TradeRefund();
        tradeRefund.setSid(Long.parseLong(refundDTO.getTradeNo()));
        tradeRefund.setNo(refundDTO.getNo());
        tradeRefund.setCtrateTime(LocalDateTime.now());
        tradeRefund.setMsg(refundDTO.getMsg());
        tradeRefund.setOperater(UserUtils.getUser().getId());
        tradeRefund.setType(1);
        tradeRefund.setMoney(new BigDecimal(refundDTO.getMoney()));
        if(reduce.add(new BigDecimal(refundDTO.getMoney())).compareTo(trade.getIncome())>0){
            tradeRefund.setStatus(0);
            tradeRefund.setErrMsg("总退款金额大于付款！");
            tradeRefundService.TradeRefundRecord(tradeRefund);
            return R.error("总退款金额大于付款！");
        }
        tradeRefund.setStatus(1);
        tradeRefundService.TradeRefundRecord(tradeRefund);
        return R.ok("退款成功！");
    }

    @Override
    public R<EChartVO> currentTradeMoney(QueryMoneyDTO queryMoneyDTO) {
        //店铺管理员权限接口
        if(!UserUtils.hasPermissions(Long.parseLong(queryMoneyDTO.getSid()), RoleEnum.SHOPADMIN.getCode())){
            throw new AuthorizationException("无权操作！");
        }
        List<Trade> trades = tradeMapper.selectList(Wrappers.lambdaQuery(Trade.class)
                .eq(Trade::getSid, Long.parseLong(queryMoneyDTO.getSid()))
                .le(Trade::getCreateTime, queryMoneyDTO.getTimeEnd())
                .ge(Trade::getCreateTime, queryMoneyDTO.getTimeStart())
        );
        Map<String, BigDecimal> collect = trades.stream().filter(e->{
            return e.getStatus()==1||e.getStatus()==2||e.getStatus()==7;
        }).collect(Collectors.groupingBy(e ->
                e.getCreateTime().format(queryMoneyDTO.getTimeType()==0?DateTimeFormatter.ISO_DATE:DateTimeFormatter.ofPattern("yyyy-MM")),
                Collectors.reducing(BigDecimal.ZERO, Trade::getIncome, BigDecimal::add)));
        List<String> date = this.getDate(queryMoneyDTO.getTimeStart(), queryMoneyDTO.getTimeEnd(), queryMoneyDTO.getTimeType());
        List<String> money = new ArrayList<>();
        date.forEach(e->{
            money.add(collect.getOrDefault(e,BigDecimal.ZERO).toString());
        });
        EChartVO eChartVO = new EChartVO();
        eChartVO.setName(date);
        eChartVO.setValue(money);
        return R.ok(eChartVO);
    }

    /**
     * 获取日期区间
     * @param a 起始
     * @param b 结束
     * @param type 返回值类型 0日 1月
     * @return
     */
    public List<String> getDate(LocalDateTime a,LocalDateTime b,Integer type){
        List<String> ans = new ArrayList<>();
        //日
        if(type == 0) {
            while (a.isBefore(b)) {
                ans.add(a.format(DateTimeFormatter.ISO_DATE));
                a = a.plusDays(1);
            }
        }
        //月
        else{
            while (a.isBefore(b)) {
                ans.add(a.format(DateTimeFormatter.ofPattern("yyyy-MM")));
                a = a.plusMonths(1);
            }
        }
        return ans;
    }

}
