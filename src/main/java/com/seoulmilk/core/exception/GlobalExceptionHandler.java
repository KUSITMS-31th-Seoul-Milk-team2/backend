package com.seoulmilk.core.exception;

import com.seoulmilk.core.presentation.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
    log.error("ERROR ::: [AllException] ", ex);
    ErrorResponse errorResponse = ErrorResponse.createErrorResponse().statusCode(500).exception(ex).build();
    return ResponseEntity.internalServerError().body(errorResponse);
  }

  @ExceptionHandler(DomainException.class)
  public final ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
    HttpStatus httpStatus = Optional.ofNullable(ex.getHttpStatus()).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    log.error("ERROR ::: [DomainException] ", ex);
    ErrorResponse errorResponse = ErrorResponse.createDomainErrorResponse().statusCode(httpStatus.value()).exception(ex).build();
    return ResponseEntity.status(httpStatus).body(errorResponse);
  }
}
