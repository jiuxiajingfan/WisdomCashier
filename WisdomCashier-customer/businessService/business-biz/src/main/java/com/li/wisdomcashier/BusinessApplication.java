package com.li.wisdomcashier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @ClassName ${NAME}
 * @Description TODO
 * @Author Nine
 * @Date 2023/10/26 20:19
 * @Version 1.0
 */
@EnableDiscoveryClient
@EnableResourceServer
@SpringBootApplication
public class BusinessApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class, args);
    }
}