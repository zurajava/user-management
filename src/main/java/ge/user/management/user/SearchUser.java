package ge.user.management.user;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchUser {
  private int pageNumber;
  private int pageSize;
  private Boolean active;
}
