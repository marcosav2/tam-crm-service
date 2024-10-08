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
public class S3Config {

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

  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
        .region(Region.of(region))
        .endpointOverride(URI.create(endpoint))
        .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
        .build();
  }

  @Bean
  public FileManagerAdapter createFileManagerPort(final S3Client s3Client) {
    return new FileManagerAdapter(s3Client, bucketName);
  }
}
