package com.onlyoffice.demo.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Swagger 配置
 * 使用 Swagger 生成 Restful API 文档
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.onlyoffice.demo.web"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .produces(contentType())
                .consumes(contentType())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, globalResponseMessage())
                .globalResponseMessage(RequestMethod.POST, globalResponseMessage());
    }

    private Set<String> contentType() {
        return Sets.newHashSet(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }


    private List<ResponseMessage> globalResponseMessage() {
        return Lists.newArrayList(
                new ResponseMessageBuilder()
                        .code(200)
                        .message("请求成功")
                        .build()
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "文档在预览和编辑 API 文档",
                "使用OnlyOffice document server 来实现文档的在预览和编辑",
                "1.0.0",
                null,
                null,
                null,
                null,
                Collections.emptyList());
    }
}