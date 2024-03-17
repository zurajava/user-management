package ge.user.management.user;

import ge.user.management.utils.PageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping()
  @PreAuthorize("hasAuthority('admin')")
  public PageView<UserDTO> getAllUsers(@RequestBody SearchUser searchUser) {
    List<UserDTO> list = userService.search(searchUser);
    Long total = userService.getUserCount(searchUser);
    return new PageView<>(list, total);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('admin')")
  public UserDTO getUserById(@PathVariable Integer id) {
    return userService.getById(id);
  }

  @PostMapping()
  @PreAuthorize("hasAuthority('admin')")
  public UserDTO addUser(@RequestBody AddEditUser addEditUser) {
    return userService.addUser(addEditUser);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('admin')")
  public UserDTO editUser(@PathVariable Integer id, @RequestBody AddEditUser addEditUser) {
    return userService.editUser(id, addEditUser);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('admin')")
  public void deleteUser(@PathVariable Integer id) {
    userService.deleteById(id);
  }
}
