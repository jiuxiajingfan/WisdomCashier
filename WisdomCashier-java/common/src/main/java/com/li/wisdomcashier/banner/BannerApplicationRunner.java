package com.li.wisdomcashier.banner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * 项目启动成功后，提供文档相关的地址
 */
@Slf4j
public class BannerApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        try {
            Thread.sleep(3000);
            log.info("\n-------------------\n\t项目启动成功\n-------------------");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
