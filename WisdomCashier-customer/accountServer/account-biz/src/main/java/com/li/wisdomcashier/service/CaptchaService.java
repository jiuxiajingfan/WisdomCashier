package com.li.wisdomcashier.service;

import com.li.wisdomcashier.entry.ResponseModel;
import com.li.wisdomcashier.entry.vo.CaptchaVO;

import java.util.Properties;

public interface CaptchaService {
    void init();

    ResponseModel get(CaptchaVO var1);

    ResponseModel check(CaptchaVO var1);

    ResponseModel verification(CaptchaVO var1);

    String captchaType();

    void destroy(Properties var1);
}