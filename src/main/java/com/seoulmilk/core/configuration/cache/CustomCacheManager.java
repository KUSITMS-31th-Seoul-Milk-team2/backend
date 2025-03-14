package com.seoulmilk.core.configuration.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;

@Getter
@Setter
public class CustomCacheManager implements CacheManager {

    private CacheManager caffeineCacheManager;
    private CacheManager redisCacheManager;

    public CustomCacheManager(CacheManager caffeineCacheManager, CacheManager redisCacheManager) {
        this.caffeineCacheManager = caffeineCacheManager;
        this.redisCacheManager = redisCacheManager;
    }

    @Override
    public Cache getCache(String name) {
        Cache caffeineCache = caffeineCacheManager.getCache(name);
        Cache redisCache = redisCacheManager.getCache(name);
        return new CustomCache(caffeineCache, redisCache);
    }

    @Override
    public Collection<String> getCacheNames() {
        return caffeineCacheManager.getCacheNames();
    }
}
