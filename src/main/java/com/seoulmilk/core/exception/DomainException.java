package com.seoulmilk.core.exception;

import com.seoulmilk.core.exception.error.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends RuntimeException {

  private HttpStatus httpStatus;

  private String code;

  public DomainException(String message) {
    super(message);
  }

  public DomainException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public DomainException(HttpStatus httpStatus, BaseErrorCode<?> errorCode) {
    super(errorCode.getMessage());
    this.httpStatus = httpStatus;
    this.code = errorCode.name();
  }
}
