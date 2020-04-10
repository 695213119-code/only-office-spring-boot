package com.onlyoffice.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 文档响应实体类
 * 成功状态 响应码必须是0,不然onlyOffice会提示文档无法下载
 *
 * @author : zhangLei
 * @serial : 2020-04-09
 */
@Data
@AllArgsConstructor
public class DocumentEditCallbackResponse implements Serializable {

    private Integer error;

    public static DocumentEditCallbackResponse success() {
        return new DocumentEditCallbackResponse(0);
    }

    public static DocumentEditCallbackResponse fail() {
        return new DocumentEditCallbackResponse(1);
    }
}
