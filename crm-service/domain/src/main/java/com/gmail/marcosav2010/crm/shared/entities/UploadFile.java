package com.gmail.marcosav2010.crm.shared.entities;

import java.io.InputStream;
import lombok.Builder;

@Builder
public record UploadFile(InputStream inputStream, String fileName, String contentType) {}
