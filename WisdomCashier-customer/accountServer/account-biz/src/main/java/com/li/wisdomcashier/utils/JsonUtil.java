//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.li.wisdomcashier.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.li.wisdomcashier.entry.vo.PointVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public JsonUtil() {
    }

    public static List<PointVO> parseArray(String text, Class<PointVO> clazz) {
        if (text == null) {
            return null;
        } else {
            String[] arr = text.replaceFirst("\\[", "").replaceFirst("\\]", "").split("\\}");
            List<PointVO> ret = new ArrayList(arr.length);
            String[] var4 = arr;
            int var5 = arr.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String s = var4[var6];
                ret.add(parseObject(s, PointVO.class));
            }

            return ret;
        }
    }

    public static PointVO parseObject(String text, Class<PointVO> clazz) {
        if (text == null) {
            return null;
        } else {
            try {
                PointVO ret = (PointVO)clazz.newInstance();
                return ret.parse(text);
            } catch (Exception var3) {
                Exception ex = var3;
                logger.error("json解析异常", ex);
                return null;
            }
        }
    }

    public static String toJSONString(Object object) {
        if (object == null) {
            return "{}";
        } else if (object instanceof PointVO) {
            PointVO t = (PointVO)object;
            return t.toJsonString();
        } else if (object instanceof List) {
            List<PointVO> list = (List)object;
            StringBuilder buf = new StringBuilder("[");
            list.stream().forEach((tx) -> {
                buf.append(tx.toJsonString()).append(",");
            });
            return buf.deleteCharAt(buf.lastIndexOf(",")).append("]").toString();
        } else if (object instanceof Map) {
            return ((Map)object).entrySet().toString();
        } else {
            throw new UnsupportedOperationException("不支持的输入类型:" + object.getClass().getSimpleName());
        }
    }
}
