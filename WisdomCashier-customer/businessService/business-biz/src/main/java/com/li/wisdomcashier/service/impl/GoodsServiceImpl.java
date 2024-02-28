package com.li.wisdomcashier.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.controller.goods.dto.BuyDTO;
import com.li.wisdomcashier.controller.goods.dto.DeleteDTO;
import com.li.wisdomcashier.controller.goods.dto.GoodQueryDTO;
import com.li.wisdomcashier.controller.goods.dto.GoodsDTO;
import com.li.wisdomcashier.controller.goods.vo.GoodsVO;
import com.li.wisdomcashier.convert.GoodsConvert;
import com.li.wisdomcashier.entry.Goods;
import com.li.wisdomcashier.entry.GoodsApi;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.Trade;
import com.li.wisdomcashier.entry.dto.PayVO;
import com.li.wisdomcashier.enums.trade.TradeEnum;
import com.li.wisdomcashier.exception.BusinessException;
import com.li.wisdomcashier.mapper.GoodsMapper;
import com.li.wisdomcashier.mapper.TradeMapper;
import com.li.wisdomcashier.service.GoodsService;
import com.li.wisdomcashier.utils.CommonUtils;
import com.li.wisdomcashier.utils.RedisUtils;
import com.li.wisdomcashier.utils.UserUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName GoodsServiceImpl
 * @Description TODO
 * @Author Nine
 * @Date 2024/1/2 22:01
 * @Version 1.0
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Value("${leaf.url}")
    private String leafUrl;

    @Value("${leaf.key}")
    private String leaKey;

    //    @Value("${goodsAPI.app_id}")
    private String apiId;

    //    @Value("${goodsAPI.app_secret}")
    private String apiSecret;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private TradeMapper tradeMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Override
    public R<Goods> reqGood(String gid) {
        if (StringUtils.isBlank(gid)) {
            return R.error("参数错误！");
        }
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("barcode", gid);
        paramsMap.put("app_id", apiId);
        paramsMap.put("app_secret", apiSecret);
        String result = HttpUtil.get("https://www.mxnzp.com/api/barcode/goods/details", paramsMap);
        Goods goods = new Goods();
        goods.setGid(gid);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        if (CommonUtils.compare(jsonObject.getStr("code"), "0")) {
            return R.ok(goods);
        }
        GoodsApi data = jsonObject.getBean("data", GoodsApi.class);
        goods.setName(data.getGoodsName());
        goods.setPriceOut(new BigDecimal(data.getPrice()));
        goods.setMetrology(data.getStandard());
        return R.ok(goods);
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
    public R<GoodsVO> getGoods(String gid, Long sid) {
        List<Goods> goods = goodsMapper.selectList(Wrappers.lambdaQuery(Goods.class)
                .eq(Goods::getGid, gid).eq(Goods::getSid, sid));
        if (CollectionUtils.isEmpty(goods)) {
            return R.error("不存在该商品！");
        }
        GoodsVO copy = GoodsConvert.INSTANCE.toGoodsVO(goods.getFirst());
        copy.setPriceOut(String.format("%.2f", goods.getFirst().getPriceOut()));
        copy.setPriceIn(String.format("%.2f", goods.getFirst().getPriceIn()));
        copy.setPriceVip(String.format("%.2f", goods.getFirst().getPriceVip()));
        return R.ok(copy);
    }


    @Override
    public R<String> buy(BuyDTO buyDTO) {
        //获取id
        String body = HttpRequest.get(leafUrl + leaKey)
                .execute().body();
        long id;
        try {
            id = Long.parseLong(body);
        } catch (NumberFormatException e) {
            throw new BusinessException("访问id生成器获取id失败！");
        }
        Trade trade = new Trade();
        trade.setId(id);
        trade.setIncome(new BigDecimal(buyDTO.getSum()));
        trade.setCreateTime(LocalDateTime.now());
        trade.setStatus(TradeEnum.WAITING.getCode());
        trade.setSid(Long.parseLong(buyDTO.getSid()));
        trade.setOperater(UserUtils.getUser().getId());
        tradeMapper.insert(trade);
        redisUtils.set(body, JSONUtil.toJsonStr(buyDTO.getGoods()), 600);
        redisUtils.set(body + "vip", buyDTO.getVip(), 600);
        return R.ok(body, "订单创建成功，等待支付！");
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

}
