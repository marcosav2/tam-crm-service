package com.gmail.marcosav2010.crm.customer.ports;

import java.io.InputStream;

public interface ProfileImagePort {

  String save(InputStream stream);

  String generateTempUrl(String key);

  void delete(String key);
}
