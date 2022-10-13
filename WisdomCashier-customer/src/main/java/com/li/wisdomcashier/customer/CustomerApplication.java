package com.li.wisdomcashier.customer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @ClassName CustomerApplication
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/10 16:41
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = { "com.li" })
@EnableAsync
@EnableSwagger2WebMvc
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}