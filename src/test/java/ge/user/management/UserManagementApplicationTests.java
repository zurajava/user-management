package ge.user.management;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserManagementApplicationTests {
  private final static int EXPECTED_PORT = 8088;
  @Autowired
  private ServerProperties serverProperties;

  @Test
  void contextLoads() {
  }

  @Test
  public void givenFixedPortAsServerPort() {
    int port = serverProperties.getPort();
    assertEquals(EXPECTED_PORT, port);
  }
}
