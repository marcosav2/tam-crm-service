package com.gmail.marcosav2010.crm.customer.ports;

import com.gmail.marcosav2010.crm.shared.entities.UploadFile;

public interface ProfileImageStoragePort {

  String save(UploadFile uploadFile);

  void delete(String key);
}
