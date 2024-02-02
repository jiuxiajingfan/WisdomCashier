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
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
//import springfox.documentation.spring.web.plugins.Docket;

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
//@EnableAutoConfiguration
//@EnableOpenApi
//@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
//@EnableConfigurationProperties(SwaggerProperties.class)
public class Knife4jConfig {
//    private SwaggerProperties swaggerProperties;
//
//    public Knife4jConfig(SwaggerProperties swaggerProperties) {
//        this.swaggerProperties = swaggerProperties;
//    }
//
//    @Bean
//    public Docket api() {
//        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2)
//                .host(swaggerProperties.getHost())
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
//                .paths(PathSelectors.any());
//        return builder.build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title(swaggerProperties.getTitle())
//                .description(swaggerProperties.getDescription())
//                .contact(new Contact(swaggerProperties.getName(), swaggerProperties.getUrl(), swaggerProperties.getEmail()))
//                .version(swaggerProperties.getVersion())
//                .build();
//    }


    /**
     * 根据@Tag 上的排序，写入x-order
     *
     * @return the global open api customizer
     */
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getTags()!=null){
                openApi.getTags().forEach(tag -> {
                    Map<String,Object> map=new HashMap<>();
                    map.put("x-order", RandomUtil.randomInt(0,100));
                    tag.setExtensions(map);
                });
            }
            if(openApi.getPaths()!=null){
                openApi.addExtension("x-test123","333");
                openApi.getPaths().addExtension("x-abb",RandomUtil.randomInt(1,100));
            }

        };
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("XXX用户系统API")
                        .version("1.0")

                        .description( "Knife4j集成springdoc-openapi示例")
                        .termsOfService("http://doc.xiaominfo.com")
                        .license(new License().name("Apache 2.0")
                                .url("http://doc.xiaominfo.com")));
    }


}
