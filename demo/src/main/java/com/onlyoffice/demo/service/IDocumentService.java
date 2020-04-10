package com.onlyoffice.demo.service;

import com.onlyoffice.demo.entity.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文档业务 接口
 *
 * @author : zhangLei
 * @serial : 2020-04-09
 */
public interface IDocumentService {
    /**
     * 构建文档对象,并获取到路径的加密Key
     *
     * @param filePath 文档路径
     * @param fileName 文档标题
     * @return documentKey 文档key
     */
    String buildDocument(String filePath, String fileName);

    /**
     * 从缓从中获取文档信息
     *
     * @param documentKey 文档的加密Key
     * @return 文档对象
     */
    Document getDocument(String documentKey);

    /**
     * 下载文档实体文件
     *
     * @param documentKey 文档的加密Key
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     */
    void downloadDocumentFile(String documentKey, HttpServletRequest request, HttpServletResponse response);

    /**
     * 构建文档编辑参数 对象
     *
     * @param userId   用户id
     * @param userName 用户名
     * @return DocumentEditParam
     */
    DocumentEditParam buildDocumentEditParam(String userId, String userName);

    /**
     * 编辑后保存文档实体文件
     *
     * @param documentKey 文档的加密Key
     * @param downloadUrl 文档的url
     * @throws IOException
     */
    boolean saveDocumentFile(String documentKey, String downloadUrl) throws IOException;

    /**
     * 获取服务暴露的host(包含端口)
     *
     * @return 访问地址的host
     */
    Object getServerHost();

    /**
     * 文档是否支持编辑
     *
     * @param document 文档对象
     * @return boolean
     */
    boolean canEdit(Document document);

    /**
     * 文档预览
     *
     * @param documentPath  文档文件路径
     * @param documentTitle 文档标题
     * @return 文档加密地址
     */
    DocumentResponse beforeView(String documentPath, String documentTitle);

    /**
     * 文档编辑
     *
     * @param documentPath  文档文件路径
     * @param documentTitle 文档标题
     * @return 文档加密地址
     */
    DocumentResponse beforeEdit(String documentPath, String documentTitle);

    /**
     * 文件回调(保存文档实体)
     *
     * @param callback 回调的实体类
     * @return DocumentEditCallbackResponse
     */
    DocumentEditCallbackResponse callback(DocumentEditCallback callback);
}
