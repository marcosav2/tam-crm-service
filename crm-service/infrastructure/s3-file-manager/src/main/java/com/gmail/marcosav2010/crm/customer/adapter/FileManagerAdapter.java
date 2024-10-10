package com.gmail.marcosav2010.crm.customer.adapter;

import com.gmail.marcosav2010.crm.customer.ports.ProfileImageStoragePort;
import com.gmail.marcosav2010.crm.customer.ports.ProfileImageURLProviderPort;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.UUID;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@CustomLog
@RequiredArgsConstructor
public class FileManagerAdapter implements ProfileImageStoragePort, ProfileImageURLProviderPort {

  private static final String BASE_PATH = "profile-images";

  private final S3Client s3Client;

  private final String bucketName;

  private final URI endpointOverride;

  private final long urlLifetime;

  @Override
  public String save(final InputStream stream) {
    final var name = UUID.randomUUID().toString();
    final var key = String.format("%s/%s/%s", bucketName, BASE_PATH, name);
    final PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(key).build();
    log.debug("Saving object {} in bucket {}", key, bucketName);

    try {
      final ByteBuffer byteBuffer = ByteBuffer.wrap(stream.readAllBytes());
      log.debug("Uploading {} bytes", byteBuffer.capacity());

      s3Client.putObject(request, RequestBody.fromByteBuffer(byteBuffer));

      return key;
    } catch (final Exception e) {
      throw new RuntimeException("Error saving object.", e);
    }
  }

  @Override
  public String generateURL(final String key) {
    try (final S3Presigner presigner =
        S3Presigner.builder()
            .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
            .endpointOverride(endpointOverride)
            .build()) {
      log.debug("Generating temporary URL for object {}", key);
      final GetObjectRequest objectRequest =
          GetObjectRequest.builder().bucket(bucketName).key(key).build();

      final GetObjectPresignRequest presignRequest =
          GetObjectPresignRequest.builder()
              .signatureDuration(Duration.ofSeconds(urlLifetime))
              .getObjectRequest(objectRequest)
              .build();

      final PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

      return presignedRequest.url().toExternalForm();
    }
  }

  @Override
  public void delete(final String key) {
    try {
      log.debug("Deleting object {}", key);
      final DeleteObjectRequest deleteObjectRequest =
          DeleteObjectRequest.builder().bucket(this.bucketName).key(key).build();

      this.s3Client.deleteObject(deleteObjectRequest);

    } catch (final Exception e) {
      throw new RuntimeException("Error deleting object.", e);
    }
  }
}
