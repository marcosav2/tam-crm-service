package com.gmail.marcosav2010.crm.customer.ports;

import java.io.InputStream;

public interface ProfileImageStoragePort {

  String save(InputStream stream);

  void delete(String key);
}
