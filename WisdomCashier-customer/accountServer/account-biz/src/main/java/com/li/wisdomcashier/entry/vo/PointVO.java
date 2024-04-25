//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.li.wisdomcashier.entry.vo;

import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class PointVO {
    private String secretKey;
    public int x;
    public int y;

    public String getSecretKey() {
        return this.secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public PointVO(int x, int y, String secretKey) {
        this.secretKey = secretKey;
        this.x = x;
        this.y = y;
    }

    public PointVO() {
    }

    public PointVO(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toJsonString() {
        return String.format("{\"secretKey\":\"%s\",\"x\":%d,\"y\":%d}", this.secretKey, this.x, this.y);
    }

    public PointVO parse(String jsonStr) {
        Map<String, Object> m = new HashMap();
        Arrays.stream(jsonStr.replaceFirst(",\\{", "\\{").replaceFirst("\\{", "").replaceFirst("\\}", "").replaceAll("\"", "").split(",")).forEach((item) -> {
            m.put(item.split(":")[0], item.split(":")[1]);
        });
        this.setX(Double.valueOf("" + m.get("x")).intValue());
        this.setY(Double.valueOf("" + m.get("y")).intValue());
        this.setSecretKey(m.getOrDefault("secretKey", "") + "");
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            PointVO pointVO = (PointVO)o;
            return this.x == pointVO.x && this.y == pointVO.y && Objects.equals(this.secretKey, pointVO.secretKey);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.secretKey, this.x, this.y});
    }
}
