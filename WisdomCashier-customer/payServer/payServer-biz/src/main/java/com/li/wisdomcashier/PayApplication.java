package com.li.wisdomcashier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName ${NAME}
 * @Description TODO
 * @Author Nine
 * @Date 2023/12/24 22:09
 * @Version 1.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }
}