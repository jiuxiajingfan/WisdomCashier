package com.li.widomcashier.config;

import com.alipay.api.domain.Contact;
import com.li.wisdomcashier.entry.SwaggerProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName Knife4jConfig
 * @Description TODO
 * @Author Nine
 * @Date 2023/10/19 20:39
 * @Version 1.0
 */

@Configuration
@EnableAutoConfiguration
@EnableOpenApi
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = false)
@EnableConfigurationProperties(SwaggerProperties.class)
public class Knife4jConfig {
    private SwaggerProperties swaggerProperties;

    public Knife4jConfig(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    public Docket api() {
        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2)
                .host(swaggerProperties.getHost())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any());
        return builder.build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .contact(new Contact(swaggerProperties.getName(), swaggerProperties.getUrl(), swaggerProperties.getEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }
}
