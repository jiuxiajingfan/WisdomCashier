//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.li.wisdomcashier.service.impl;

import com.li.wisdomcashier.service.CaptchaCacheService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CaptchaServiceFactory {

    protected static final Map<String, CaptchaCacheService> STRATEGY_MAP = new HashMap<>();

    public CaptchaServiceFactory() {
    }

    public static void put(String type,CaptchaCacheService cacheService){
        STRATEGY_MAP.put(type,cacheService);
    }

    public static  CaptchaCacheService getCache(String cacheType) {
        return STRATEGY_MAP.get(cacheType);
    }

}
