package com.onlyoffice.demo.service;

import org.springframework.ui.Model;

/**
 * 文档操作接口
 *
 * @author : zhangLei
 * @serial : 2020-04-09 0009
 */
public interface IDocumentModelViewService {

    /**
     * 返回预览页面
     *
     * @param documentKey 文件的加密Key
     * @param model       ModelView
     * @return 预览页面
     */
    String view(String documentKey, Model model);

    /**
     * 返回编辑器页面
     *
     * @param documentKey 文件的加密Key
     * @param userId      用户id
     * @param userName    用户名
     * @param model       ModelView
     * @return 编辑页面
     */
    String edit(String documentKey, String userId, String userName, Model model);
}
