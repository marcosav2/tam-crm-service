package com.gmail.marcosav2010.crm.api.helpers;

import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@NoArgsConstructor(access = AccessLevel.NONE)
public class FileHelper {

  public static InputStream getInputStream(MultipartFile file) {
    try {
      if (file == null || file.isEmpty()) {
        return null;
      }
      return file.getInputStream();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
