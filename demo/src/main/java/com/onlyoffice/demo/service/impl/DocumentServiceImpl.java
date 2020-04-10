package com.onlyoffice.demo.service.impl;


import com.onlyoffice.demo.config.PropertiesConfig;
import com.onlyoffice.demo.constant.DocumentConstants;
import com.onlyoffice.demo.enumeration.DocumentEnum;
import com.onlyoffice.demo.enumeration.ErrorCodeEnum;
import com.onlyoffice.demo.entity.*;
import com.onlyoffice.demo.exception.DocumentException;
import com.onlyoffice.demo.service.IDocumentCacheService;
import com.onlyoffice.demo.service.IDocumentService;
import com.onlyoffice.demo.util.IdUtils;
import com.onlyoffice.demo.util.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 文档相关业务
 *
 * @author : zhangLei
 * @serial : 2020-04-08
 */
@Slf4j
@Service
public class DocumentServiceImpl implements IDocumentService {

    private final PropertiesConfig propertiesConfig;
    private final IDocumentCacheService cacheService;

    @Autowired
    public DocumentServiceImpl(PropertiesConfig propertiesConfig, IDocumentCacheService cacheService) {
        this.propertiesConfig = propertiesConfig;
        this.cacheService = cacheService;
    }

    @Override
    public String buildDocument(String filePath, String fileName) {
        if (StringUtils.isBlank(filePath)) {
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_NOT_EXISTS);
        }
        //获取当前系统格式化路径
        filePath = FilenameUtils.normalize(filePath);
        //获取文件的后缀
        String fileType = StringUtils.lowerCase(FilenameUtils.getExtension(filePath));
        if (StringUtils.isBlank(fileType)) {
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_NO_EXTENSION);
        }
        // 如果指定了文件名，则需要校验和实体文件格式是否一致
        if (StringUtils.isNotBlank(fileName) && !fileType.equalsIgnoreCase(FilenameUtils.getExtension(fileName))) {
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_EXTENSION_NOT_MATCH);
        }
        File docFile = new File(filePath);
        // 校验文件实体
        preFileCheck(docFile);
        fileName = StringUtils.isNotBlank(fileName) ? fileName : docFile.getName();
        String fileKey = this.fileKey(docFile, fileName);
        log.info("============> 本次获取到的文件[{}]缓存中的Key:{}", fileName, fileKey);
        Document document = Document.builder()
                .fileType(fileType)
                .title(fileName)
                .storage(filePath)
                .build();
        boolean cached = false;
        try {
            cached = cacheService.put(fileKey, document);
        } catch (Exception e) {
            log.error("============> 缓存失败", e);
        }
        if (!cached) {
            throw new DocumentException(ErrorCodeEnum.DOC_CACHE_ERROR);
        }
        return fileKey;
    }

    @Override
    public Document getDocument(String documentKey) {
        Document doc = null;
        try {
            doc = cacheService.get(documentKey);
        } catch (Exception e) {
            log.error("============> 获取文件缓存数据失败", e);
        }
        if (doc == null) {
            throw new DocumentException(ErrorCodeEnum.DOC_CACHE_NOT_EXISTS);
        }
        // 从缓存中取出后，再绑定非必需缓存字段（节省缓存大小）
        doc.setKey(documentKey);
        doc.setUrl(fileUrl(documentKey));
        if (log.isDebugEnabled()) {
            log.info(doc.toString());
        }
        return doc;
    }


    /**
     * 计算文件key值: 文件md5值+路径的短md5值+名称的短md5值
     *
     * @param docFile 文档文件
     * @param name    文档名称
     * @return 计算后的Key值
     */
    private String fileKey(File docFile, String name) {
        String docFileMd5 = Md5Utils.getFileMd5(docFile);
        if (StringUtils.isBlank(docFileMd5)) {
            log.error("============> 构建文件信息失败！计算文件 md5 失败！");
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_MD5_ERROR);
        }
        String pathShortMd5 = Md5Utils.md5(docFile.getAbsolutePath());
        String nameShortMd5 = Md5Utils.md5(name);
        Hashids hashids = new Hashids(DocumentConstants.HASH_KEY);
        // (将文件md5值 + 路径字符串短md5值 + 名称字符串短md5值) ==> 再转成短id形式 ==> 作为文档的key（暂且认为是不会重复的）
        String key = hashids.encodeHex(String.format("%s%s%s", docFileMd5, pathShortMd5, nameShortMd5));
        if (StringUtils.isBlank(key)) {
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_KEY_ERROR);
        }
        return key;
    }

    /**
     * 获取文件url地址
     *
     * @param fileKey 文件的Key
     * @return url
     */
    private String fileUrl(String fileKey) {
        return String.format(DocumentConstants.OFFICE_API_DOC_FILE, getServerHost(), fileKey);
    }

    @Override
    public void downloadDocumentFile(String documentKey, HttpServletRequest request, HttpServletResponse response) {
        Document doc = this.getDocument(documentKey);
        File file = new File(doc.getStorage());
        try (InputStream reader = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            byte[] buf = new byte[(int) FileUtils.ONE_KB];
            int len = 0;
            //response.setContentType(mimeType(file));
            while ((len = reader.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            log.error("============> 下载失败！读取文件[" + doc.getStorage() + "]报错", e);
        }
    }


    @Override
    public DocumentEditParam buildDocumentEditParam(String userId, String userName) {
        return DocumentEditParam.builder()
                .callbackUrl(callbackUrl())
                .user(DocumentEditParam.UserBean.builder()
                        .id(userId)
                        .name(userName)
                        .build())
                .build();
    }

    /**
     * 获取文档回调地址
     *
     * @return 回调地址
     */
    private String callbackUrl() {
        return String.format(DocumentConstants.OFFICE_API_CALLBACK, getServerHost());
    }


    @Override
    public boolean saveDocumentFile(String documentKey, String downloadUrl) {
        log.info("============> 文档服务器中文件地址:" + downloadUrl);
        Document doc = this.getDocument(documentKey);
        String saveFilePath = doc.getStorage();
        File saveFile = new File(saveFilePath);
        boolean success = false;
        try {
            FileUtils.copyURLToFile(new URL(downloadUrl), saveFile);
            if (saveFile.exists() && saveFile.length() > 0) {
                success = true;
            }
        } catch (IOException e) {
            log.error("============> 保存文档失败!", e);
        }
        return success;
    }

    @Override
    public Object getServerHost() {
        if (StringUtils.startsWith(propertiesConfig.getDocumentServerHost(), DocumentConstants.HTTP_SCHEME)) {
            return propertiesConfig.getDocumentServerHost();
        }
        return String.format("http://%s", propertiesConfig.getDocumentServerHost());
    }

    @Override
    public boolean canEdit(Document document) {
        if (ArrayUtils.contains(DocumentConstants.FILE_TYPE_UNSUPPORT_EDIT, document.getFileType())) {
            return false;
        }
        return true;
    }

    @Override
    public DocumentResponse beforeView(String documentPath, String documentTitle) {
        String documentKey = buildDocument(documentPath, documentTitle);
        Map<String, String> data = new HashMap<>(16);
        data.put("url", String.format(DocumentConstants.OFFICE_VIEWER, getServerHost(), documentKey));
        return DocumentResponse.success(data);
    }

    @Override
    public DocumentResponse beforeEdit(String documentPath, String documentTitle) {
        String documentKey = buildDocument(documentPath, documentTitle);
        Map<String, String> data = new HashMap<>(16);
        //由于PDF格式的文件不支持编辑,所以在这里进行阻止
        if (!canEdit(getDocument(documentKey))) {
            log.error("============> 本次编辑的文件为PDF格式,暂时不支持编辑!");
            return DocumentResponse.failue(ErrorCodeEnum.EDITING_TYPE_IS_NOT_SUPPORTED);
        }
        //TODO 获取用户id与用户名
        String userId = String.valueOf(IdUtils.generateId());
        String userName = "法外狂徒7777...";
        data.put("url", String.format(DocumentConstants.OFFICE_EDITOR, getServerHost(), documentKey, userId, userName));
        return DocumentResponse.success(data);
    }


    @Override
    public DocumentEditCallbackResponse callback(DocumentEditCallback callback) {
        log.info("============> 编辑完毕进入回调接口,回调信息：{}", callback);
        // 需要保存时写出文件
        if (DocumentEnum.READY_FOR_SAVING.getCode() == callback.getStatus() || DocumentEnum.BEING_EDITED_STATE_SAVED.getCode() == callback.getStatus()) {
            log.info("============> 文档已经关闭,开始将数据重新写入文档!");
            boolean success = saveDocumentFile(callback.getKey(), callback.getUrl());
            if (!success) {
                return DocumentEditCallbackResponse.fail();
            }
            log.info("============> 数据写入文档完毕!");
            //由于重新写入了数据,所以需要清除redis中的缓存
            log.info("============> 开始清除Redis中的缓存数据!");
            cacheService.remove(callback.getKey());
            log.info("============> Redis缓存数据清除完毕!");
        }
        return DocumentEditCallbackResponse.success();
    }


    /**
     * 校验文档文件
     *
     * @param docFile 文档文件
     */
    private void preFileCheck(File docFile) {
        if (log.isDebugEnabled()) {
            log.debug("============> 开始校验文档：[{}]", docFile.getAbsolutePath());
        }
        if (docFile == null || !docFile.exists()) {
            log.error("============> 目标文档不存在，无法打开！");
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_NOT_EXISTS);
        }
        if (docFile.isDirectory() || docFile.length() <= 0) {
            log.error("============> 目标文档[{}]是目录或空文件，无法打开！", docFile.getAbsolutePath());
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_EMPTY);
        }
        if (!docFile.canRead()) {
            log.error("============> 目标文档[{}]不可读，无法打开！", docFile.getAbsolutePath());
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_UNREADABLE);
        }
        if (docFile.length() > propertiesConfig.getDocumentFileSizeLimit()) {
            log.error("============> 目标文档大小超过限制({}B > {}B)，无法打开！", docFile.length(), propertiesConfig.getDocumentFileSizeLimit());
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_OVERSIZE);
        }
        String ext = StringUtils.lowerCase(FilenameUtils.getExtension(docFile.getName()));
        if (!ArrayUtils.contains(DocumentConstants.FILE_TYPE_SUPPORT_VIEW, ext)) {
            log.error("============> 目标文档格式[{}]不正确，无法打开！（只支持：{}）",
                    ext, StringUtils.join(DocumentConstants.FILE_TYPE_SUPPORT_VIEW, ","));
            throw new DocumentException(ErrorCodeEnum.DOC_FILE_TYPE_UNSUPPORTED);
        }
    }
}
