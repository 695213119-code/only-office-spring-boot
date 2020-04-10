package com.onlyoffice.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.onlyoffice.demo.constant.DocumentConstants;
import com.onlyoffice.demo.entity.Document;
import com.onlyoffice.demo.service.IDocumentCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 使用redis作为缓存
 *
 * @author : zhangLei
 * @serial : 2020-04-08
 */
@Slf4j
@Service
public class RedisDocumentCacheServiceImpl implements IDocumentCacheService {

    private final RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public RedisDocumentCacheServiceImpl(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;

    }

    @Override
    public boolean put(String documentKey, Document doc) {
        if (doc == null) {
            return false;
        }
        redisTemplate.opsForValue().set(cacheKey(documentKey), JSON.toJSONString(doc), DocumentConstants.REDIS_DEFAULT_CACHE_EXPIRATION_TIME, TimeUnit.DAYS);
        return true;
    }

    @Override
    public Document get(String documentKey) {
        return JSON.parseObject((String) redisTemplate.opsForValue().get(cacheKey(documentKey)), Document.class);
    }

    @Override
    public void remove(String documentKey) {
        redisTemplate.delete(cacheKey(documentKey));
    }

    private String cacheKey(String documentKey) {
        return String.format(DocumentConstants.DOCUMENT_REDIS_KEY_PREFIX_FORMAT, documentKey);
    }
}
