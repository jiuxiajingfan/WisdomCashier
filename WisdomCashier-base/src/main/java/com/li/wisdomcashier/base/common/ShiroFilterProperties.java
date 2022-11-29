package com.li.wisdomcashier.base.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ShiroFilterProperties
 * @Description TODO
 * @Author Nine
 * @Date 2022/11/29 10:50
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "permission-config")
public class ShiroFilterProperties {
    List<Map<String, String>> perms;
}