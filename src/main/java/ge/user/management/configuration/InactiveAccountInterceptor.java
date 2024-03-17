package ge.user.management.configuration;

import ge.user.management.auth.AuthenticationUser;
import ge.user.management.user.UserDTO;
import ge.user.management.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class InactiveAccountInterceptor implements HandlerInterceptor {
  @Autowired
  private UserService userService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    throws Exception {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.isAuthenticated()) {
      AuthenticationUser loggedUser = ((AuthenticationUser) auth.getPrincipal());
      UserDTO userDTO = userService.getById(loggedUser.getId());

      if (!userDTO.getActive()) {
        SecurityContextHolder.clearContext();
        //TODO return error response
        return false;
      }
    }
    return true;
  }
}
