package com.onlyoffice.demo.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.commons.codec.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文档 MVC 配置 application.properties
 *
 * @author : zhangLei
 * @serial : 2020-04-10
 */
@Configuration
public class DocumentApiMvcConfiguration implements WebMvcConfigurer {

    private final PropertiesConfig propertiesConfig;

    @Resource(name = "thymeleafViewResolver")
    private ThymeleafViewResolver thymeleafViewResolver;


    @Autowired
    public DocumentApiMvcConfiguration(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }


    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        if (thymeleafViewResolver != null) {
            Map<String, Object> vars = new HashMap<>(8);
            vars.put("documentServerApiJs", String.format(propertiesConfig.getOnlyOfficeDocumentServerApiJs(), propertiesConfig.getOnlyOfficeDocumentServerHost()));
            // 静态参数，只取一次值
            thymeleafViewResolver.setStaticVariables(vars);
        }
    }

    /**
     * 替换springboot默认的json转换器为 fastJson
     *
     * @param converters converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setCharset(Charsets.UTF_8);
        converter.setFastJsonConfig(config);
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        converter.setSupportedMediaTypes(list);
        for (int i = 0; i < converters.size(); i++) {
            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                converters.set(i, converter);
            }
        }
    }
}
