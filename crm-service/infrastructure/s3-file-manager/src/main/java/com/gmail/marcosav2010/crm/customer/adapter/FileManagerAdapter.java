package com.gmail.marcosav2010.crm.customer.adapter;

import com.gmail.marcosav2010.crm.customer.ports.ProfileImagePort;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@CustomLog
@RequiredArgsConstructor
public class FileManagerAdapter implements ProfileImagePort {

  private static final String BASE_PATH = "profile-images";

  private final S3Client s3Client;

  private final String bucketName;

  @Override
  public String save(final InputStream stream) {
    final var name = UUID.randomUUID().toString();
    final var key = String.format("%s/%s", BASE_PATH, name);
    final PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(key).build();

    try {
      final ByteBuffer byteBuffer = ByteBuffer.wrap(stream.readAllBytes());
      s3Client.putObject(request, RequestBody.fromByteBuffer(byteBuffer));

      return key;
    } catch (final Exception e) {
      throw new RuntimeException("Error saving object.", e);
    }
  }

  @Override
  public String generateTempUrl(final String key) {
    try (final S3Presigner presigner = S3Presigner.create()) {

      final GetObjectRequest objectRequest =
          GetObjectRequest.builder().bucket(bucketName).key(key).build();

      final GetObjectPresignRequest presignRequest =
          GetObjectPresignRequest.builder()
              .signatureDuration(Duration.ofMinutes(10))
              .getObjectRequest(objectRequest)
              .build();

      final PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

      return presignedRequest.url().toExternalForm();
    }
  }

  @Override
  public void delete(final String key) {
    try {
      final DeleteObjectRequest deleteObjectRequest =
          DeleteObjectRequest.builder().bucket(this.bucketName).key(key).build();

      this.s3Client.deleteObject(deleteObjectRequest);

    } catch (final Exception e) {
      throw new RuntimeException("Error deleting object.", e);
    }
  }
}
