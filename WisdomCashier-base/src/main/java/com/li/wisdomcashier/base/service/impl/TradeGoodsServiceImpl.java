package com.li.wisdomcashier.base.service.impl;

import com.li.wisdomcashier.base.entity.po.Goods;
import com.li.wisdomcashier.base.entity.po.TradeGoods;
import com.li.wisdomcashier.base.mapper.TradeGoodsMapper;
import com.li.wisdomcashier.base.service.TradeGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
public class TradeGoodsServiceImpl extends ServiceImpl<TradeGoodsMapper, TradeGoods> implements TradeGoodsService {


}
