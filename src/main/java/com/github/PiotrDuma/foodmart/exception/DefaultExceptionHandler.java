package com.github.PiotrDuma.foodmart.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
class DefaultExceptionHandler {
  private final Clock clock;

  public DefaultExceptionHandler(Clock clock) {
    this.clock = clock;
  }

  @ExceptionHandler({NoHandlerFoundException.class})
  private ResponseEntity<ExceptionDto> handleNoHandlerFoundException(HttpServletRequest request) {
    ExceptionDto exceptionDto = new ExceptionDto(
        request.getRequestURI(),
        "The requested URL does not exist",
        HttpStatus.NOT_FOUND.value(),
        LocalDateTime.now(clock)
    );
    return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({ResourceNotFoundException.class})
  private ResponseEntity<ExceptionDto> handleResourceNotFoundException(Exception ex,
      HttpServletRequest request) {
    ExceptionDto exceptionDto = new ExceptionDto(
        request.getRequestURI(),
        ex.getMessage(),
        HttpStatus.NOT_FOUND.value(),
        LocalDateTime.now(clock)
    );
    return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({Exception.class})
  private ResponseEntity<ExceptionDto> handleDefaultException(HttpServletRequest request) {
    ExceptionDto exceptionDto = new ExceptionDto(
        request.getRequestURI(),
        "Something went wrong",
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        LocalDateTime.now(clock)
    );
    return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private record ExceptionDto(
      String requestURL,
      String errorMessage,
      int statusCode,
      LocalDateTime timestamp
  ) {
  }
}