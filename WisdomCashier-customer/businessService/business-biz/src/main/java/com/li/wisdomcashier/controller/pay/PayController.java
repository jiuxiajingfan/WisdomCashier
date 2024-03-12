package com.li.wisdomcashier.controller.pay;

import com.li.wisdomcashier.entry.R;
import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.PayVO;
import com.li.wisdomcashier.entry.dto.RefundDTO;
import com.li.wisdomcashier.entry.vo.StatusVO;
import com.li.wisdomcashier.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
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

    @Resource
    PayService payService;

    @PostMapping("/pay")
    @Operation(summary = "支付")
    public R<PayVO> pay(@RequestBody @Validated PayDTO payDTO) {
        return payService.pay(payDTO);
    }

    @GetMapping("/status")
    @Operation(summary = "交易查询")
    public R<StatusVO> status(String tradeNo, Integer type) {
        return payService.status(type,tradeNo);
    }
//
//    @GetMapping("/cancelPay")
//    @Operation(summary = "支付宝交易撤销")
//    R<String> cancelPay(@Param("tradeNo") String tradeNo, @Param("sid") String sid) {
//        return payService.cancelPay(tradeNo, Long.parseLong(sid));
//    }
//
//    @GetMapping("/closePay")
//    @Operation(summary = "支付宝交易关闭")
//    R<String> closePay(@Param("tradeNo") String tradeNo, @Param("sid") String sid) {
//        return payService.closePay(tradeNo, Long.parseLong(sid));
//    }
//
//    @PostMapping("/refundPay")
//    @Operation(summary = "支付宝交易退款")
//    R<String> refundPay(@RequestBody @Validated RefundDTO refundDTO) {
//        return payService.refundPay(refundDTO);
//    }
//
//    @PostMapping("/queryRefund")
//    @Operation(summary = "支付宝交易退款查询")
//    R<String> queryRefund(@RequestBody @Validated RefundDTO refundDTO) {
//        return payService.queryRefund(refundDTO);
//    }

}
