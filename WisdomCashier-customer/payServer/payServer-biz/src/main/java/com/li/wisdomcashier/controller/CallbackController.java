package com.li.wisdomcashier.controller;

import com.li.wisdomcashier.enums.pay.PayEnums;
import com.li.wisdomcashier.strategy.pay.PayStrategyFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.annotation.security.PermitAll;

import java.util.Map;

/**
 * @ClassName CallbackController
 * @Description TODO
 * @Author Nine
 * @Date 2025/3/28 16:38
 * @Version 1.0
 */

@RestController
@RequestMapping("/callback")
@Tag(name = "支付回调相关")
public class CallbackController {



    @PermitAll
    @PostMapping("/wx/callBack")
    @Operation(summary = "微信支付通知接口")
    public Map<String, String> weChatPayCallback(HttpServletRequest request){
        return ( Map<String, String>) PayStrategyFactory.getPayStrategy(PayEnums.WX.getType()).payCallback(request);
    }

    @PermitAll
    @PostMapping("/wx/refund/callBack")
    @Operation(summary = "微信支付退款通知接口")
    public Map<String, String> weChatRefundCallBack(HttpServletRequest request){
        return ( Map<String, String>) PayStrategyFactory.getPayStrategy(PayEnums.WX.getType()).refundCallback(request);
    }

    @PermitAll
    @PostMapping("/zfb/callBack")
    @Operation(summary = "支付宝支付通知接口")
    public Map<String, String> aliPayCallback(HttpServletRequest request){
        return ( Map<String, String>) PayStrategyFactory.getPayStrategy(PayEnums.ZFB.getType()).payCallback(request);
    }

    @PermitAll
    @PostMapping("/zfb/refund/callBack")
    @Operation(summary = "支付宝支付退款通知接口")
    public Map<String, String> aliRefundCallBack(HttpServletRequest request){
        return ( Map<String, String>) PayStrategyFactory.getPayStrategy(PayEnums.ZFB.getType()).refundCallback(request);
    }

}
