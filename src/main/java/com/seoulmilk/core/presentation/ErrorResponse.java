package com.seoulmilk.core.presentation;

import com.seoulmilk.core.exception.DomainException;
import com.seoulmilk.core.exception.error.BaseErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse extends BaseResponse {

  private final int statusCode;

  private final String code;

  private final String message;

  @Builder(builderClassName = "CreateErrorResponse", builderMethodName = "createErrorResponse")
  public ErrorResponse(int statusCode, Exception exception) {
    super(false, LocalDateTime.now());
    this.statusCode = statusCode;
    this.code = exception.getClass().getSimpleName();
    this.message = exception.getMessage();
  }

  @Builder(builderClassName = "CreateDomainErrorResponse", builderMethodName = "createDomainErrorResponse")
  public ErrorResponse(int statusCode, DomainException exception) {
    super(false, LocalDateTime.now());
    this.statusCode = statusCode;
    this.code = exception.getCode();
    this.message = exception.getMessage();
  }

  @Builder(builderClassName = "CreateSwaggerErrorResponse", builderMethodName = "createSwaggerErrorResponse")
  public ErrorResponse(BaseErrorCode baseErrorCode) {
    super(false, LocalDateTime.now());
    this.statusCode = baseErrorCode.getHttpStatus().value();
    this.code = baseErrorCode.name();
    this.message = baseErrorCode.getMessage();
  }

  @Builder(builderClassName = "CreateValidationErrorResponse", builderMethodName = "createValidationErrorResponse")
  public ErrorResponse(int statusCode, MethodArgumentNotValidException exception) {
    super(false, LocalDateTime.now());
    this.statusCode = statusCode;
    this.code = exception.getClass().getSimpleName();
    this.message = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
  }
}
