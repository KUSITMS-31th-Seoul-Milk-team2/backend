package com.seoulmilk.core.configuration.cache.annotation.cacheable;

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Cacheable(cacheNames = "localCache", cacheManager = "localCacheManager", keyGenerator = "pageableCacheKeyGenerator")
public @interface LocalCacheOnly {
}
