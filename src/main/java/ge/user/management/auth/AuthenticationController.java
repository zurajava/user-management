package ge.user.management.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
  @Autowired
  private AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
    @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }

  @PutMapping("/register/{id}")
  @PreAuthorize("@securityService.getUserById(#id)")
  public ResponseEntity<AuthenticationResponse> editRegistration(
    @PathVariable Integer id,
    @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.editRegistration(id, request));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(
    @RequestBody AuthenticationRequest request
  ) {
    System.out.println("login method called");
    return ResponseEntity.ok(service.login(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }
}
