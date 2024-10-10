package com.gmail.marcosav2010.crm.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gmail.marcosav2010.crm.customer.ports.ProfileImageURLProviderPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CachedURLProviderTest {

  @Mock private ProfileImageURLProviderPort fileManagerPort;

  @InjectMocks private CachedURLProvider cachedURLProvider;

  @Test
  void generateURL() {
    when(fileManagerPort.generateURL("key")).thenReturn("url");

    final var url = cachedURLProvider.generateURL("key");
    assertThat(url).isEqualTo("url");

    verify(fileManagerPort).generateURL("key");
  }
}
