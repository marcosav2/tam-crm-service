package com.gmail.marcosav2010.crm.api.validation;

import com.gmail.marcosav2010.crm.api.exception.InvalidImageTypeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Validate {

  public static void imageType(MultipartFile multipartFile) {
    if (multipartFile == null || multipartFile.isEmpty()) {
      return;
    }

    String contentType = multipartFile.getContentType();
    if (!isSupportedContentType(contentType)) {
      throw new InvalidImageTypeException(
          contentType + " is not a supported image format, only PNG, JPG and JPEG are supported");
    }
  }

  private static boolean isSupportedContentType(String contentType) {
    return contentType != null
        && (contentType.contains("image/png")
            || contentType.contains("image/jpg")
            || contentType.contains("image/jpeg"));
  }
}
