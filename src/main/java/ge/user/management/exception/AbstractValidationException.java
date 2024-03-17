package ge.user.management.exception;

import java.util.ArrayList;
import java.util.List;


public class AbstractValidationException extends Exception {

  protected List<ValidationError> errors = new ArrayList<>();

  public AbstractValidationException addError(ValidationError validationError) {
    this.errors.add(validationError);
    return this;
  }

  public List<ValidationError> getErrors() {
    return errors;
  }
}
