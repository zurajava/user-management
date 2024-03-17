package ge.user.management.user;

import ge.user.management.auth.AuthenticationUser;
import ge.user.management.auth.RegisterRequest;
import ge.user.management.persistence.entities.Role;
import ge.user.management.persistence.entities.User;
import ge.user.management.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "user")
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Optional<AuthenticationUser> getAuthenticationUser(String email) {
    AuthenticationUser jwtUser = new AuthenticationUser();
    Optional<User> user = userRepository.findByEmail(email);

    if (user.isPresent()) {
      jwtUser.setId(user.get().getId());
      jwtUser.setFirstName(user.get().getFirstName());
      jwtUser.setLastName(user.get().getLastLame());
      jwtUser.setEmail(user.get().getEmail());
      jwtUser.setRole(user.get().getRole());
      jwtUser.setPassword(user.get().getPassword());
      return Optional.of(jwtUser);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Optional<UserDTO> getUserByEmail(String email) {
    Optional<User> userOptional = userRepository.findByEmail(email);
    return userOptional.map(this::map);
  }

  @Override
  public UserDTO addGuestUser(RegisterRequest request) {
    var user = new User();
    return map(userRepository.save(convertUserRequestToUser(user, request)));
  }

  @Override
  public UserDTO editGuestUser(Integer id, RegisterRequest request) {
    var user = new User();
    user.setId(id);
    return map(userRepository.save(convertUserRequestToUser(user, request)));
  }

  @Override
  @Cacheable(value = "userSearch", key = "#searchUser.toString()")
  public List<UserDTO> search(SearchUser searchUser) {
    return userRepository.findAll(
      PageRequest.of(
        searchUser.getPageNumber(),
        searchUser.getPageSize(),
        Sort.Direction.DESC, "id"
      )
    ).stream().map(this::map).toList();
  }

  @Override
  public Long getUserCount(SearchUser searchUser) {
    return userRepository.count();
  }

  @Override
  @Cacheable(value = "user", key = "#id")
  public UserDTO getById(Integer id) {
    return map(userRepository.findById(id).orElseThrow());
  }

  @Override
  public UserDTO addUser(AddEditUser addEditUser) {
    var user = new User();
    return map(userRepository.save(convertAddEditUserToUser(user, addEditUser)));

  }

  @Override
  public UserDTO editUser(Integer id, AddEditUser addEditUser) {
    var user = new User();
    user.setId(id);
    return map(userRepository.save(convertAddEditUserToUser(user, addEditUser)));
  }

  @Override
  public void deleteById(Integer id) {
    userRepository.deleteById(id);
  }

  private User convertUserRequestToUser(User user, RegisterRequest addUser) {
    user.setFirstName(addUser.getFirstName());
    user.setLastLame(addUser.getLastLame());
    user.setEmail(addUser.getEmail());
    String encodedPassword = passwordEncoder.encode(addUser.getPassword());
    user.setPassword(encodedPassword);
    user.setRole(Role.guest);
    user.setActive(true);
    return user;
  }

  private User convertAddEditUserToUser(User user, AddEditUser addEditUser) {
    user.setFirstName(addEditUser.getFirstName());
    user.setLastLame(addEditUser.getLastLame());
    user.setEmail(addEditUser.getEmail());
    //For admin maybe we don't need to change password for guest
    String encodedPassword = passwordEncoder.encode(addEditUser.getPassword());
    user.setPassword(encodedPassword);
    user.setRole(Role.valueOf(addEditUser.getRole()));
    user.setActive(addEditUser.getActive());
    return user;
  }

  private UserDTO map(User user) {
    var userDTO = new UserDTO();
    userDTO.setId(user.getId());
    userDTO.setFirstName(user.getFirstName());
    userDTO.setLastLame(user.getLastLame());
    userDTO.setEmail(user.getEmail());
    userDTO.setRole(user.getRole().name());
    userDTO.setActive(user.getActive());
    userDTO.setCreateDate(user.getCreateDate());
    userDTO.setModificationDate(user.getModificationDate());
    return userDTO;
  }
}
