//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.li.wisdomcashier.service.impl;

import com.li.wisdomcashier.entry.ResponseModel;
import com.li.wisdomcashier.entry.vo.CaptchaVO;
import com.li.wisdomcashier.entry.vo.PointVO;
import com.li.wisdomcashier.enums.CaptchaTypeEnum;
import com.li.wisdomcashier.enums.RepCodeEnum;
import com.li.wisdomcashier.utils.AESUtil;
import com.li.wisdomcashier.utils.ImageUtils;
import com.li.wisdomcashier.utils.JsonUtil;
import com.li.wisdomcashier.utils.RandomUtils;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import javax.imageio.ImageIO;

@Service
public class BlockPuzzleCaptchaServiceImpl extends AbstractCaptchaService {
    public BlockPuzzleCaptchaServiceImpl() {
    }

    public void init() {
        super.init();
    }

    public void destroy(Properties config) {
        this.logger.info("start-clear-history-data-", this.captchaType());
    }

    public String captchaType() {
        return CaptchaTypeEnum.BLOCKPUZZLE.getCodeValue();
    }

    public ResponseModel get(CaptchaVO captchaVO) {
        ResponseModel r = super.get(captchaVO);
        if (!this.validatedReq(r)) {
            return r;
        } else {
            BufferedImage originalImage = ImageUtils.getOriginal();
            if (null == originalImage) {
                this.logger.error("滑动底图未初始化成功，请检查路径");
                return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_BASEMAP_NULL);
            } else {
                Graphics backgroundGraphics = originalImage.getGraphics();
                int width = originalImage.getWidth();
                int height = originalImage.getHeight();
                backgroundGraphics.setFont(this.waterMarkFont);
                backgroundGraphics.setColor(Color.white);
                backgroundGraphics.drawString(waterMark, width - getEnOrChLength(waterMark), height - HAN_ZI_SIZE / 2 + 7);
                String jigsawImageBase64 = ImageUtils.getslidingBlock();
                BufferedImage jigsawImage = ImageUtils.getBase64StrToImage(jigsawImageBase64);
                if (null == jigsawImage) {
                    this.logger.error("滑动底图未初始化成功，请检查路径");
                    return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_BASEMAP_NULL);
                } else {
                    CaptchaVO captcha = this.pictureTemplatesCut(originalImage, jigsawImage, jigsawImageBase64);
                    return captcha != null && !StringUtils.isBlank(captcha.getJigsawImageBase64()) && !StringUtils.isBlank(captcha.getOriginalImageBase64()) ? ResponseModel.successData(captcha) : ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_ERROR);
                }
            }
        }
    }

    public ResponseModel check(CaptchaVO captchaVO) {
        ResponseModel r = super.check(captchaVO);
        if (!this.validatedReq(r)) {
            return r;
        } else {
            String codeKey = String.format(REDIS_CAPTCHA_KEY, captchaVO.getToken());
            if (!CaptchaServiceFactory.getCache(cacheType).exists(codeKey)) {
                return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_INVALID);
            } else {
                String s = CaptchaServiceFactory.getCache(cacheType).get(codeKey);
                CaptchaServiceFactory.getCache(cacheType).delete(codeKey);
                PointVO point = null;
                PointVO point1 = null;
                String pointJson = null;

                try {
                    point = JsonUtil.parseObject(s, PointVO.class);
                    pointJson = decrypt(captchaVO.getPointJson(), point.getSecretKey());
                    point1 = JsonUtil.parseObject(pointJson, PointVO.class);
                } catch (Exception var12) {
                    Exception e = var12;
                    this.logger.error("验证码坐标解析失败", e);
                    this.afterValidateFail(captchaVO);
                    return ResponseModel.errorMsg(e.getMessage());
                }

                if (point.x - Integer.parseInt(slipOffset) <= point1.x && point1.x <= point.x + Integer.parseInt(slipOffset) && point.y == point1.y) {
                    String secretKey = point.getSecretKey();
                    String value = null;

                    try {
                        value = AESUtil.aesEncrypt(captchaVO.getToken().concat("---").concat(pointJson), secretKey);
                    } catch (Exception var11) {
                        Exception e = var11;
                        this.logger.error("AES加密失败", e);
                        this.afterValidateFail(captchaVO);
                        return ResponseModel.errorMsg(e.getMessage());
                    }

                    String secondKey = String.format(REDIS_SECOND_CAPTCHA_KEY, value);
                    CaptchaServiceFactory.getCache(cacheType).set(secondKey, captchaVO.getToken(), EXPIRESIN_THREE);
                    captchaVO.setResult(true);
                    captchaVO.resetClientFlag();
                    return ResponseModel.successData(captchaVO);
                } else {
                    this.afterValidateFail(captchaVO);
                    return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR);
                }
            }
        }
    }

    public ResponseModel verification(CaptchaVO captchaVO) {
        ResponseModel r = super.verification(captchaVO);
        if (!this.validatedReq(r)) {
            return r;
        } else {
            try {
                String codeKey = String.format(REDIS_SECOND_CAPTCHA_KEY, captchaVO.getCaptchaVerification());
                if (!CaptchaServiceFactory.getCache(cacheType).exists(codeKey)) {
                    return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_INVALID);
                }

                CaptchaServiceFactory.getCache(cacheType).delete(codeKey);
            } catch (Exception var4) {
                Exception e = var4;
                this.logger.error("验证码坐标解析失败", e);
                return ResponseModel.errorMsg(e.getMessage());
            }

            return ResponseModel.success();
        }
    }

    public CaptchaVO pictureTemplatesCut(BufferedImage originalImage, BufferedImage jigsawImage, String jigsawImageBase64) {
        try {
            CaptchaVO dataVO = new CaptchaVO();
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            int jigsawWidth = jigsawImage.getWidth();
            int jigsawHeight = jigsawImage.getHeight();
            PointVO point = generateJigsawPoint(originalWidth, originalHeight, jigsawWidth, jigsawHeight);
            int x = point.getX();
            int y = point.getY();
            BufferedImage newJigsawImage = new BufferedImage(jigsawWidth, jigsawHeight, jigsawImage.getType());
            Graphics2D graphics = newJigsawImage.createGraphics();
            int bold = 5;
            newJigsawImage = graphics.getDeviceConfiguration().createCompatibleImage(jigsawWidth, jigsawHeight, 3);
            cutByTemplate(originalImage, jigsawImage, newJigsawImage, x, 0);
            if (captchaInterferenceOptions > 0) {
                int position;
                if (originalWidth - x - 5 > jigsawWidth * 2) {
                    position = RandomUtils.getRandomInt(x + jigsawWidth + 5, originalWidth - jigsawWidth);
                } else {
                    position = RandomUtils.getRandomInt(100, x - jigsawWidth - 5);
                }

                String s;
                do {
                    s = ImageUtils.getslidingBlock();
                } while(jigsawImageBase64.equals(s));

                interferenceByTemplate(originalImage, (BufferedImage)Objects.requireNonNull(ImageUtils.getBase64StrToImage(s)), position, 0);
            }

            if (captchaInterferenceOptions > 1) {
                String s;
                do {
                    s = ImageUtils.getslidingBlock();
                } while(jigsawImageBase64.equals(s));

                Integer randomInt = RandomUtils.getRandomInt(jigsawWidth, 100 - jigsawWidth);
                interferenceByTemplate(originalImage, (BufferedImage)Objects.requireNonNull(ImageUtils.getBase64StrToImage(s)), randomInt, 0);
            }

            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setStroke(new BasicStroke((float)bold, 0, 2));
            graphics.drawImage(newJigsawImage, 0, 0, (ImageObserver)null);
            graphics.dispose();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(newJigsawImage, "png", os);
            byte[] jigsawImages = os.toByteArray();
            ByteArrayOutputStream oriImagesOs = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", oriImagesOs);
            byte[] oriCopyImages = oriImagesOs.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            dataVO.setOriginalImageBase64(encoder.encodeToString(oriCopyImages).replaceAll("\r|\n", ""));
            dataVO.setJigsawImageBase64(encoder.encodeToString(jigsawImages).replaceAll("\r|\n", ""));
            dataVO.setToken(RandomUtils.getUUID());
            dataVO.setSecretKey(point.getSecretKey());
            String codeKey = String.format(REDIS_CAPTCHA_KEY, dataVO.getToken());
            CaptchaServiceFactory.getCache(cacheType).set(codeKey, JsonUtil.toJSONString(point), EXPIRESIN_SECONDS);
            this.logger.debug("token：{},point:{}", dataVO.getToken(), JsonUtil.toJSONString(point));
            return dataVO;
        } catch (Exception var21) {
            Exception e = var21;
            e.printStackTrace();
            return null;
        }
    }

    private static PointVO generateJigsawPoint(int originalWidth, int originalHeight, int jigsawWidth, int jigsawHeight) {
        Random random = new Random();
        int widthDifference = originalWidth - jigsawWidth;
        int heightDifference = originalHeight - jigsawHeight;
        int x;
        if (widthDifference <= 0) {
            x = 5;
        } else {
            x = random.nextInt(originalWidth - jigsawWidth - 100) + 100;
        }

        int y;
        if (heightDifference <= 0) {
            y = 5;
        } else {
            y = random.nextInt(originalHeight - jigsawHeight) + 5;
        }

        String key = null;
        if (captchaAesStatus) {
            key = AESUtil.getKey();
        }

        return new PointVO(x, y, key);
    }

    private static void cutByTemplate(BufferedImage oriImage, BufferedImage templateImage, BufferedImage newImage, int x, int y) {
        int[][] martrix = new int[3][3];
        int[] values = new int[9];
        int xLength = templateImage.getWidth();
        int yLength = templateImage.getHeight();

        for(int i = 0; i < xLength; ++i) {
            for(int j = 0; j < yLength; ++j) {
                int rgb = templateImage.getRGB(i, j);
                if (rgb < 0) {
                    newImage.setRGB(i, j, oriImage.getRGB(x + i, y + j));
                    readPixel(oriImage, x + i, y + j, values);
                    fillMatrix(martrix, values);
                    oriImage.setRGB(x + i, y + j, avgMatrix(martrix));
                }

                if (i != xLength - 1 && j != yLength - 1) {
                    int rightRgb = templateImage.getRGB(i + 1, j);
                    int downRgb = templateImage.getRGB(i, j + 1);
                    if (rgb >= 0 && rightRgb < 0 || rgb < 0 && rightRgb >= 0 || rgb >= 0 && downRgb < 0 || rgb < 0 && downRgb >= 0) {
                        newImage.setRGB(i, j, Color.white.getRGB());
                        oriImage.setRGB(x + i, y + j, Color.white.getRGB());
                    }
                }
            }
        }

    }

    private static void interferenceByTemplate(BufferedImage oriImage, BufferedImage templateImage, int x, int y) {
        int[][] martrix = new int[3][3];
        int[] values = new int[9];
        int xLength = templateImage.getWidth();
        int yLength = templateImage.getHeight();

        for(int i = 0; i < xLength; ++i) {
            for(int j = 0; j < yLength; ++j) {
                int rgb = templateImage.getRGB(i, j);
                if (rgb < 0) {
                    readPixel(oriImage, x + i, y + j, values);
                    fillMatrix(martrix, values);
                    oriImage.setRGB(x + i, y + j, avgMatrix(martrix));
                }

                if (i != xLength - 1 && j != yLength - 1) {
                    int rightRgb = templateImage.getRGB(i + 1, j);
                    int downRgb = templateImage.getRGB(i, j + 1);
                    if (rgb >= 0 && rightRgb < 0 || rgb < 0 && rightRgb >= 0 || rgb >= 0 && downRgb < 0 || rgb < 0 && downRgb >= 0) {
                        oriImage.setRGB(x + i, y + j, Color.white.getRGB());
                    }
                }
            }
        }

    }

    private static void readPixel(BufferedImage img, int x, int y, int[] pixels) {
        int xStart = x - 1;
        int yStart = y - 1;
        int current = 0;

        for(int i = xStart; i < 3 + xStart; ++i) {
            for(int j = yStart; j < 3 + yStart; ++j) {
                int tx = i;
                if (tx < 0) {
                    tx = -tx;
                } else if (tx >= img.getWidth()) {
                    tx = x;
                }

                int ty = j;
                if (ty < 0) {
                    ty = -ty;
                } else if (ty >= img.getHeight()) {
                    ty = y;
                }

                pixels[current++] = img.getRGB(tx, ty);
            }
        }

    }

    private static void fillMatrix(int[][] matrix, int[] values) {
        int filled = 0;

        for(int i = 0; i < matrix.length; ++i) {
            int[] x = matrix[i];

            for(int j = 0; j < x.length; ++j) {
                x[j] = values[filled++];
            }
        }

    }

    private static int avgMatrix(int[][] matrix) {
        int r = 0;
        int g = 0;
        int b = 0;

        for(int i = 0; i < matrix.length; ++i) {
            int[] x = matrix[i];

            for(int j = 0; j < x.length; ++j) {
                if (j != 1) {
                    Color c = new Color(x[j]);
                    r += c.getRed();
                    g += c.getGreen();
                    b += c.getBlue();
                }
            }
        }

        return (new Color(r / 8, g / 8, b / 8)).getRGB();
    }
}
