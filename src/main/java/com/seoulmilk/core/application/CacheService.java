package com.seoulmilk.core.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CacheService {
    private final CacheManager cacheManager;

    public void checkCacheContent() {
        Cache cache = cacheManager.getCache("notice");
        if (cache != null) {
            log.info("캐시 구현체: {}", cache.getClass().getName());
            log.info("네이티브 캐시 구현체: {}", cache.getNativeCache().getClass().getName());

            if (cache instanceof CaffeineCache) {
                log.info("CaffeineCache 검색.");
            } else if (cache instanceof RedisCache) {
                log.info("RedisCache의 검색");
            }
        } else {
            log.warn("캐시를 찾을 수 없습니다.");
        }
    }
}
