package com.gmail.marcosav2010.crm.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
class CacheConfig {

  @Bean("urlProvider")
  public CacheManager dailyFoodCacheManager(@Value("${crm.files.url-lifetime}") long ttlSeconds) {
    final var cacheManager = new CaffeineCacheManager("urlProvider");
    cacheManager.setCaffeine(
        Caffeine.newBuilder().expireAfterWrite(Duration.ofSeconds(ttlSeconds)));
    return cacheManager;
  }
}
