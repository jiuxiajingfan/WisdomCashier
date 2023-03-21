package com.li.wisdomcashier.base.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.cglib.CglibUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.BuyGoodDTO;
import com.li.wisdomcashier.base.entity.dto.DeleteDTO;
import com.li.wisdomcashier.base.entity.dto.GoodDTO;
import com.li.wisdomcashier.base.entity.dto.GoodQueryDTO;
import com.li.wisdomcashier.base.entity.po.Goods;
import com.li.wisdomcashier.base.entity.po.GoodsVO;
import com.li.wisdomcashier.base.entity.po.Trade;
import com.li.wisdomcashier.base.entity.po.TradeGoods;
import com.li.wisdomcashier.base.entity.pojo.GoodsApi;
import com.li.wisdomcashier.base.entity.pojo.QueryTrade;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.enums.TradeEnum;
import com.li.wisdomcashier.base.mapper.GoodsMapper;
import com.li.wisdomcashier.base.mapper.TradeMapper;
import com.li.wisdomcashier.base.service.AlipayService;
import com.li.wisdomcashier.base.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.service.TradeGoodsService;
import com.li.wisdomcashier.base.service.TradeService;
import com.li.wisdomcashier.base.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lsw
 * @since 2023-02-26
 */
@Service
@Slf4j
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Value("${goodsAPI.app_id}")
    private String apiId;

    @Value("${goodsAPI.app_secret}")
    private String apiSecret;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private TradeGoodsService tradeGoodsService;

    @Resource
    private TradeMapper tradeMapper;

    @Resource
    private AlipayService alipayService;

    @Resource
    private TradeService tradeService;

    @Override
    public R<Goods> reqGood(String gid) {
        if(StringUtils.isBlank(gid)){
            return R.error("参数错误！");
        }
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("barcode",gid);
        paramsMap.put("app_id",apiId);
        paramsMap.put("app_secret",apiSecret);
        String result = HttpUtil.get("https://www.mxnzp.com/api/barcode/goods/details", paramsMap);
        Goods goods = new Goods();
        goods.setGid(gid);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        if((Integer) jsonObject.get("code")==0){
            return R.ok(goods);
        }
        GoodsApi data = jsonObject.getBean("data", GoodsApi.class);
        goods.setName(data.getGoodsName());
        goods.setPriceOut(new BigDecimal(data.getPrice()));
        goods.setMetrology(data.getStandard());
        return R.ok(goods);
    }

    @Override
    public R<String> addGood(GoodDTO good) {
        UserUtils.hasPermissions(good.getSid(), RoleEnum.SHOP.getCode());
        List<Goods> goods = goodsMapper.selectList(Wrappers.lambdaQuery(Goods.class).eq(Goods::getGid, good.getGid()).eq(Goods::getSid, Long.parseLong(good.getSid())));
        if(!CollectionUtils.isEmpty(goods)){
            return R.error("该条码已存在库中！添加失败");
        }
        Goods copy = CglibUtil.copy(good, Goods.class);
        if(good.getDate()==null)
        {
            good.setDate(LocalDate.now());
        }
        copy.setPriceOut(new BigDecimal(good.getPriceOut()));
        copy.setPriceIn(new BigDecimal(good.getPriceIn()));
        copy.setPriceVip(new BigDecimal(good.getPriceVip()));
        copy.setProfit(copy.getPriceOut().subtract(copy.getPriceIn()));
        copy.setSid(Long.parseLong(good.getSid()));
        copy.setDeadline(good.getDate().plusDays(good.getShelfLife()));
        return goodsMapper.insert(copy)==1?R.ok("添加成功"):R.error("添加失败,请联系管理员查看问题");
    }


    @Override
    public R<IPage<Goods>> getGoodPage(GoodQueryDTO goodQueryDTO) {
        UserUtils.hasPermissions(goodQueryDTO.getSid(), RoleEnum.SHOP.getCode());
        Boolean flag = true;
        //判断是商品名还是条形码
        if(!StringUtils.isBlank(goodQueryDTO.getGid())) {
            flag = goodQueryDTO.getGid().matches("^[0-9]*$");
        }
        LambdaQueryWrapper<Goods> wrapper = Wrappers.lambdaQuery(Goods.class)
                .eq(Goods::getSid, goodQueryDTO.getSid())
                .eq(!StringUtils.isBlank(goodQueryDTO.getGid()) && flag, Goods::getGid, goodQueryDTO.getGid())
                .like(!StringUtils.isBlank(goodQueryDTO.getGid()) && !flag, Goods::getName, goodQueryDTO.getGid());
        Page<Goods> page = new Page(goodQueryDTO.getCurrent(),goodQueryDTO.getPageSize());
        Page<Goods> result = goodsMapper.selectPage(page, wrapper);
        if(CollectionUtils.isEmpty(result.getRecords())){
            return R.ok(new Page<>());
        }
        return R.ok(result);
    }

    @Override
    public R<String> updateGood(GoodDTO good) {
        UserUtils.hasPermissions(good.getSid(), RoleEnum.SHOP.getCode());
        Goods goods = goodsMapper.selectOne(Wrappers.lambdaQuery(Goods.class).eq(Goods::getGid, good.getGid()).eq(Goods::getSid, Long.parseLong(good.getSid())));
        if(Objects.isNull(goods)){
            return R.error("不存在该商品！");
        }
        goods.setPriceOut(new BigDecimal(good.getPriceOut()));
        goods.setPriceIn(new BigDecimal(good.getPriceIn()));
        goods.setPriceVip(new BigDecimal(good.getPriceVip()));
        if(goods.getPriceOut().compareTo(goods.getPriceIn())<0 || goods.getPriceVip().compareTo(goods.getPriceIn()) < 0)
            return R.error("售价不能小于进价！");
        goods.setProfit(goods.getPriceOut().subtract(goods.getPriceIn()));
        if(good.getDate()==null)
        {
            good.setDate(LocalDate.now());
        }
        goods.setType(good.getType());
        goods.setDeadline(good.getDate().plusDays(good.getShelfLife()));
        return goodsMapper.updateById(goods)==1?R.ok("更新成功！"):R.error("更新失败,请联系管理员查看问题");
    }

    @Override
    public R<GoodsVO> getGood(String gid, Long sid) {
        UserUtils.hasPermissions(sid.toString(), RoleEnum.SHOP.getCode());
        List<Goods> goods = goodsMapper.selectList(Wrappers.lambdaQuery(Goods.class).eq(Goods::getGid, gid).eq(Goods::getSid, sid));
        if(CollectionUtils.isEmpty(goods)){
            return R.error("不存在该商品！");
        }
        GoodsVO copy = CglibUtil.copy(goods.get(0), GoodsVO.class);
        copy.setPriceOut(String.format("%.2f",goods.get(0).getPriceOut()));
        copy.setPriceIn(String.format("%.2f",goods.get(0).getPriceIn()));
        copy.setPriceVip(String.format("%.2f",goods.get(0).getPriceVip()));
        return R.ok(copy);
    }

    @Override()
    @Transactional(rollbackFor = Exception.class)
    public R<String> buyGood(BuyGoodDTO buyGoodDTO) {
        UserUtils.hasPermissions(buyGoodDTO.getSid(), RoleEnum.SHOP.getCode());
        try {
            if(buyGoodDTO.getStatus()==TradeEnum.FINISH.getCode()) {
                Trade trade = new Trade();
                trade.setSid(Long.parseLong(buyGoodDTO.getSid()));
                trade.setIncome(new BigDecimal(buyGoodDTO.getSum()));
                trade.setCreateTime(LocalDateTime.now());
                trade.setType(buyGoodDTO.getType());
                trade.setRemoteNo(buyGoodDTO.getRemoteNo());
                trade.setStatus(buyGoodDTO.getStatus());
                trade.setMsg("交易成功");
                trade.setOperater(UserUtils.getUser().getId());
                if(!StringUtils.isBlank(buyGoodDTO.getId())) {
                    trade.setId(Long.parseLong(buyGoodDTO.getId()));
                }
                tradeMapper.insert(trade);
                tradeService.AsyncSaveGood(buyGoodDTO.getGoods(),trade.getId());
            }
            else{
                this.failTradeLogAsunc(buyGoodDTO.getRemoteNo(), Long.parseLong(buyGoodDTO.getSid()),buyGoodDTO.getType());
            }
            return R.ok("交易成功！");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.error("交易失败，请重试或联系管理员！");
        }
    }

    @Override
    public R<String> getRandID() {
        return R.ok(IdUtil.nanoId());
    }

    @Override
    public void failTradeLogAsunc(String tradeNo,Long sid,Integer type) {
        QueryTrade queryTrade = alipayService.queryPayDetil(tradeNo);
        Trade trade = new Trade();
        trade.setSid(sid);
        trade.setId(Long.parseLong(queryTrade.getTradeNo()));
        trade.setIncome(new BigDecimal(queryTrade.getSum()));
        trade.setCreateTime(LocalDateTime.now());
        trade.setType(type);
        trade.setRemoteNo(queryTrade.getRemoteNo());
        trade.setStatus(TradeEnum.CANCEL.getCode());
        trade.setOperater(UserUtils.getUser().getId());
        trade.setMsg("交易关闭");
        trade.setPayer(queryTrade.getPayUserId());
        tradeMapper.insert(trade);
    }

    @Override
    public R<String> deleteGood(DeleteDTO deleteDTO) {
        //管理员接口
        UserUtils.hasPermissions(deleteDTO.getSid(), RoleEnum.SHOPADMIN.getCode());
        Goods goods = goodsMapper.selectOne(Wrappers.lambdaQuery(Goods.class).eq(Goods::getGid, deleteDTO.getId()));
        if(Objects.isNull(goods))
            return R.error("不存在该商品！");
        return goodsMapper.deleteById(goods) ==1?R.ok("删除成功！"):R.error("删除失败,请联系管理员查看问题");
    }

    @Override
    public R<IPage<Goods>> getGoodTemporaryPage(GoodQueryDTO goodQueryDTO) {
        UserUtils.hasPermissions(goodQueryDTO.getSid(), RoleEnum.SHOP.getCode());
        LambdaQueryWrapper<Goods> wrapper = Wrappers.lambdaQuery(Goods.class)
                .eq(Goods::getSid,Long.parseLong(goodQueryDTO.getSid()))
                .le(Goods::getDeadline, LocalDate.now().plusDays(7));
        Page<Goods> page = new Page(goodQueryDTO.getCurrent(),goodQueryDTO.getPageSize());
        Page<Goods> result = goodsMapper.selectPage(page, wrapper);
        return R.ok(result);
    }

}
