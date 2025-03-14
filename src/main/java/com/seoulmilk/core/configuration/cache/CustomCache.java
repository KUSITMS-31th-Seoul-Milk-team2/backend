package com.seoulmilk.core.configuration.cache;

import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

public record CustomCache(Cache firstLevelCache, Cache secondLevelCache) implements Cache {
    @Override
    public String getName() {
        return firstLevelCache.getName();
    }

    @Override
    public Object getNativeCache() {
        return firstLevelCache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper valueWrapper = firstLevelCache.get(key);

        if (valueWrapper == null) {
            valueWrapper = secondLevelCache.get(key);
            if (valueWrapper != null) {
                firstLevelCache.put(key, valueWrapper.get());
            }
        }
        return secondLevelCache.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        T value = firstLevelCache.get(key, type);
        if (value == null) {
            value = secondLevelCache.get(key, type);
            if (value != null) {
                firstLevelCache.put(key, value);
            }
        }
        return value;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        try {
            return firstLevelCache.get(key, valueLoader);
        } catch (Exception e) {
            return secondLevelCache.get(key, valueLoader);
        }
    }

    @Override
    public void put(Object key, Object value) {
        firstLevelCache.put(key, value);
        secondLevelCache.put(key, value);
    }

    @Override
    public void evict(Object key) {
        firstLevelCache.evict(key);
        secondLevelCache.evict(key);
    }

    @Override
    public void clear() {
        firstLevelCache.clear();
        secondLevelCache.clear();
    }
}
