package com.li.WisdomCashier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName ${NAME}
 * @Description TODO
 * @Author Nine
 * @Date 2023/7/10 13:19
 * @Version 1.0
 */

@SpringBootApplication
@EnableDiscoveryClient
public class oauth2Application {
    public static void main(String[] args) {
        SpringApplication.run(oauth2Application.class,args);
    }
}