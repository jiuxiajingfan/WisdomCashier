package com.li.wisdomcashier.controller.trade;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.li.wisdomcashier.controller.trade.dto.QueryTradeDTO;
import com.li.wisdomcashier.controller.trade.vo.TradeVO;
import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.TradeGoods;
import com.li.wisdomcashier.entry.TradeRefund;
import com.li.wisdomcashier.service.TradeRefundService;
import com.li.wisdomcashier.service.TradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trade")
@Tag(name = "订单相关")
public class TradeController {
    @Resource
    private TradeService tradeService;

    @Resource
    private TradeRefundService tradeRefundService;

    @GetMapping("/queryLeast")
    @Operation(summary = "查询店铺最近十笔交易记录")
    @PreAuthorize("@ss.hasPermission(#sid,1,2,3)")
    R<List<TradeVO>> queryLeast(@RequestParam("sid") String sid){
        return tradeService.queryLeast(Long.parseLong(sid));
    }

    @PostMapping("/queryTradePage")
    @Operation(summary = "交易记录")
    @PreAuthorize("@ss.hasPermission(#dto.sid,1,2,3)")
    public R<IPage<TradeVO>> queryTradePage(@RequestBody @Validated QueryTradeDTO dto){
        return tradeService.queryTradePage(dto);
    }

    @GetMapping("/queryRefund")
    @Operation(summary = "查询退款记录")
    @PreAuthorize("@ss.hasPermission(#sid,1,2)")
    R<List<TradeRefund>> queryRefund(@RequestParam("id") String id,@RequestParam("sid") String sid){
        return tradeRefundService.queryRefund(Long.parseLong(sid),Long.parseLong(id));
    }

    @GetMapping("/queryGoodsById")
    @Operation(summary = "更具订单查询详情")
    R<List<TradeGoods>> queryGoodsById(@RequestParam("id") String id){
        return tradeService.queryGoodsById(Long.parseLong(id));
    }

}
