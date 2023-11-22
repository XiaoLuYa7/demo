package com.lucloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public RedisTemplate redisTemplateInit() {
        StringRedisSerializer serializer = new StringRedisSerializer();
        //设置默认的序列化对象
        redisTemplate.setDefaultSerializer(serializer);
        //确认开启
        redisTemplate.setEnableDefaultSerializer(true);
        //设置序列化Key的实例化对象
        redisTemplate.setKeySerializer(serializer);
        //设置序列化Value的实例化对象
        redisTemplate.setValueSerializer(serializer);
        //设置序列化 hash key实例化对象
        redisTemplate.setHashKeySerializer(serializer);
        //设置序列化 hash value实例化对象
        redisTemplate.setHashValueSerializer(serializer);
        //开启redis事务一致
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }
}
