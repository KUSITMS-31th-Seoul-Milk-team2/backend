package com.seoulmilk.core.configuration.cache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;


@Configuration
public class CacheKeyGeneratorConfiguration {

    @Bean("noticePageableKeyGenerator")
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            Pageable pageable = (Pageable) params[0];
            return String.format("page-%d-size-%d",
                    pageable.getPageNumber(),
                    pageable.getPageSize()
            );
        };
    }
}
