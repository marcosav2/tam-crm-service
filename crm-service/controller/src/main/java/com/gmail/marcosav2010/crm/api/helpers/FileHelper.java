package com.gmail.marcosav2010.crm.api.helpers;

import com.gmail.marcosav2010.crm.shared.entities.UploadFile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.NONE)
public class FileHelper {

  public static UploadFile toDomain(MultipartFile file) {
    try {
      if (file == null || file.isEmpty()) {
        return null;
      }
      return UploadFile.builder()
          .inputStream(file.getInputStream())
          .contentType(file.getContentType())
          .fileName(file.getOriginalFilename())
          .build();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
