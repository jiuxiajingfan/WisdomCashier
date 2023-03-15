package com.li.wisdomcashier.customer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.dto.QueryMoneyDTO;
import com.li.wisdomcashier.base.entity.dto.QueryTradeDTO;
import com.li.wisdomcashier.base.entity.dto.RefundDTO;
import com.li.wisdomcashier.base.entity.po.TradeGoods;
import com.li.wisdomcashier.base.entity.vo.EChartVO;
import com.li.wisdomcashier.base.entity.vo.TradeVO;
import com.li.wisdomcashier.base.service.TradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @GetMapping("/queryLeast")
    @ApiOperation(value = "查询店铺最近十笔交易记录")
    R<List<TradeVO>> queryLeast(String sid){
        return tradeService.queryLeast(Long.parseLong(sid));
    }


    @GetMapping("/queryGoodsById")
    @ApiOperation(value = "更具订单查询详情")
    R<List<TradeGoods>> queryGoodsById(String id){
        return tradeService.queryGoodsById(Long.parseLong(id));
    }

    @PostMapping("/queryTradePage")
    @ApiOperation(value = "交易记录")
    public R<IPage<TradeVO>> queryTradePage(@RequestBody @Validated QueryTradeDTO queryTradeDTO){
        return tradeService.queryTradePage(queryTradeDTO);
    }

    @PostMapping("/cashTradeRefund")
    @ApiOperation(value = "现金退款")
    public R<String> cashTradeRefund(@RequestBody @Validated RefundDTO refundDTO){
        return tradeService.cashTradeRefund(refundDTO);
    }

    @PostMapping("/currentTradeMoney")
    @ApiOperation(value = "最近交易")
    public R<EChartVO> currentTradeMoney(@RequestBody @Validated QueryMoneyDTO queryMoneyDTO){
        return tradeService.currentTradeMoney(queryMoneyDTO);
    }
}

