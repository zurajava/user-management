package ge.user.management.exception;


import ge.user.management.utils.ObjectMapperUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionController {

  private final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);

  @ExceptionHandler(AbstractValidationException.class)
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  public ResponseEntity<Object> handleAbstractValidationException(AbstractValidationException ex) {
    logger.debug(ex.getMessage(), ex);
    return buildResponse(ex.getErrors(), HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(ObjectNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<Object> handleObjectNotFoundException(ObjectNotFoundException ex) {
    logger.debug(ex.getMessage(), ex);
    return buildResponse(
      List.of(new ValidationError(
        "alert", "Not Fount")), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleMethodArgumentNotValidException(
    MethodArgumentNotValidException ex) {
    logger.debug(ex.getMessage(), ex);

    BindingResult result = ex.getBindingResult();
    final List<ObjectError> objectErrors = result.getAllErrors();

    List<ValidationError> validationErrors = new ArrayList<>();

    for (ObjectError objectError : objectErrors) {
      validationErrors.add(new ValidationError(((FieldError) objectError).getField(),
        objectError.getDefaultMessage()));
    }
    return buildResponse(validationErrors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleConstraintViolationException(
    ConstraintViolationException ex) {
    return buildResponse(
      ex.getConstraintViolations()
        .stream()
        .map(c -> new ValidationError("alert", c.getMessage()))
        .collect(Collectors.toList()), HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<Object> handleAuthenticationException(Exception ex) {
    logger.error(ex.getMessage(), ex);
    return buildResponse(
      List.of(new ValidationError(
        "alert", "Username or Password is incorrect.")), HttpStatus.UNAUTHORIZED);
  }

  //TODO Need refactor
  @ExceptionHandler({ExpiredJwtException.class, UnsupportedJwtException.class, MalformedJwtException.class, SignatureException.class, IllegalArgumentException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<Object> handleInvalidBearerTokenException(Exception ex) {
    logger.error(ex.getMessage(), ex);
    return buildResponse(
      List.of(new ValidationError(
        "alert", "The access token provided is expired, revoked, malformed, or invalid for other reasons.")), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(InsufficientAuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<Object> handleInsufficientAuthenticationException(InsufficientAuthenticationException ex) {
    logger.error(ex.getMessage(), ex);
    return buildResponse(
      List.of(new ValidationError(
        "alert", "Login credentials are missing.")), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AccountStatusException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<Object> handleAccountStatusException(AccountStatusException ex) {
    logger.error(ex.getMessage(), ex);
    return buildResponse(
      List.of(new ValidationError(
        "alert", "User account is abnormal.")), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
    logger.error(ex.getMessage(), ex);
    return buildResponse(
      List.of(new ValidationError(
        "alert", "No permission.")), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Object> handleException(Exception ex) {
    logger.error(ex.getMessage(), ex);
    return buildResponse(
      List.of(new ValidationError(
        "alert", "Service unavailable. Unable to process your request. Please try again later"))
      , HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<Object> buildResponse(List<ValidationError> errors, HttpStatusCode statusCode) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    return new ResponseEntity(ObjectMapperUtils.writeValueAsString(errors), httpHeaders,
      statusCode);
  }
}