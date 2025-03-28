package com.li.wisdomcashier.strategy.pay;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.li.wisdomcashier.entry.dto.PayDTO;
import com.li.wisdomcashier.entry.dto.PayInfo;
import com.li.wisdomcashier.entry.dto.PayVO;
import com.li.wisdomcashier.entry.dto.RefundDTO;
import com.li.wisdomcashier.entry.vo.StatusVO;
import com.li.wisdomcashier.enums.pay.PayEnums;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName WXPayStrategy
 * @Description TODO
 * @Author Nine
 * @Date 2025/3/26 14:46
 * @Version 1.0
 */

@Component
@Slf4j
public class WXPayStrategy extends AbstractPayStrategy{
    @Override
    protected PayEnums getTypeEnum() {
        return PayEnums.WX;
    }

    @Override
    public PayVO pay(PayDTO payDTO) {
        return null;
    }

    @Override
    public StatusVO status(String tradeNo) {
        return null;
    }

    @Override
    public String cancel(String tradeNo) {
        return "";
    }

    @Override
    public String close(String tradeNo) {
        return "";
    }

    @Override
    public PayInfo detail(String tradeNo) {
        return null;
    }

    @Override
    public String refund(RefundDTO refundDTO) {
        return "";
    }

    @Override
    public Object payCallback(HttpServletRequest request) {
        String body = getRequestBody(request);
        String nonceStr = request.getHeader("Wechatpay-Nonce");
        String signature = request.getHeader("Wechatpay-Signature");
        String serialNo = request.getHeader("Wechatpay-Serial");
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String signStr = Stream.of(timestamp, nonceStr, body).collect(Collectors.joining("\n", "", "\n"));
        Map<String, String> map = new HashMap<>(2);
        try {
            if (result) {
                String plainBody = decryptBody(body, config);
                log.info("微信支付通知接口解密后的明文:{}", plainBody);
                cn.hutool.json.JSONObject parseObj = JSONUtil.parseObj(plainBody);
                //订单
                if (StringUtils.equals(parseObj.getStr("attach"), "order")) {
                    orderAsyncService.queryPay(parseObj.getStr("out_trade_no"));
                }
            } else {
                log.error("微信支付回调解析请求体数据异常");
            }
        } catch (GeneralSecurityException e) {
            log.error("微信支付回调异常:{}", e.toString());
        }
        map.put("code", "SUCCESS");
        map.put("message", "成功");
        return map;
    }

    @Override
    public Object refundCallback(HttpServletRequest request) {
        return null;
    }

    /**
     * 读取请求数据流
     */
    private static String getRequestBody(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        try (ServletInputStream inputStream = request.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("读取数据流异常:{}", JSONObject.toJSONString(e));
        }
        return sb.toString();
    }
}
