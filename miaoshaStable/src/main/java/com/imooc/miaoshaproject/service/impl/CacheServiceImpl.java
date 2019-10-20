package com.imooc.miaoshaproject.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.imooc.miaoshaproject.service.CacheService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;


@Service
public class CacheServiceImpl implements CacheService {
    // google guava cache
    private Cache<String, Object> commonCache = null;

    // springboot优先加载init
    @PostConstruct
    public void init(){
        commonCache = CacheBuilder.newBuilder()
                // 设置缓存初始容量为10
                .initialCapacity(10)
                // 设置缓存中最大存储100个key，超过时淘汰策略lru
                .maximumSize(100)
                // 过期时间。通常都是相对于写入时间，因为热点数据会不停的读
                .expireAfterWrite(60, TimeUnit.SECONDS).build();
    }

    @Override
    public void setCommonCaches(String key, Object value) {
        commonCache.put(key, value);
    }

    @Override
    public Object getFromCommonCache(String key) {
        // 不存在时返回null
        return commonCache.getIfPresent(key);
    }
}
