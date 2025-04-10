package com.seoulmilk.core.configuration.cache.annotation.evict;

import org.springframework.cache.annotation.CacheEvict;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@CacheEvict(
        value = "distributedCache",
        cacheManager = "distributedCacheManager",
        allEntries = true
)
public @interface DistributedCacheEvict {
}
