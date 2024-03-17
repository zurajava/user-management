package ge.user.management.persistence.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "\"user\"")
public class User {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "first_name")
  private String firstName;
  @Column(name = "last_name")
  private String lastLame;
  @Column(name = "email")
  private String email;
  @Column(name = "password")
  private String password;
  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private Role role;
  @Column(name = "active")
  private Boolean active;
  @Column(name = "create_date")
  @CreationTimestamp
  private LocalDateTime createDate;
  @Column(name = "modification_date")
  @UpdateTimestamp
  private LocalDateTime modificationDate;

  public User() {
  }

  public User(String firstName, String lastLame, String email, String password, Role role, Boolean active) {
    this.firstName = firstName;
    this.lastLame = lastLame;
    this.email = email;
    this.password = password;
    this.role = role;
    this.active = active;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastLame() {
    return lastLame;
  }

  public void setLastLame(String lastLame) {
    this.lastLame = lastLame;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  public LocalDateTime getModificationDate() {
    return modificationDate;
  }

  public void setModificationDate(LocalDateTime modificationDate) {
    this.modificationDate = modificationDate;
  }
}
