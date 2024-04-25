//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.li.wisdomcashier.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CacheUtil {
    private static final Logger logger = LoggerFactory.getLogger(CacheUtil.class);
    private static final Map<String, Object> CACHE_MAP = new ConcurrentHashMap();
    private static Integer CACHE_MAX_NUMBER = 1000;
    private static ScheduledExecutorService scheduledExecutor;

    public CacheUtil() {
    }

    public static void init(int cacheMaxNumber, long second) {
        CACHE_MAX_NUMBER = cacheMaxNumber;
        if (second > 0L) {
            scheduledExecutor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    return new Thread(r, "thd-captcha-cache-clean");
                }
            }, new ThreadPoolExecutor.CallerRunsPolicy());
            scheduledExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    CacheUtil.refresh();
                }
            }, 10L, second, TimeUnit.SECONDS);
        }

    }

    public static void refresh() {
        logger.debug("local缓存刷新,清除过期数据");
        Iterator var0 = CACHE_MAP.keySet().iterator();

        while(var0.hasNext()) {
            String key = (String)var0.next();
            exists(key);
        }

    }

    public static void set(String key, String value, long expiresInSeconds) {
        if (CACHE_MAP.size() > CACHE_MAX_NUMBER * 2) {
            logger.info("CACHE_MAP达到阈值，clear map");
            clear();
        }

        CACHE_MAP.put(key, value);
        if (expiresInSeconds > 0L) {
            CACHE_MAP.put(key + "_HoldTime", System.currentTimeMillis() + expiresInSeconds * 1000L);
        }

    }

    public static void delete(String key) {
        CACHE_MAP.remove(key);
        CACHE_MAP.remove(key + "_HoldTime");
    }

    public static boolean exists(String key) {
        Long cacheHoldTime = (Long)CACHE_MAP.get(key + "_HoldTime");
        if (cacheHoldTime != null && cacheHoldTime != 0L) {
            if (cacheHoldTime < System.currentTimeMillis()) {
                delete(key);
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static String get(String key) {
        return exists(key) ? (String)CACHE_MAP.get(key) : null;
    }

    public static void clear() {
        logger.debug("have clean all key !");
        CACHE_MAP.clear();
    }
}
