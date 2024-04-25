package com.li.wisdomcashier.service.impl;
import com.li.wisdomcashier.entry.ResponseModel;
import com.li.wisdomcashier.entry.vo.CaptchaVO;
import com.li.wisdomcashier.enums.RepCodeEnum;
import org.apache.commons.lang3.StringUtils;
import com.li.wisdomcashier.service.CaptchaCacheService;

import java.util.Objects;
import java.util.Properties;

public interface FrequencyLimitHandler {
    String LIMIT_KEY = "AJ.CAPTCHA.REQ.LIMIT-%s-%s";

    ResponseModel validateGet(CaptchaVO var1);

    ResponseModel validateCheck(CaptchaVO var1);

    ResponseModel validateVerify(CaptchaVO var1);

    public static class DefaultLimitHandler implements FrequencyLimitHandler {
        private Properties config;
        private CaptchaCacheService cacheService;

        public DefaultLimitHandler(Properties config, CaptchaCacheService cacheService) {
            this.config = config;
            this.cacheService = cacheService;
        }

        private String getClientCId(CaptchaVO input, String type) {
            return String.format("AJ.CAPTCHA.REQ.LIMIT-%s-%s", type, input.getClientUid());
        }

        public ResponseModel validateGet(CaptchaVO d) {
            if (StringUtils.isEmpty(d.getClientUid())) {
                return null;
            } else {
                String getKey = this.getClientCId(d, "GET");
                String lockKey = this.getClientCId(d, "LOCK");
                if (Objects.nonNull(this.cacheService.get(lockKey))) {
                    return ResponseModel.errorMsg(RepCodeEnum.API_REQ_LOCK_GET_ERROR);
                } else {
                    String getCnts = this.cacheService.get(getKey);
                    if (Objects.isNull(getCnts)) {
                        this.cacheService.set(getKey, "1", 60L);
                        getCnts = "1";
                    }

                    this.cacheService.increment(getKey, 1L);
                    if (Long.valueOf(getCnts) > Long.parseLong(this.config.getProperty("captcha.req.get.minute.limit", "120"))) {
                        return ResponseModel.errorMsg(RepCodeEnum.API_REQ_LIMIT_GET_ERROR);
                    } else {
                        String failKey = this.getClientCId(d, "FAIL");
                        String failCnts = this.cacheService.get(failKey);
                        if (Objects.isNull(failCnts)) {
                            return null;
                        } else if (Long.valueOf(failCnts) > Long.parseLong(this.config.getProperty("captcha.req.get.lock.limit", "5"))) {
                            this.cacheService.set(lockKey, "1", Long.valueOf(this.config.getProperty("captcha.req.get.lock.seconds", "300")));
                            return ResponseModel.errorMsg(RepCodeEnum.API_REQ_LOCK_GET_ERROR);
                        } else {
                            return null;
                        }
                    }
                }
            }
        }

        public ResponseModel validateCheck(CaptchaVO d) {
            if (StringUtils.isEmpty(d.getClientUid())) {
                return null;
            } else {
                String key = this.getClientCId(d, "CHECK");
                String v = this.cacheService.get(key);
                if (Objects.isNull(v)) {
                    this.cacheService.set(key, "1", 60L);
                    v = "1";
                }

                this.cacheService.increment(key, 1L);
                return Long.valueOf(v) > Long.valueOf(this.config.getProperty("captcha.req.check.minute.limit", "600")) ? ResponseModel.errorMsg(RepCodeEnum.API_REQ_LIMIT_CHECK_ERROR) : null;
            }
        }

        public ResponseModel validateVerify(CaptchaVO d) {
            String key = this.getClientCId(d, "VERIFY");
            String v = this.cacheService.get(key);
            if (Objects.isNull(v)) {
                this.cacheService.set(key, "1", 60L);
                v = "1";
            }

            this.cacheService.increment(key, 1L);
            return Long.valueOf(v) > Long.valueOf(this.config.getProperty("captcha.req.verify.minute.limit", "600")) ? ResponseModel.errorMsg(RepCodeEnum.API_REQ_LIMIT_VERIFY_ERROR) : null;
        }
    }
}
