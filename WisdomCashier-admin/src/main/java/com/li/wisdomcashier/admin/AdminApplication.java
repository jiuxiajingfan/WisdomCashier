package com.li.wisdomcashier.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @ClassName AdminApplication
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/10 16:14
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = { "com.li" })
@EnableSwagger2WebMvc
@EnableAsync
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
