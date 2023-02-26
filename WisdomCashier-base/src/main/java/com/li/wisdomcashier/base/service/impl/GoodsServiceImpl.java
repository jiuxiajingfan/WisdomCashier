package com.li.wisdomcashier.base.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.po.Goods;
import com.li.wisdomcashier.base.entity.po.GoodsVO;
import com.li.wisdomcashier.base.entity.pojo.GoodsApi;
import com.li.wisdomcashier.base.mapper.GoodsMapper;
import com.li.wisdomcashier.base.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;

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
}
