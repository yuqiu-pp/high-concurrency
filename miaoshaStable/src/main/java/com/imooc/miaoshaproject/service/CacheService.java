package com.imooc.miaoshaproject.service;

public interface CacheService {
    // 存方法
    void setCommonCaches(String key, Object value);

    // 取方法
    Object getFromCommonCache(String key);
}
