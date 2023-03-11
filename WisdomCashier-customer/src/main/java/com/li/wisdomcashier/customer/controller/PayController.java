package com.li.wisdomcashier.customer.controller;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.li.wisdomcashier.base.common.R;
import com.li.wisdomcashier.base.common.UnCheck;
import com.li.wisdomcashier.base.entity.dto.AliPayDTO;
import com.li.wisdomcashier.base.entity.dto.PayDTO;
import com.li.wisdomcashier.base.entity.dto.RefundDTO;
import com.li.wisdomcashier.base.service.AlipayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName PayController
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/4 13:06
 * @Version 1.0
 */
@RestController
@RequestMapping("/pay")
@Api(tags = "支付相关")
public class PayController {

    @Resource
    private AlipayService alipayService;

    @PostMapping("/aliPay")
    @ApiOperation(value = "支付宝支付")
    public R<PayDTO> aliPay(@RequestBody AliPayDTO aliPayDTO){
    return alipayService.aliPay(aliPayDTO);
    }

    @GetMapping("/queryAliPay")
    @ApiOperation(value = "支付宝交易查询")
    public R<String> queryAliPay(String tradeNo){
        return alipayService.queryAliPay(tradeNo);
    }

    @GetMapping("/cancelPay")
    @ApiOperation(value = "支付宝交易撤销")
    R<String> cancelPay(@Param("tradeNo") String tradeNo,@Param("sid") String sid){
        return alipayService.cancelPay(tradeNo, Long.parseLong(sid));
    }

    @GetMapping("/closePay")
    @ApiOperation(value = "支付宝交易关闭")
    R<String> closePay(@Param("tradeNo") String tradeNo,@Param("sid") String sid){
        return alipayService.closePay(tradeNo, Long.parseLong(sid));
    }

    @PostMapping("/refundPay")
    @ApiOperation(value = "支付宝交易退款")
    R<String> refundPay(@RequestBody @Validated RefundDTO refundDTO){
        return alipayService.refundPay(refundDTO);
    }

    @PostMapping("/queryRefund")
    @ApiOperation(value = "支付宝交易退款查询")
    R<String> queryRefund(@RequestBody @Validated RefundDTO refundDTO){
        return alipayService.queryRefund(refundDTO);
    }

}
