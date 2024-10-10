package com.gmail.marcosav2010.crm.config;

import com.gmail.marcosav2010.crm.customer.adapter.FileManagerAdapter;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class FileManagerConfig {

  @Value("${aws.region}")
  private String region;

  @Value("${aws.s3.access-key}")
  private String accessKey;

  @Value("${aws.s3.secret-key}")
  private String secretKey;

  @Value("${aws.s3.bucket}")
  private String bucketName;

  @Value("${aws.s3.endpoint}")
  private String endpoint;

  @Value("${aws.s3.external-endpoint}")
  private String externalEndpoint;

  @Value("${crm.files.url-lifetime}")
  private long urlLifetime;

  @Bean
  public S3Client s3Client() {
    final var s3Client =
        S3Client.builder()
            .forcePathStyle(true)
            .region(Region.of(region))
            .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey));

    if (endpoint != null) {
      s3Client.endpointOverride(URI.create(endpoint));
    }

    return s3Client.build();
  }

  @Bean
  public FileManagerAdapter fileManagerPort(final S3Client s3Client) {
    final var endpointURI =
        endpoint == null
            ? null
            : URI.create(externalEndpoint == null ? endpoint : externalEndpoint);
    return new FileManagerAdapter(s3Client, bucketName, endpointURI, urlLifetime);
  }
}
