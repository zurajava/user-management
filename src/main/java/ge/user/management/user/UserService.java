package ge.user.management.user;

import ge.user.management.auth.AuthenticationUser;
import ge.user.management.auth.RegisterRequest;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface UserService {
  Optional<AuthenticationUser> getAuthenticationUser(String email);

  Optional<UserDTO> getUserByEmail(String email);

  UserDTO addGuestUser(RegisterRequest request);

  UserDTO editGuestUser(Integer id, RegisterRequest request);

  //Admin methods
  List<UserDTO> search(SearchUser searchUser);

  Long getUserCount(SearchUser searchUser);

  UserDTO getById(Integer id);

  UserDTO addUser(AddEditUser addEditUser);

  UserDTO editUser(Integer id, @RequestBody AddEditUser addEditUser);

  void deleteById(Integer id);
}
