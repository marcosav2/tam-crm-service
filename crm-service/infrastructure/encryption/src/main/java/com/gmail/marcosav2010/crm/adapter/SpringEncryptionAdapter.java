package com.gmail.marcosav2010.crm.adapter;

import com.gmail.marcosav2010.crm.user.ports.PasswordEncryptionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringEncryptionAdapter implements PasswordEncryptionPort {

  private final PasswordEncoder passwordEncoder;

  @Override
  public String process(String password) {
    return passwordEncoder.encode(password);
  }
}
