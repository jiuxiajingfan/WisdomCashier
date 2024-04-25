//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.li.wisdomcashier.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;

import com.li.wisdomcashier.enums.CaptchaBaseMapEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtils {
    private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);
    private static Map<String, String> originalCacheMap = new ConcurrentHashMap();
    private static Map<String, String> slidingBlockCacheMap = new ConcurrentHashMap();
    private static Map<String, String> picClickCacheMap = new ConcurrentHashMap();
    private static Map<String, String[]> fileNameMap = new ConcurrentHashMap();

    public ImageUtils() {
    }

    public static void cacheImage(String captchaOriginalPathJigsaw, String captchaOriginalPathClick) {
        if (StringUtils.isBlank(captchaOriginalPathJigsaw)) {
            originalCacheMap.putAll(getResourcesImagesFile("defaultImages/jigsaw/original"));
            slidingBlockCacheMap.putAll(getResourcesImagesFile("defaultImages/jigsaw/slidingBlock"));
        } else {
            originalCacheMap.putAll(getImagesFile(captchaOriginalPathJigsaw + File.separator + "original"));
            slidingBlockCacheMap.putAll(getImagesFile(captchaOriginalPathJigsaw + File.separator + "slidingBlock"));
        }

        if (StringUtils.isBlank(captchaOriginalPathClick)) {
            picClickCacheMap.putAll(getResourcesImagesFile("defaultImages/pic-click"));
        } else {
            picClickCacheMap.putAll(getImagesFile(captchaOriginalPathClick));
        }

        fileNameMap.put(CaptchaBaseMapEnum.ORIGINAL.getCodeValue(), originalCacheMap.keySet().toArray(new String[0]));
        fileNameMap.put(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue(), slidingBlockCacheMap.keySet().toArray(new String[0]));
        fileNameMap.put(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue(), picClickCacheMap.keySet().toArray(new String[0]));
        logger.info("初始化底图:{}", JsonUtil.toJSONString(fileNameMap));
    }

    public static void cacheBootImage(Map<String, String> originalMap, Map<String, String> slidingBlockMap, Map<String, String> picClickMap) {
        originalCacheMap.putAll(originalMap);
        slidingBlockCacheMap.putAll(slidingBlockMap);
        picClickCacheMap.putAll(picClickMap);
        fileNameMap.put(CaptchaBaseMapEnum.ORIGINAL.getCodeValue(), originalCacheMap.keySet().toArray(new String[0]));
        fileNameMap.put(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue(), slidingBlockCacheMap.keySet().toArray(new String[0]));
        fileNameMap.put(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue(), picClickCacheMap.keySet().toArray(new String[0]));
        logger.info("自定义resource底图:{}", JsonUtil.toJSONString(fileNameMap));
    }

    public static BufferedImage getOriginal() {
        String[] strings = (String[])fileNameMap.get(CaptchaBaseMapEnum.ORIGINAL.getCodeValue());
        if (null != strings && strings.length != 0) {
            Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
            String s = (String)originalCacheMap.get(strings[randomInt]);
            return getBase64StrToImage(s);
        } else {
            return null;
        }
    }

    public static String getslidingBlock() {
        String[] strings = (String[])fileNameMap.get(CaptchaBaseMapEnum.SLIDING_BLOCK.getCodeValue());
        if (null != strings && strings.length != 0) {
            Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
            String s = (String)slidingBlockCacheMap.get(strings[randomInt]);
            return s;
        } else {
            return null;
        }
    }

    public static BufferedImage getPicClick() {
        String[] strings = (String[])fileNameMap.get(CaptchaBaseMapEnum.PIC_CLICK.getCodeValue());
        if (null != strings && strings.length != 0) {
            Integer randomInt = RandomUtils.getRandomInt(0, strings.length);
            String s = (String)picClickCacheMap.get(strings[randomInt]);
            return getBase64StrToImage(s);
        } else {
            return null;
        }
    }

    public static String getImageToBase64Str(BufferedImage templateImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ImageIO.write(templateImage, "png", baos);
        } catch (IOException var4) {
            IOException e = var4;
            e.printStackTrace();
        }

        byte[] bytes = baos.toByteArray();
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(bytes).trim();
    }

    public static BufferedImage getBase64StrToImage(String base64String) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = decoder.decode(base64String);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            return ImageIO.read(inputStream);
        } catch (IOException var4) {
            IOException e = var4;
            e.printStackTrace();
            return null;
        }
    }

    private static Map<String, String> getResourcesImagesFile(String path) {
        Map<String, String> imgMap = new HashMap();
        ClassLoader classLoader = ImageUtils.class.getClassLoader();

        for(int i = 1; i <= 6; ++i) {
            InputStream resourceAsStream = classLoader.getResourceAsStream(path.concat("/").concat(String.valueOf(i).concat(".png")));
            byte[] bytes = new byte[0];

            try {
                bytes = FileCopyUtils.copyToByteArray(resourceAsStream);
            } catch (IOException var8) {
                IOException e = var8;
                e.printStackTrace();
            }

            String string = Base64Utils.encodeToString(bytes);
            String filename = String.valueOf(i).concat(".png");
            imgMap.put(filename, string);
        }

        return imgMap;
    }

    private static Map<String, String> getImagesFile(String path) {
        Map<String, String> imgMap = new HashMap();
        File file = new File(path);
        if (!file.exists()) {
            return new HashMap();
        } else {
            File[] files = file.listFiles();
            Arrays.stream(files).forEach((item) -> {
                try {
                    FileInputStream fileInputStream = new FileInputStream(item);
                    byte[] bytes = FileCopyUtils.copyToByteArray(fileInputStream);
                    String string = Base64Utils.encodeToString(bytes);
                    imgMap.put(item.getName(), string);
                } catch (FileNotFoundException var5) {
                    FileNotFoundException e = var5;
                    e.printStackTrace();
                } catch (IOException var6) {
                    IOException ex = var6;
                    ex.printStackTrace();
                }

            });
            return imgMap;
        }
    }
}
