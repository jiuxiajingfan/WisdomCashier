package com.li.wisdomcashier.controller.shop;

import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.dto.AliPayDTO;
import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.RefundDTO;
import com.li.wisdomcashier.service.AlipayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @ClassName PayController
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/4 13:06
 * @Version 1.0
 */
@RestController
@RequestMapping("/pay")
@Tag(name = "支付相关")
public class PayController {

    @DubboReference(version = "1.0",check = false)
    private AlipayService alipayService;

    @PostMapping("/aliPay")
    @Operation(summary = "支付宝支付")
    public R<PayDTO> aliPay(@RequestBody AliPayDTO aliPayDTO){
    return alipayService.aliPay(aliPayDTO);
    }

    @GetMapping("/queryAliPay")
    @Operation(summary = "支付宝交易查询")
    public R<String> queryAliPay(String tradeNo){
        return alipayService.queryAliPay(tradeNo);
    }

    @GetMapping("/cancelPay")
    @Operation(summary = "支付宝交易撤销")
    R<String> cancelPay(@Param("tradeNo") String tradeNo,@Param("sid") String sid){
        return alipayService.cancelPay(tradeNo, Long.parseLong(sid));
    }

    @GetMapping("/closePay")
    @Operation(summary = "支付宝交易关闭")
    R<String> closePay(@Param("tradeNo") String tradeNo,@Param("sid") String sid){
        return alipayService.closePay(tradeNo, Long.parseLong(sid));
    }

    @PostMapping("/refundPay")
    @Operation(summary = "支付宝交易退款")
    R<String> refundPay(@RequestBody @Validated RefundDTO refundDTO){
        return alipayService.refundPay(refundDTO);
    }

    @PostMapping("/queryRefund")
    @Operation(summary = "支付宝交易退款查询")
    R<String> queryRefund(@RequestBody @Validated RefundDTO refundDTO){
        return alipayService.queryRefund(refundDTO);
    }

}
