package ge.user.management.configuration;


import ge.user.management.auth.AuthenticationUser;
import ge.user.management.user.UserDTO;
import ge.user.management.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
  @Autowired
  private UserService userService;

  public Boolean getUserById(int id) {
    UserDTO userDTO = userService.getById(id);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    AuthenticationUser authenticationUser = (AuthenticationUser) auth.getPrincipal();
    return userDTO.getId() == authenticationUser.getId();
  }
}
