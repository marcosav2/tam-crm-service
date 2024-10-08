package com.gmail.marcosav2010.crm.user.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.mapper.UserRowMapper;
import com.gmail.marcosav2010.crm.user.repository.UserRepository;
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
@ContextConfiguration(classes = {UserRepository.class, UserRowMapper.class})
class UserRepositoryTestIT {

  private static final String IMAGE_VERSION = "postgres:16.4";

  @Autowired private UserRepository userRepository;

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
    final String query = "DELETE FROM users";
    this.jdbcTemplate.update(query, Map.of());
  }

  @Test
  void insert() {
    final var user =
        User.builder()
            .id(UUID.randomUUID())
            .username("aaaaa")
            .name("name")
            .surname("surname")
            .active(true)
            .build();

    userRepository.insert(user);

    final var result = userRepository.findById(user.id());

    assertThat(result).isNotEmpty().contains(user);
  }

  @Test
  void update() {
    final var user =
        User.builder()
            .id(UUID.randomUUID())
            .username("aaaaa")
            .password("assd")
            .name("name")
            .surname("surname")
            .active(true)
            .build();

    userRepository.insert(user);

    final var updated =
        user.toBuilder()
            .username("vvvvv")
            .password("fasd")
            .name("newName")
            .surname("surname2")
            .active(false)
            .build();

    userRepository.update(updated);

    final var result = userRepository.findById(user.id());

    assertThat(result).isNotEmpty();
    assertThat(result.get().username()).isEqualTo("aaaaa");
    assertThat(result.get().password()).isEqualTo("fasd");
    assertThat(result.get().name()).isEqualTo("newName");
    assertThat(result.get().surname()).isEqualTo("surname2");
    assertThat(result.get().active()).isFalse();
  }

  @Test
  void findByUsername() {
    final var user =
        User.builder()
            .id(UUID.randomUUID())
            .username("aaaaa")
            .name("name")
            .surname("surname")
            .active(true)
            .build();

    userRepository.insert(user);

    final var result = userRepository.findByUsername(user.username());

    assertThat(result).isNotEmpty().contains(user);
  }

  @Test
  void findActive() {
    final var user =
        User.builder()
            .id(UUID.randomUUID())
            .username("aaafs")
            .name("name")
            .surname("surname")
            .active(true)
            .build();

    userRepository.insert(user);

    final var result = userRepository.findByActiveWithLimitAndOffset(true, 10, 0);

    assertThat(result).isNotEmpty().contains(user);
  }

  @Test
  void countActive() {
    final var user =
        User.builder()
            .id(UUID.randomUUID())
            .username("aaafs")
            .name("name")
            .surname("surname")
            .active(true)
            .build();

    userRepository.insert(user);

    final var result = userRepository.countByActive(true);

    assertThat(result).isEqualTo(1);
  }

  @AfterAll
  static void afterAll() {
    postgresContainer.stop();
  }
}
