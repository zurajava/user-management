package ge.user.management.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.user.management.auth.AuthenticationRequest;
import ge.user.management.persistence.entities.Role;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Integration tests for UserController API endpoints")
@Tag("integration")
class UserControllerIntegrationTest {
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;
  String adminToken;
  @Autowired
  private UserService userService;
  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");
  private static boolean isSetUpExecuted = false;
  private static Integer adminUserId;

  @BeforeEach
  void setUp() throws Exception {
    AddEditUser user = AddEditUser.builder().firstName("testFirstName")
      .lastLame("testLastName").email("admin@gmail.com").password("admin").role(Role.admin.name()).active(true).build();
    if (!isSetUpExecuted) {
      this.adminUserId = userService.addUser(user).getId();
      isSetUpExecuted = true;
    }


    AuthenticationRequest adminAuth = AuthenticationRequest.builder()
      .email(user.getEmail()).password(user.getPassword()).build();
    String adminAuthRequest = this.objectMapper.writeValueAsString(adminAuth);
    ResultActions adminResultActions = this.mockMvc.perform(post("/auth/login")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .content(adminAuthRequest));
    MvcResult adminResult = adminResultActions.andDo(print()).andReturn();
    JSONObject adminJson = new JSONObject(adminResult.getResponse().getContentAsString());
    this.adminToken = "Bearer " + adminJson.getString("access_token");

  }

  @Test
  void connectionEstablished() {
    assertThat(postgres.isCreated()).isTrue();
    assertThat(postgres.isRunning()).isTrue();
  }

  @Test
  void shouldFindAllUsers() throws Exception {
    SearchUser searchUser = SearchUser.builder().pageSize(10).pageNumber(0).active(true).build();
    String searchUserRequest = this.objectMapper.writeValueAsString(searchUser);
    ResultActions findAllAction = this.mockMvc.perform(get("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, this.adminToken)
        .content(searchUserRequest))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.totalCount").value(greaterThan(0)));
  }

  @Test
  void shouldFindUserById() throws Exception {
    ResultActions findByIdAction = this.mockMvc.perform(get("/user/{id}", this.adminUserId)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, this.adminToken))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(this.adminUserId));
  }
}