package com.li.wisdomcashier.customer.controller;


import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.entity.po.TradeRefund;
import com.li.wisdomcashier.base.service.TradeRefundService;
import com.li.wisdomcashier.base.service.TradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lsw
 * @since 2023-03-11
 */
@RestController
@RequestMapping("/tradeRefund")
@Api(tags = "退款记录")
public class TradeRefundController {

    @Resource
    private TradeRefundService tradeRefundService;

    @GetMapping("/queryRefund")
    @ApiOperation(value = "查询退款记录")
    R<List<TradeRefund>> queryRefund(String id,String sid){
        return tradeRefundService.queryRefund(Long.parseLong(sid),Long.parseLong(id));
    }
}

