package ge.user.management.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.user.management.configuration.JwtService;
import ge.user.management.persistence.entities.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Unit tests for UserController")
@Tag("unit")
class UserControllerTest {
  @Autowired
  MockMvc mockMvc;
  @MockBean
  UserService userService;
  @MockBean
  JwtService jwtService;
  @Autowired
  ObjectMapper objectMapper;
  List<UserDTO> users = new ArrayList<>();
  UserDTO user;

  @BeforeEach
  void setUp() {
    users = List.of(
      UserDTO.builder()
        .id(1)
        .firstName("testFirstName1")
        .lastLame("testLastName1")
        .email("test1@gmail.com")
        .role(Role.guest.name())
        .active(true)
        .createDate(LocalDateTime.now())
        .modificationDate(LocalDateTime.now())
        .build(),

      UserDTO.builder()
        .id(2)
        .firstName("testFirstName2")
        .lastLame("testLastName2")
        .email("test2@gmail.com")
        .role(Role.admin.name())
        .active(true)
        .createDate(LocalDateTime.now())
        .modificationDate(LocalDateTime.now())
        .build());

    this.user = UserDTO.builder()
      .id(1)
      .firstName("byidtest")
      .lastLame("byidtest")
      .email("byidtest@gmail.com")
      .role(Role.guest.name())
      .active(true)
      .createDate(LocalDateTime.now())
      .modificationDate(LocalDateTime.now())
      .build();
  }


  @Test
  @DisplayName("Check shouldFindAllUsers (GET with request body)")
  void shouldFindAllUsers() throws Exception {
    SearchUser searchUser = SearchUser.builder().pageSize(15).pageNumber(0).active(false).build();
    String searchUserJsonRequest = this.objectMapper.writeValueAsString(searchUser);

    given(this.userService.search(searchUser)).willReturn(users);
    given(this.userService.getUserCount(searchUser)).willReturn((long) users.size());

    ResultActions searchUserActions = this.mockMvc.perform(get("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(searchUserJsonRequest))
      .andExpect(status().isOk());

    MvcResult searchUserActionsResult = searchUserActions.andDo(print()).andReturn();
  }

  @Test
  @DisplayName("Check shouldFindUserById (GET)")
  void shouldFindUserById() throws Exception {
    when(userService.getById(1)).thenReturn(this.user);

    ResultActions searchUserActions = this.mockMvc.perform(get("/user/{id}", this.user.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(this.user.getId()))
      .andExpect(jsonPath("$.firstName").value(this.user.getFirstName()))
      .andExpect(jsonPath("$.lastLame").value(this.user.getLastLame()))
      .andExpect(jsonPath("$.email").value(this.user.getEmail()))
      .andExpect(jsonPath("$.role").value(this.user.getRole()))
      .andExpect(jsonPath("$.active").value(this.user.getActive()));
  }
}