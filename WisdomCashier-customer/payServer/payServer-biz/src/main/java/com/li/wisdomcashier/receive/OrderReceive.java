package com.li.wisdomcashier.receive;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.li.wisdomcashier.entry.dto.BuyGoodsDTO;
import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.po.Trade;
import com.li.wisdomcashier.entry.po.TradeGoods;
import com.li.wisdomcashier.entry.vo.StatusVO;
import com.li.wisdomcashier.enums.trade.TradeEnum;
import com.li.wisdomcashier.mapper.GoodsMapper;
import com.li.wisdomcashier.mapper.TradeMapper;
import com.li.wisdomcashier.service.DubboPayService;
import com.li.wisdomcashier.service.TradeGoodsService;
import com.li.wisdomcashier.utils.CommonUtils;
import com.li.wisdomcashier.utils.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.li.wisdomcashier.constant.MQConstant.*;


/**
 * @ClassName VerificationCodeReceive
 * @Description TODO
 * @Author Nine
 * @Date 2023/7/26 16:35
 * @Version 1.0
 */
@Component
@Slf4j
public class OrderReceive {

    @Resource
    private DubboPayService dubboPayService;

    @Resource
    private ScheduledThreadPoolExecutor scheduler;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private TradeMapper tradeMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private TradeGoodsService tradeGoodsService;

    private final Map<String, Future> futureTaskMap = new ConcurrentHashMap<>(20);

    /**
     * 轮询查询订单状态，并更新订单状态
     *
     * @param payDTO
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = ROUTING_MSG_ORDER_CYCLE),
            exchange = @Exchange(name = ROUTING_EXCHANGE_ORDER),
            key = ROUTING_KEY_ORDER_CYCLE
    ))
    public void cycleQuery(PayDTO payDTO) {
        log.info("收到支付订单消息");
        AtomicInteger count = new AtomicInteger(0);
        Runnable getResultTask = () -> {
            log.info("查询订单{}", payDTO.getRemoteId());
            //60次设置为失败，取消订单
            if (60 <= count.get()) {
                dubboPayService.cancel(payDTO.getType(), payDTO.getRemoteId());
                tradeMapper.update(null, Wrappers.lambdaUpdate(Trade.class)
                        .set(Trade::getStatus, TradeEnum.FAIL.getCode())
                        .set(Trade::getRemoteNo, payDTO.getRemoteId())
                        .set(Trade::getType, payDTO.getType())
                        .set(Trade::getPayer, payDTO.getUserID())
                        .set(Trade::getMsg, "支付超时")
                        .eq(Trade::getId, payDTO.getId())
                );
                removeTask(payDTO.getRemoteId());
            }
            count.getAndIncrement();
            StatusVO status = dubboPayService.status(payDTO.getType(), payDTO.getRemoteId());
            if (CommonUtils.compare(status.getStatus(), TradeEnum.FINISH.getDes())) {
                //更新库
                tradeMapper.update(null, Wrappers.lambdaUpdate(Trade.class)
                        .set(Trade::getStatus, TradeEnum.FINISH.getCode())
                        .set(Trade::getRemoteNo, payDTO.getRemoteId())
                        .set(Trade::getPayer, payDTO.getUserID())
                        .set(Trade::getType, payDTO.getType())
                        .set(Trade::getMsg, "支付成功")
                        .eq(Trade::getId, payDTO.getId())
                        .eq(Trade::getStatus,TradeEnum.WAITING.getCode())
                );
                //通知
                rabbitTemplate.convertAndSend(ROUTING_EXCHANGE_ORDER, ROUTING_KEY_ORDER_FINISH, payDTO);
                removeTask(payDTO.getRemoteId());
            }
        };
        //5秒一次 支付回调网关速度同步更快，作为网关兜底
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleWithFixedDelay(getResultTask, 5, 5, TimeUnit.SECONDS);
        futureTaskMap.put(payDTO.getRemoteId(), scheduledFuture);
    }

    /**
     * 更新订单详情
     *
     * @param payDTO
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = ROUTING_MSG_ORDER_FINISH),
            exchange = @Exchange(name = ROUTING_EXCHANGE_ORDER),
            key = ROUTING_KEY_ORDER_FINISH
    ))
    @Transactional(rollbackFor = Exception.class)
    public void updateDetail(PayDTO payDTO) {
        String s = (String) redisUtils.get(payDTO.getId());
        Integer vip = (Integer) redisUtils.get(payDTO.getId() + "vip");
        List<BuyGoodsDTO> goodsList = JSON.parseArray(s, BuyGoodsDTO.class);
        List<TradeGoods> collect = goodsList.stream().map(e -> {
            TradeGoods tradeGoods = new TradeGoods();
            tradeGoods.setGid(e.getGid())
                    .setName(e.getName())
                    .setTradeId(Long.parseLong(payDTO.getId()))
                    .setNum(e.getNum())
                    .setPrice(vip == 1 ? e.getPriceVip() : e.getPriceOut())
                    .setPriceIn(e.getPriceIn())
                    .setType(e.getType())
                    .setPriceOutSum(tradeGoods.getPrice().multiply(new BigDecimal(e.getNum())))
                    .setPriceInSum(e.getPriceIn().multiply(new BigDecimal(e.getNum())));
            goodsMapper.reduceNum(payDTO.getShopId(), e.getGid(), e.getNum());
            return tradeGoods;
        }).toList();
        tradeGoodsService.saveBatch(collect);
    }


    void removeTask(String key) {
        Future future = futureTaskMap.get(key);
        if (null != future) {
            boolean isCancel = future.cancel(true);
            if (isCancel) {
                futureTaskMap.remove(key);
            }
        }
    }
}
