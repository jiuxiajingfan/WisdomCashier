package com.li.wisdomcashier.customer.controller;


import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.TradeDTO;
import com.li.wisdomcashier.base.entity.po.TradeGoods;
import com.li.wisdomcashier.base.service.TradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lsw
 * @since 2023-03-03
 */
@RestController
@Api(tags = "交易详情")
@RequestMapping("/trade")
public class TradeController {
    @Resource
    private TradeService tradeService;


    @Validated
    @GetMapping("/queryLeast")
    @ApiOperation(value = "查询店铺最近十笔交易记录")
    R<List<TradeDTO>> queryLeast( Long sid){
        return tradeService.queryLeast(sid);
    }

    @Validated
    @GetMapping("/queryGoodsById")
    @ApiOperation(value = "更具订单查询详情")
    R<List<TradeGoods>> queryGoodsById(Long id){
        return tradeService.queryGoodsById(id);
    }
}

