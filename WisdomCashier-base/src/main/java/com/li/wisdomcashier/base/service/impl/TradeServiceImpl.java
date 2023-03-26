package com.li.wisdomcashier.base.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.common.StatusFailException;
import com.li.wisdomcashier.base.entity.dto.GoodQueryDTO;
import com.li.wisdomcashier.base.entity.dto.QueryMoneyDTO;
import com.li.wisdomcashier.base.entity.dto.QueryTradeDTO;
import com.li.wisdomcashier.base.entity.dto.RefundDTO;
import com.li.wisdomcashier.base.entity.po.*;
import com.li.wisdomcashier.base.entity.vo.EChartVO;
import com.li.wisdomcashier.base.entity.vo.TradeVO;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.mapper.GoodsMapper;
import com.li.wisdomcashier.base.mapper.TradeGoodsMapper;
import com.li.wisdomcashier.base.mapper.TradeMapper;
import com.li.wisdomcashier.base.mapper.TradeRefundMapper;
import com.li.wisdomcashier.base.service.TradeGoodsService;
import com.li.wisdomcashier.base.service.TradeRefundService;
import com.li.wisdomcashier.base.service.TradeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.UserUtils;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    TradeGoodsService tradeGoodsService;

    @Resource
    private TradeGoodsMapper tradeGoodsMapper;

    @Resource
    private TradeRefundService tradeRefundService;

    @Resource
    private TradeRefundMapper tradeRefundMapper;

    @Resource
    private GoodsMapper goodsMapper;


    @Override
    public R<List<TradeVO>> queryLeast(Long sid) {
        if (Objects.isNull(sid)) {
            return R.error("店铺ID不能为空！");
        }
        UserUtils.hasPermissions(sid.toString(), RoleEnum.SHOP.getCode());
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
        UserUtils.hasPermissions(queryTradeDTO.getSid(), RoleEnum.SHOP.getCode());
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
        UserUtils.hasPermissions(refundDTO.getSid(), RoleEnum.SHOPADMIN.getCode());
        Trade trade = tradeMapper.selectById(Long.parseLong(refundDTO.getTradeNo()));
        List<TradeRefund> tradeRefunds = tradeRefundMapper.selectList(Wrappers.lambdaQuery(TradeRefund.class).eq(TradeRefund::getSid, Long.parseLong(refundDTO.getTradeNo())));
        BigDecimal reduce = tradeRefunds.stream().filter(e ->
                e.getStatus() == 1
        ).map(TradeRefund::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        TradeRefund tradeRefund = new TradeRefund();
        tradeRefund.setSid(Long.parseLong(refundDTO.getTradeNo()));
        tradeRefund.setNo(refundDTO.getNo());
        tradeRefund.setCtrateTime(LocalDateTime.now());
        tradeRefund.setMsg(refundDTO.getMsg());
        tradeRefund.setOperater(UserUtils.getUser().getId());
        tradeRefund.setType(1);
        tradeRefund.setMoney(new BigDecimal(refundDTO.getMoney()));
        if (reduce.add(new BigDecimal(refundDTO.getMoney())).compareTo(trade.getIncome()) > 0) {
            tradeRefund.setStatus(0);
            tradeRefund.setErrMsg("总退款金额大于付款！");
            tradeRefundService.TradeRefundRecord(tradeRefund);
            return R.error("总退款金额大于付款！");
        }
        tradeRefund.setStatus(1);
        tradeRefundService.TradeRefundRecord(tradeRefund);
        return R.ok("退款成功！");
    }

    @SneakyThrows
    @Override
    public R<List<List<EChartVO>>> currentTradeMoney(QueryMoneyDTO queryMoneyDTO) {
        //店铺管理员权限接口
        UserUtils.hasPermissions(queryMoneyDTO.getSid(), RoleEnum.SHOPADMIN.getCode());
        //月份处理
        if (queryMoneyDTO.getTimeType() == 1) {
            LocalDateTime tt = queryMoneyDTO.getTimeEnd();
            LocalDateTime tt2 = queryMoneyDTO.getTimeStart();
            queryMoneyDTO.setTimeEnd(LocalDateTime.of(tt.getYear(), tt.plusMonths(1).getMonth(), 1, 0, 0, 0));
            queryMoneyDTO.setTimeStart(LocalDateTime.of(tt2.getYear(), tt2.getMonth(), 1, 0, 0, 0));
        }
        List<Trade> trades = tradeMapper.selectList(Wrappers.lambdaQuery(Trade.class)
                .eq(Trade::getSid, Long.parseLong(queryMoneyDTO.getSid()))
                .le(Trade::getCreateTime, queryMoneyDTO.getTimeEnd())
                .ge(Trade::getCreateTime, queryMoneyDTO.getTimeStart())
                .in(Trade::getStatus, Arrays.asList(3, 4, 6))
        );
        //月份处理
        if (queryMoneyDTO.getTimeType() == 1) {
            queryMoneyDTO.setTimeEnd(queryMoneyDTO.getTimeEnd().minusDays(1));
        }
        if (trades.isEmpty()) {
            throw new StatusFailException("区间内暂无数据！");
        }
        List<String> date = this.getDate(queryMoneyDTO.getTimeStart(), queryMoneyDTO.getTimeEnd(), queryMoneyDTO.getTimeType());
        ArrayList<List<EChartVO>> ans = new ArrayList<>();

        //折线图
        ArrayList<EChartVO> area = new ArrayList<>();
        if (queryMoneyDTO.getType() == 0) {
            //计算金额
            Map<String, BigDecimal> collect = trades.stream().collect(Collectors.groupingBy(e ->
                            e.getCreateTime().format(queryMoneyDTO.getTimeType() == 0 ? DateTimeFormatter.ISO_DATE : DateTimeFormatter.ofPattern("yyyy-MM")),
                    Collectors.reducing(BigDecimal.ZERO, Trade::getIncome, BigDecimal::add)));
            date.forEach(e -> {
                EChartVO eChartVO = new EChartVO();
                eChartVO.setName(e);
                eChartVO.setValue(collect.getOrDefault(e, BigDecimal.ZERO).toString());
                area.add(eChartVO);
            });
            ans.add(area);
        } else {
            //计算订单数
            Map<String, List<Trade>> collect = trades.stream().collect(Collectors.groupingBy(e ->
                    e.getCreateTime().format(queryMoneyDTO.getTimeType() == 0 ? DateTimeFormatter.ISO_DATE : DateTimeFormatter.ofPattern("yyyy-MM"))));
            date.forEach(e -> {
                EChartVO eChartVO = new EChartVO();
                eChartVO.setName(e);
                List<Trade> orDefault = collect.getOrDefault(e, new ArrayList<>());
                eChartVO.setValue(Integer.toString(orDefault.size()));
                area.add(eChartVO);
            });
            ans.add(area);

        }
        //饼图1
        List<Long> collect1 = trades.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<TradeGoods> tradeGoods = tradeGoodsMapper.selectList(Wrappers.lambdaQuery(TradeGoods.class).in(TradeGoods::getTradeId, collect1));
        BigDecimal out = tradeGoods.stream().map(TradeGoods::getPriceOutSum).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal in = tradeGoods.stream().map(TradeGoods::getPriceInSum).reduce(BigDecimal.ZERO, BigDecimal::add);
        ArrayList<EChartVO> pie = new ArrayList<>();
        pie.add(new EChartVO("成本", in.toString()));
        pie.add(new EChartVO("利润", out.subtract(in).toString()));
        ans.add(pie);

        //饼图2
        Map<String, BigDecimal> collect2 = tradeGoods.stream().collect(Collectors.groupingBy(TradeGoods::getType,
                Collectors.reducing(BigDecimal.ZERO, TradeGoods::getPriceOutSum, BigDecimal::add)));
        ArrayList<EChartVO> pie2 = new ArrayList<>();
        Iterator<Map.Entry<String, BigDecimal>> iterator = collect2.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, BigDecimal> next = iterator.next();
            pie2.add(new EChartVO(next.getKey(), next.getValue().toString()));
        }
        ans.add(pie2);
        return R.ok(ans);
    }

    @Override
    @Async
    public void AsyncSaveGood(List<Goods> goodsList, Long id, boolean isVip,String sid) {
        try {
            List<TradeGoods> collect = goodsList.stream().map(e -> {
                TradeGoods tradeGoods1 = new TradeGoods();
                tradeGoods1.setGid(e.getGid());
                tradeGoods1.setName(e.getName());
                tradeGoods1.setTradeId(id);
                tradeGoods1.setNum(e.getNum());
                if (isVip) {
                    tradeGoods1.setPrice(e.getPriceVip());
                } else {
                    tradeGoods1.setPrice(e.getPriceOut());
                }
                tradeGoods1.setPriceIn(e.getPriceIn());
                tradeGoods1.setType(e.getType());
                tradeGoods1.setPriceOutSum(tradeGoods1.getPrice().multiply(new BigDecimal(e.getNum())));
                tradeGoods1.setPriceInSum(e.getPriceIn().multiply(new BigDecimal(e.getNum())));
                Goods goods = goodsMapper.selectOne(Wrappers.lambdaQuery(Goods.class).eq(Goods::getSid, Long.parseLong(sid)).eq(Goods::getGid, e.getGid()));
                goods.setNum(goods.getNum() - e.getNum());
                goodsMapper.updateById(goods);
                return tradeGoods1;
            }).collect(Collectors.toList());
            tradeGoodsService.saveBatch(collect);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public R<List<Goods>> getGoodRankPage(GoodQueryDTO goodQueryDTO) {
        UserUtils.hasPermissions(goodQueryDTO.getSid(), RoleEnum.SHOPADMIN.getCode());
        List<Trade> trades = tradeMapper.selectList(Wrappers.lambdaQuery(Trade.class)
                .eq(Trade::getSid, Long.parseLong(goodQueryDTO.getSid()))
                .le(Trade::getCreateTime, LocalDateTime.now())
                .ge(Trade::getCreateTime, LocalDateTime.now().minusMonths(1))
                .in(Trade::getStatus, Arrays.asList(3, 4, 6))
        );
        if(trades.isEmpty())
            return R.ok(new ArrayList<>());
        List<Long> collect1 = trades.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<TradeGoods> tradeGoods = tradeGoodsMapper.selectList(Wrappers.lambdaQuery(TradeGoods.class).in(TradeGoods::getTradeId, collect1));
        Map<String, Integer> collect = tradeGoods.stream().collect(Collectors.groupingBy(TradeGoods::getGid, Collectors.summingInt(TradeGoods::getNum)));
        ArrayList<String> strings = new ArrayList<>();
        Iterator<Map.Entry<String, Integer>> iterator = collect.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, Integer> next = iterator.next();
            strings.add(next.getKey());
        }
        List<Goods> collect2 = goodsMapper.selectList(Wrappers.lambdaQuery(Goods.class)
                .eq(Goods::getSid, Long.parseLong(goodQueryDTO.getSid()))
                .in(Goods::getGid, strings)
        ).stream().map(e ->
        {
            e.setNum(collect.get(e.getGid()));
            return e;
        }).collect(Collectors.toList());
        collect2.sort(Comparator.comparing(Goods::getNum).reversed());
        return R.ok(collect2);

    }


    /**
     * 获取日期区间
     *
     * @param a    起始
     * @param b    结束
     * @param type 返回值类型 0日 1月
     * @return
     */
    public List<String> getDate(LocalDateTime a, LocalDateTime b, Integer type) {
        List<String> ans = new ArrayList<>();
        //日
        if (type == 0) {
            while (a.isBefore(b)) {
                ans.add(a.format(DateTimeFormatter.ISO_DATE));
                a = a.plusDays(1);
            }
        }
        //月
        else {
            while (a.isBefore(b)) {
                ans.add(a.format(DateTimeFormatter.ofPattern("yyyy-MM")));
                a = a.plusMonths(1);
            }
        }
        return ans;
    }

}
