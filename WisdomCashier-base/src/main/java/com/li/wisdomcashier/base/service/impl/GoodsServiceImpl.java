package com.li.wisdomcashier.base.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.GoodDTO;
import com.li.wisdomcashier.base.entity.dto.GoodQueryDTO;
import com.li.wisdomcashier.base.entity.po.Goods;
import com.li.wisdomcashier.base.entity.po.GoodsVO;
import com.li.wisdomcashier.base.entity.pojo.GoodsApi;
import com.li.wisdomcashier.base.enums.RoleEnum;
import com.li.wisdomcashier.base.mapper.GoodsMapper;
import com.li.wisdomcashier.base.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.wisdomcashier.base.util.UserUtils;
import javafx.print.Collation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

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
        goods.setPriceOut(Double.parseDouble(data.getPrice()));
        goods.setMetrology(data.getStandard());
        return R.ok(goods);
    }

    @Override
    public R<String> addGood(GoodDTO good) {
        if(!UserUtils.hasPermissions(good.getSid(), RoleEnum.SHOP.getCode())){
            throw new AuthorizationException("无权操作！");
        }
        List<Goods> goods = goodsMapper.selectList(Wrappers.lambdaQuery(Goods.class).eq(Goods::getGid, good.getGid()).eq(Goods::getSid, good.getSid()));
        if(!CollectionUtils.isEmpty(goods)){
            return R.error("该条码已存在库中！添加失败");
        }
        Goods copy = CglibUtil.copy(good, Goods.class);
        if(good.getDate()==null)
        {
            good.setDate(LocalDate.now());
        }
        copy.setDeadline(good.getDate().plusDays(good.getShelfLife()));
        goodsMapper.insert(copy);
        return R.ok("新增商品成功！");
    }


    @Override
    public R<IPage<Goods>> getGoodPage(GoodQueryDTO goodQueryDTO) {
        if(!UserUtils.hasPermissions(goodQueryDTO.getSid(), RoleEnum.SHOP.getCode())){
            throw new AuthorizationException("无权操作！");
        }

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
        if(!UserUtils.hasPermissions(good.getSid(), RoleEnum.SHOP.getCode())){
            throw new AuthorizationException("无权操作！");
        }
        List<Goods> goods = goodsMapper.selectList(Wrappers.lambdaQuery(Goods.class).eq(Goods::getGid, good.getGid()).eq(Goods::getSid, good.getSid()));
        if(CollectionUtils.isEmpty(goods)){
            return R.error("不存在该商品！");
        }
        Goods copy = CglibUtil.copy(good, Goods.class);
        if(good.getDate()==null)
        {
            good.setDate(LocalDate.now());
        }
        copy.setDeadline(good.getDate().plusDays(good.getShelfLife()));
        goodsMapper.updateById(copy);
        return R.ok("更新成功！");
    }
}
