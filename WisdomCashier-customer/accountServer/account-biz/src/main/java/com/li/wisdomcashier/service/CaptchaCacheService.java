package com.li.wisdomcashier.service;

public interface CaptchaCacheService {
    void set(String var1, String var2, long var3);

    boolean exists(String var1);

    void delete(String var1);

    String get(String var1);

    String type();

    void init();

    default Long increment(String key, long val) {
        return 0L;
    }
}