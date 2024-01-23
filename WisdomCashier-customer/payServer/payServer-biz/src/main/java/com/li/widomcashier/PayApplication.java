package com.li.widomcashier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @ClassName ${NAME}
 * @Description TODO
 * @Author Nine
 * @Date 2023/12/24 22:09
 * @Version 1.0
 */
@EnableDiscoveryClient
@EnableResourceServer
@SpringBootApplication
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }
}