//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.li.wisdomcashier.service.impl;

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

import com.li.wisdomcashier.entry.ResponseModel;
import com.li.wisdomcashier.entry.vo.CaptchaVO;
import com.li.wisdomcashier.enums.RepCodeEnum;
import com.li.wisdomcashier.service.CaptchaCacheService;
import com.li.wisdomcashier.service.CaptchaService;
import com.li.wisdomcashier.utils.AESUtil;
import com.li.wisdomcashier.utils.CacheUtil;
import com.li.wisdomcashier.utils.ImageUtils;
import com.li.wisdomcashier.utils.MD5Util;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCaptchaService implements CaptchaService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected static final String IMAGE_TYPE_PNG = "png";
    protected static int HAN_ZI_SIZE = 25;
    protected static int HAN_ZI_SIZE_HALF;
    protected static String REDIS_CAPTCHA_KEY;
    protected static String REDIS_SECOND_CAPTCHA_KEY;
    protected static Long EXPIRESIN_SECONDS;
    protected static Long EXPIRESIN_THREE;
    protected static String waterMark;
    protected static String waterMarkFontStr;
    protected Font waterMarkFont;
    protected static String slipOffset;
    protected static Boolean captchaAesStatus;
    protected static String clickWordFontStr;
    protected Font clickWordFont;
    protected static String cacheType;
    protected static int captchaInterferenceOptions;
    private static FrequencyLimitHandler limitHandler;

    public AbstractCaptchaService() {
    }

    @PostConstruct
    public void init() {
        this.logger.info("--->>>初始化验证码底图<<<---" + this.captchaType());
        waterMark = "";
        slipOffset = "5";
        waterMarkFontStr = "WenQuanZhengHei.ttf";
        captchaAesStatus = true;
        clickWordFontStr = "WenQuanZhengHei.ttf";
        cacheType = "redis";
        captchaInterferenceOptions = 0;
        this.loadWaterMarkFont();
        if (cacheType.equals("local")) {
            this.logger.info("初始化local缓存...");
            CacheUtil.init(1000,180L);
        }
        ImageUtils.cacheImage(null,null);

//        if (config.getProperty("captcha.history.data.clear.enable", "0").equals("1")) {
//            this.logger.info("历史资源清除开关...开启..." + this.captchaType());
//            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//                public void run() {
//                    AbstractCaptchaService.this.destroy(config);
//                }
//            }));
//        }
//
//        if (config.getProperty("captcha.req.frequency.limit.enable", "0").equals("1") && limitHandler == null) {
//            this.logger.info("接口分钟内限流开关...开启...");
//            limitHandler = new FrequencyLimitHandler.DefaultLimitHandler(config, this.getCacheService(cacheType));
//        }

    }

    protected CaptchaCacheService getCacheService(String cacheType) {
        return CaptchaServiceFactory.getCache(cacheType);
    }

    public void destroy(Properties config) {
    }

    public ResponseModel get(CaptchaVO captchaVO) {
        if (limitHandler != null) {
            captchaVO.setClientUid(this.getValidateClientId(captchaVO));
            return limitHandler.validateGet(captchaVO);
        } else {
            return null;
        }
    }

    public ResponseModel check(CaptchaVO captchaVO) {
        if (limitHandler != null) {
            captchaVO.setClientUid(this.getValidateClientId(captchaVO));
            return limitHandler.validateCheck(captchaVO);
        } else {
            return null;
        }
    }

    public ResponseModel verification(CaptchaVO captchaVO) {
        if (captchaVO == null) {
            return RepCodeEnum.NULL_ERROR.parseError(new Object[]{"captchaVO"});
        } else if (StringUtils.isEmpty(captchaVO.getCaptchaVerification())) {
            return RepCodeEnum.NULL_ERROR.parseError(new Object[]{"captchaVerification"});
        } else {
            return limitHandler != null ? limitHandler.validateVerify(captchaVO) : null;
        }
    }

    protected boolean validatedReq(ResponseModel resp) {
        return resp == null || resp.isSuccess();
    }

    protected String getValidateClientId(CaptchaVO req) {
        if (StringUtils.isNotEmpty(req.getBrowserInfo())) {
            return MD5Util.md5(req.getBrowserInfo());
        } else {
            return StringUtils.isNotEmpty(req.getClientUid()) ? req.getClientUid() : null;
        }
    }

    protected void afterValidateFail(CaptchaVO data) {
        if (limitHandler != null) {
            String fails = String.format("AJ.CAPTCHA.REQ.LIMIT-%s-%s", "FAIL", data.getClientUid());
            CaptchaCacheService cs = this.getCacheService(cacheType);
            if (!cs.exists(fails)) {
                cs.set(fails, "1", 60L);
            }

            cs.increment(fails, 1L);
        }

    }

    private void loadWaterMarkFont() {
        try {
            if (!waterMarkFontStr.toLowerCase().endsWith(".ttf") && !waterMarkFontStr.toLowerCase().endsWith(".ttc") && !waterMarkFontStr.toLowerCase().endsWith(".otf")) {
                this.waterMarkFont = new Font(waterMarkFontStr, 1, HAN_ZI_SIZE / 2);
            } else {
                this.waterMarkFont = Font.createFont(0, this.getClass().getResourceAsStream("/fonts/" + waterMarkFontStr)).deriveFont(1, (float)(HAN_ZI_SIZE / 2));
            }
        } catch (Exception var2) {
            Exception e = var2;
            this.logger.error("load font error:{}", e);
        }

    }

    public static boolean base64StrToImage(String imgStr, String path) {
        if (imgStr == null) {
            return false;
        } else {
            Base64.Decoder decoder = Base64.getDecoder();

            try {
                byte[] b = decoder.decode(imgStr);

                for(int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {
                        b[i] = (byte)(b[i] + 256);
                    }
                }

                File tempFile = new File(path);
                if (!tempFile.getParentFile().exists()) {
                    tempFile.getParentFile().mkdirs();
                }

                OutputStream out = new FileOutputStream(tempFile);
                out.write(b);
                out.flush();
                out.close();
                return true;
            } catch (Exception var6) {
                return false;
            }
        }
    }

    public static String decrypt(String point, String key) throws Exception {
        return AESUtil.aesDecrypt(point, key);
    }

    protected static int getEnOrChLength(String s) {
        int enCount = 0;
        int chCount = 0;

        int chOffset;
        int length;
        for(chOffset = 0; chOffset < s.length(); ++chOffset) {
            length = String.valueOf(s.charAt(chOffset)).getBytes(StandardCharsets.UTF_8).length;
            if (length > 1) {
                ++chCount;
            } else {
                ++enCount;
            }
        }

        chOffset = HAN_ZI_SIZE / 2 * chCount + 5;
        length = enCount * 8;
        return chOffset + length;
    }

    static {
        HAN_ZI_SIZE_HALF = HAN_ZI_SIZE / 2;
        REDIS_CAPTCHA_KEY = "RUNNING:CAPTCHA:%s";
        REDIS_SECOND_CAPTCHA_KEY = "RUNNING:CAPTCHA:second-%s";
        EXPIRESIN_SECONDS = 120L;
        EXPIRESIN_THREE = 180L;
        waterMark = "我的水印";
        waterMarkFontStr = "WenQuanZhengHei.ttf";
        slipOffset = "5";
        captchaAesStatus = true;
        clickWordFontStr = "WenQuanZhengHei.ttf";
        cacheType = "local";
        captchaInterferenceOptions = 0;
    }
}
