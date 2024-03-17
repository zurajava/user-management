package ge.user.management.auth;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
  private String firstName;
  private String lastLame;
  private String email;
  private String password;
}
