package ge.user.management.user;

import ge.user.management.exception.AbstractValidationException;
import ge.user.management.exception.ValidationError;


public class UserAlreadyExistsException extends AbstractValidationException {

  public UserAlreadyExistsException addError(ValidationError validationError) {
    this.errors.add(validationError);
    return this;
  }

  public static UserAlreadyExistsException build(String field, String message) {
    return new UserAlreadyExistsException().addError(new ValidationError(field, message));
  }
}
