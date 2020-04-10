package com.onlyoffice.demo.constant;

/**
 * 文档常量类
 *
 * @author : zhangLei
 * @serial : 2020-04-08
 */
public class DocumentConstants {

    private DocumentConstants() {

    }

    public static final String HTTP_SCHEME = "http";

    /**
     * 支持的文档类型
     */
    public static final String[] FILE_TYPE_SUPPORT_VIEW = {"doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "pdf"};

    /**
     * 不支持编辑的类型
     */
    public static final String[] FILE_TYPE_UNSUPPORT_EDIT = {"pdf"};


    //-----------------------------------缓存(redis/Cache)-------------------------------------------------------------------------------

    /**
     * 默认缓存过期时间: 1天 ---redis
     */
    public static final Long REDIS_DEFAULT_CACHE_EXPIRATION_TIME = 1L;

    /**
     * 文档缓存前缀 格式化 ---redis
     */
    public static final String DOCUMENT_REDIS_KEY_PREFIX_FORMAT = "onlyoffice:document:%s";

    //-----------------------------------地址-------------------------------------------------------------------------------

    /**
     * 预览地址
     */
    public static final String OFFICE_VIEWER = "%s/document/model/viewer/%s";

    /**
     * 文档文件下载接口地址
     */
    public static final String OFFICE_API_DOC_FILE = "%s/document/api/file/%s";

    /**
     * 编辑回调地址
     */
    public static final String OFFICE_API_CALLBACK = "%s/document/api/callback";

    /**
     * 编辑地址
     */
    public static final String OFFICE_EDITOR = "%s/document/model/editor/%s?userId=%s&userName=%s";


    //-----------------------------------加密的盐-------------------------------------------------------------------------------

    public static final String HASH_KEY = "daTang";
}
