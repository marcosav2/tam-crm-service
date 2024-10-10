package com.gmail.marcosav2010.crm.adapter;

import com.gmail.marcosav2010.crm.customer.ports.ProfileImageURLProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class CachedURLProvider implements ProfileImageURLProviderPort {

  private final ProfileImageURLProviderPort fileManagerPort;

  @Override
  @Cacheable(cacheManager = "urlProvider", cacheNames = "urlProvider")
  public String generateURL(String key) {
    return fileManagerPort.generateURL(key);
  }
}
