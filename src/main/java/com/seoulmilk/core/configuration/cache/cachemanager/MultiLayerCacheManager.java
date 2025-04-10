package com.seoulmilk.core.configuration.cache.cachemanager;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MultiLayerCacheManager implements CacheManager {
    private final CacheManager localCacheManager;
    private final CacheManager distributedCacheManager;


    public MultiLayerCacheManager(
            @Qualifier("localCacheManager") CacheManager localCacheManager,
            @Qualifier("distributedCacheManager") CacheManager distributedCacheManager
    ) {
        this.localCacheManager = localCacheManager;
        this.distributedCacheManager = distributedCacheManager;
    }

    @Override
    public Cache getCache(String name) {
        return new MultiLayerCache(
                localCacheManager.getCache("localCache"),
                distributedCacheManager.getCache("distributedCache")
        );
    }

    @Override
    public Collection<String> getCacheNames() {
        return Stream.concat(
                localCacheManager.getCacheNames().stream(),
                distributedCacheManager.getCacheNames().stream()
        ).collect(Collectors.toSet());
    }

}
