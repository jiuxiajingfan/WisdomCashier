package com.li.wisdomcashier.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.controller.goods.dto.*;
import com.li.wisdomcashier.controller.goods.vo.GoodsVO;
import com.li.wisdomcashier.entry.Goods;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.Trade;
import com.li.wisdomcashier.entry.dto.PayVO;
import com.li.wisdomcashier.enums.trade.TradeEnum;
import com.li.wisdomcashier.exception.BusinessException;
import com.li.wisdomcashier.mapper.TradeMapper;
import com.li.wisdomcashier.service.GoodsService;
import com.li.wisdomcashier.utils.UserUtils;
import jakarta.annotation.Resource;
import jakarta.validation.ReportAsSingleViolation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName GoodsServiceImpl
 * @Description TODO
 * @Author Nine
 * @Date 2024/1/2 22:01
 * @Version 1.0
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Value("${leaf.url}")
    private String leafUrl;

    @Value("${leaf.key}")
    private String leaKey;

    @Resource
    protected RabbitTemplate rabbitTemplate;

    @Resource
    private TradeMapper tradeMapper;

    @Override
    public R<Goods> reqGood(String gid) {
        return null;
    }

    @Override
    public R<String> addGood(GoodsDTO goods) {
        return null;
    }

    @Override
    public R<IPage<Goods>> getGoodPage(GoodQueryDTO goodQueryDTO) {
        return null;
    }

    @Override
    public R<String> updateGood(GoodsDTO good) {
        return null;
    }

    @Override
    public R<GoodsVO> getGood(String gid, Long sid) {
        return null;
    }


    @Override
    public R<String> buy(BuyDTO buyDTO) {
        //获取id
        String body = HttpRequest.get(leafUrl + leaKey)
                .execute().body();
        Long id;
       try {
         id =  Long.parseLong(body);
       } catch (NumberFormatException e) {
           throw new  BusinessException("访问id生成器获取id失败！");
       }
        Trade trade = new Trade();
        trade.setId(id);
        trade.setIncome(new BigDecimal(buyDTO.getSum()));
        trade.setCreateTime(LocalDateTime.now());
        trade.setStatus(TradeEnum.WAITING.getCode());
        trade.setOperater(UserUtils.getUser().getId());
        tradeMapper.insert(trade);
        return R.ok(body);
    }

    @Override
    public void failTradeLogAsunc(String tradeNo, Long sid, Integer type) {

    }

    @Override
    public R<String> deleteGood(DeleteDTO deleteDTO) {
        return null;
    }

    @Override
    public R<IPage<Goods>> getGoodTemporaryPage(GoodQueryDTO goodQueryDTO) {
        return null;
    }

    @Override
    public R<String> updateGoodImg(PayVO payVO) {
        return null;
    }

    @Override
    public boolean saveBatch(Collection<Goods> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<Goods> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<Goods> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(Goods entity) {
        return false;
    }

    @Override
    public Goods getOne(Wrapper<Goods> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<Goods> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<Goods> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<Goods> getBaseMapper() {
        return null;
    }

    @Override
    public Class<Goods> getEntityClass() {
        return null;
    }
}
