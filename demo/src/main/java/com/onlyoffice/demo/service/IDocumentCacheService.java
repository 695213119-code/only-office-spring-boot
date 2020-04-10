package com.onlyoffice.demo.service;


import com.onlyoffice.demo.entity.Document;

/**
 * 文件缓存接口
 *
 * @author : zhangLei
 * @serial : 2020-04-08
 */
public interface IDocumentCacheService {

    /**
     * 将文档信息放入缓存
     *
     * @param documentKey 文档Key
     * @param doc         文档对象
     * @return boolean
     */
    boolean put(String documentKey, Document doc);

    /**
     * 根据 documentKey 从缓存中获取文档
     *
     * @param documentKey 文档Key
     * @return 文档对象
     */
    Document get(String documentKey);

    /**
     * 删除缓存
     *
     * @param documentKey 文档Key
     */
    void remove(String documentKey);
}
