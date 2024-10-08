package com.gmail.marcosav2010.crm.customer.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.gmail.marcosav2010.crm.customer.entities.Customer;
import com.gmail.marcosav2010.crm.customer.mapper.CustomerRowMapper;
import com.gmail.marcosav2010.crm.customer.repository.CustomerRepository;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@JdbcTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {CustomerRepository.class, CustomerRowMapper.class})
class CustomerRepositoryTestIT {

  private static final String IMAGE_VERSION = "postgres:16.4";

  @Autowired private CustomerRepository customerRepository;

  @Autowired private NamedParameterJdbcTemplate jdbcTemplate;

  @Container
  private static final PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>(DockerImageName.parse(IMAGE_VERSION));

  @DynamicPropertySource
  static void properties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
    registry.add("spring.datasource.password", postgresContainer::getPassword);
    registry.add("spring.datasource.username", postgresContainer::getUsername);
    registry.add("spring.datasource.driver-class-name", postgresContainer::getDriverClassName);
    registry.add("spring.liquibase.change-log", () -> "classpath:/liquibase/changelog.xml");
    registry.add("spring.liquibase.defaultSchema", () -> "public");
  }

  @BeforeEach
  void setUp() {
    final String query = "DELETE FROM customers";
    this.jdbcTemplate.update(query, Map.of());
  }

  @Test
  void insert() {
    final var dateTime = OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.UTC);
    final var customer =
        Customer.builder()
            .id(UUID.randomUUID())
            .name("name")
            .surname("surname")
            .profileImageUrl("profileImageUrl")
            .active(true)
            .createdAt(dateTime)
            .createdBy("createdBy")
            .updatedAt(dateTime)
            .updatedBy("updatedBy")
            .build();

    customerRepository.insert(customer);

    final var result = customerRepository.findById(customer.id());

    assertThat(result).isNotEmpty().contains(customer);
  }

  @Test
  void update() {
    final var dateTime = OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.UTC);
    final var customer =
        Customer.builder()
            .id(UUID.randomUUID())
            .name("name")
            .surname("surname")
            .profileImageUrl("profileImageUrl")
            .active(true)
            .createdAt(dateTime)
            .createdBy("createdBy")
            .updatedAt(dateTime)
            .updatedBy("updatedBy")
            .build();

    customerRepository.insert(customer);

    final var updated =
        customer.toBuilder()
            .name("newName")
            .surname("surname2")
            .profileImageUrl("profileImageUrl2")
            .updatedAt(OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.UTC))
            .updatedBy("user")
            .active(false)
            .build();

    customerRepository.update(updated);

    final var result = customerRepository.findById(customer.id());

    assertThat(result).isNotEmpty().contains(updated);
    assertThat(result.get().name()).isEqualTo("newName");
    assertThat(result.get().surname()).isEqualTo("surname2");
    assertThat(result.get().updatedAt()).isAfter(dateTime);
    assertThat(result.get().updatedBy()).isEqualTo("user");
    assertThat(result.get().createdAt()).isEqualTo(dateTime);
    assertThat(result.get().createdBy()).isEqualTo("createdBy");
    assertThat(result.get().profileImageUrl()).isEqualTo("profileImageUrl2");
    assertThat(result.get().active()).isFalse();
  }

  @Test
  void findActive() {
    final var dateTime = OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.UTC);
    final var customer =
        Customer.builder()
            .id(UUID.randomUUID())
            .name("name")
            .surname("surname")
            .profileImageUrl("profileImageUrl")
            .active(true)
            .createdAt(dateTime)
            .createdBy("createdBy")
            .updatedAt(dateTime)
            .updatedBy("updatedBy")
            .build();

    customerRepository.insert(customer);

    final var result = customerRepository.findByActiveWithLimitAndOffset(true, 10, 0);

    assertThat(result).isNotEmpty().contains(customer);
  }

  @Test
  void countActive() {
    final var dateTime = OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.UTC);
    final var customer =
        Customer.builder()
            .id(UUID.randomUUID())
            .name("name")
            .surname("surname")
            .profileImageUrl("profileImageUrl")
            .active(true)
            .createdAt(dateTime)
            .createdBy("createdBy")
            .updatedAt(dateTime)
            .updatedBy("updatedBy")
            .build();

    customerRepository.insert(customer);

    final var result = customerRepository.countByActive(true);

    assertThat(result).isEqualTo(1);
  }

  @AfterAll
  static void afterAll() {
    postgresContainer.stop();
  }
}
