package ge.user.management.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageView<T> {

  private Long totalCount;
  private List<T> data;

  public PageView(List<T> data, Long totalCount) {
    this.totalCount = totalCount;
    this.data = data;
  }

  public PageView() {
  }
}
