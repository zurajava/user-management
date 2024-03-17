package ge.user.management.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddEditUser {
  private String firstName;
  private String lastLame;
  private String email;
  private String password;
  private String role;
  private Boolean active;
}
