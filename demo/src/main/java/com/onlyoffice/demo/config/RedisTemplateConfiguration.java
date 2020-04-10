package com.onlyoffice.demo.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 装配 RedisTemplate Bean,替换为fastjson序列化器
 */
@Configuration
@ConditionalOnMissingBean(RedisTemplate.class)
@ConditionalOnClass(FastJsonRedisSerializer.class)
public class RedisTemplateConfiguration {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        // 通常使用 GenericFastJsonRedisSerializer 即可满足大部分场景，序列化时会将对象类型写入@type属性（要求对象必须实现无参构造器）
        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        template.setDefaultSerializer(fastJsonRedisSerializer);
        // 单独设置keySerializer
        template.setKeySerializer(new StringRedisSerializer());
        // 单独设置valueSerializer
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

}
