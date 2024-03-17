package ge.user.management.user;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {
  private Integer id;
  private String firstName;
  private String lastLame;
  private String email;
  private String role;
  private Boolean active;
  private LocalDateTime createDate;
  private LocalDateTime modificationDate;
}
