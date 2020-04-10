package com.onlyoffice.demo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 读取配置文件中的参数
 *
 * @author : zhangLei
 * @serial : 2020-04-10 0010
 */
@Data
@Component
public class PropertiesConfig {

    /**
     * 对外暴露的服务地址，包含协议头、端口、文根(如果存在)
     */
    @Value("${document.server.host}")
    private String documentServerHost;

    /**
     * onlyoffice文档服务Host,需带协议头和端口(如果有)
     */
    @Value("${onlyoffice.document-server.api.host}")
    private String onlyOfficeDocumentServerHost;

    /**
     * onlyoffice文档服务提供的js api 地址
     */
    @Value("${onlyoffice.document-server.api.js}")
    private String onlyOfficeDocumentServerApiJs;

    /**
     * 文档大小限制(单位：B)，超过就不再支持预览和编辑（默认10M）
     */
    @Value("${document.file-size.limit:10485760}")
    private Long documentFileSizeLimit;


}
