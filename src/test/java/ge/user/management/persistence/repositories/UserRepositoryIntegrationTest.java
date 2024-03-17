package ge.user.management.persistence.repositories;

import ge.user.management.persistence.entities.Role;
import ge.user.management.persistence.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Testcontainers
//Instead of @SpringBootTest we can use @DataJdbcTest to avoid whole application context load, but we are using spring data jpa repositories instead of spring data jdbc
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Integration tests for UserRepository")
@Tag("integration")
class UserRepositoryIntegrationTest {
  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");
  @Autowired
  UserRepository userRepository;
  @Autowired
  JdbcConnectionDetails jdbcConnectionDetails;
  private static boolean isSetUpExecuted = false;

  //We can use @BeforeAll but in this case setUp() method and  userRepository should be static
  @BeforeEach
  void setUp() {
    if (!isSetUpExecuted) {
      List<User> user = List.of(new User("testFirstName", "'testLastName", "test@gmail.com", "testpassword", Role.guest, true));
      userRepository.saveAll(user);
      isSetUpExecuted = true;
    }
  }

  @Test
  void connectionEstablished() {
    assertThat(postgres.isCreated()).isTrue();
    assertThat(postgres.isRunning()).isTrue();
  }

  @Test
  void shouldReturnUserByEmail() {
    User user = userRepository.findByEmail("test@gmail.com").orElseThrow();
    assertEquals("test@gmail.com", user.getEmail(), "User Email should be 'test@gmail.com'");
  }

  @Test
  void shouldNotReturnUserWhenEmailIsNotFound() {
    Optional<User> user = userRepository.findByEmail("notexist@gmail.com");
    assertFalse(user.isPresent(), "User should not be present");
  }
}