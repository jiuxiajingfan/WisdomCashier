package com.li.wisdomcashier.customer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @ClassName Knife4gConfig
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/10 15:34
 * @Version 1.0
 */
@Configuration
public class Knife4gConfig {

    @Value("${swagger.enable:false}")
    private Boolean enable;

    @Bean(value = "dockerBean")
    public Docket dockerBean() {
        //指定使用Swagger2规范
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .enable(enable)
                .apiInfo(new ApiInfoBuilder()
                        .title("智慧收银客户端Api文档")
                        //描述字段支持Markdown语法
                        .description("# 智慧收银客户端Api文档")
                        .contact(new Contact("lsw","dianaforever.cn"
                                ,"jiuxiajingfan@163.com"))
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("all")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.li"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}