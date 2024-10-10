package com.gmail.marcosav2010.crm.customer.adapter;

import com.gmail.marcosav2010.crm.customer.ports.ProfileImageStoragePort;
import com.gmail.marcosav2010.crm.customer.ports.ProfileImageURLProviderPort;
import com.gmail.marcosav2010.crm.shared.entities.UploadFile;
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
  public String save(final UploadFile uploadFile) {
    final var originalExtension =
        uploadFile.fileName().substring(uploadFile.fileName().lastIndexOf("."));
    final var name = UUID.randomUUID().toString();
    final var key = String.format("%s/%s%s", BASE_PATH, name, originalExtension);
    final PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(key).build();
    log.debug("Saving object {} in bucket {}", key, bucketName);

    try (final var inputStream = uploadFile.inputStream()) {
      final ByteBuffer byteBuffer = ByteBuffer.wrap(inputStream.readAllBytes());
      log.debug("Uploading {} bytes", byteBuffer.capacity());

      s3Client.putObject(request, RequestBody.fromByteBuffer(byteBuffer));

      return bucketName + "/" + key;
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
      final String bucket = key.split("/")[0];
      final String objectKey = key.replace(bucket + "/", "");
      final GetObjectRequest objectRequest =
          GetObjectRequest.builder().bucket(bucket).key(objectKey).build();

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
      final String bucket = key.split("/")[0];
      final String objectKey = key.replace(bucket + "/", "");
      final DeleteObjectRequest deleteObjectRequest =
          DeleteObjectRequest.builder().bucket(bucket).key(objectKey).build();

      this.s3Client.deleteObject(deleteObjectRequest);

    } catch (final Exception e) {
      throw new RuntimeException("Error deleting object.", e);
    }
  }
}
