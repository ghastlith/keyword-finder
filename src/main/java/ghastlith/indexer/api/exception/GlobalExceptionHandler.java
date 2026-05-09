package ghastlith.indexer.api.exception;

import static java.time.Instant.now;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Handler for exceptions that formats the information in a user readable way
 * that matches the spring default to return it to the request.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(BAD_REQUEST)
  @ResponseBody
  private ErrorResponse handleValidation(
      final MethodArgumentNotValidException exception,
      final HttpServletRequest request
  ) {
    final var timestamp = now();
    final var status = BAD_REQUEST.value();
    final var error = BAD_REQUEST.getReasonPhrase();
    final var message = exception.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(FieldError::getDefaultMessage)
        .collect(toUnmodifiableList());
    final var path = request.getRequestURI();

    return ErrorResponse.builder()
        .timestamp(timestamp)
        .status(status)
        .error(error)
        .message(message)
        .path(path)
        .build();
  }

}
