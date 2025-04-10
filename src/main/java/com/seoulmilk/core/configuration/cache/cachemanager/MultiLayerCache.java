package com.seoulmilk.core.configuration.cache.cachemanager;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class MultiLayerCache implements Cache {
    private final Cache localCache;
    private final Cache distributedCache;

    @Override
    public String getName() {
        return localCache.getName();
    }

    @Override
    public Object getNativeCache() {
        return localCache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper value = localCache.get(key);
        if (value == null) {
            value = distributedCache.get(key);
            if (value != null) {
                localCache.put(key, value.get());
            }
        }
        return value;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        T value = localCache.get(key, type);
        if (value == null) {
            value = distributedCache.get(key, type);
            if (value != null) {
                localCache.put(key, value);
            }
        }
        return value;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        try {
            return localCache.get(key, valueLoader);
        } catch (Exception e) {
            return distributedCache.get(key, valueLoader);
        }
    }

    @Override
    public void put(Object key, Object value) {
        distributedCache.put(key, value);  // L2에 먼저 저장
        localCache.put(key, value);        // L1 갱신
    }

    @Override
    public void evict(Object key) {
        distributedCache.evict(key);
        localCache.evict(key);
    }

    @Override
    public void clear() {
        distributedCache.clear();
        localCache.clear();
    }
}
