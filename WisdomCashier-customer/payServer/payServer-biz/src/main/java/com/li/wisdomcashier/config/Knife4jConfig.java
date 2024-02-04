package com.li.wisdomcashier.config;

import cn.hutool.core.util.RandomUtil;
import com.li.wisdomcashier.entry.SwaggerProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Knife4jConfig
 * @Description TODO
 * @Author Nine
 * @Date 2023/10/19 20:39
 * @Version 1.0
 */

@Configuration
@EnableAutoConfiguration
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerProperties.class)
public class Knife4jConfig {
    private SwaggerProperties swaggerProperties;

    public Knife4jConfig(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    /**
     * 根据@Tag 上的排序，写入x-order
     *
     * @return the global open api customizer
     */
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getTags() != null) {
                openApi.getTags().forEach(tag -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("x-order", RandomUtil.randomInt(0, 100));
                    tag.setExtensions(map);
                });
            }
            if (openApi.getPaths() != null) {
                openApi.addExtension("x-test123", "333");
                openApi.getPaths().addExtension("x-abb", RandomUtil.randomInt(1, 100));
            }

        };
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(swaggerProperties.getTitle())
                        .version(swaggerProperties.getVersion())
                        .description(swaggerProperties.getDescription())
                        .license(new License().name("Apache 2.0")
                                .url(swaggerProperties.getUrl())
                        )
                );
    }


}
