package com.li.wisdomcashier.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.li.wisdomcashier.entry.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;


/**
 * @ClassName GatewayConfig
 * @Description TODO
 * @Author Nine
 * @Date 2023/7/24 15:22
 * @Version 1.0
 */
@Component
public class GatewayConfig {
    /**
     *限流配置
     */
    @PostConstruct
    public void init(){
        BlockRequestHandler blockRequestHandler = (serverWebExchange, throwable) -> {
            R result = null;

            if (throwable instanceof FlowException){
                result = R.error("操作太快，请稍后再试");
            }else if (throwable instanceof DegradeException){
                result = R.error("服务繁忙，请稍后再试");
            }else if (throwable instanceof SystemBlockException){
                result = R.error("活动太火爆，请稍后再试");
            }

            // 返回数据
            return ServerResponse.status(HttpStatus.OK) // 响应状态码
                    .contentType(MediaType.APPLICATION_JSON)  // 响应数据类型，这里设置为json
                    .body(BodyInserters.fromValue(result)); // 响应数据
        };

        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }
}
