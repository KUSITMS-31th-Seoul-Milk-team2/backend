package com.seoulmilk.core.presentation;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RestResponse<T> extends BaseResponse {

  private final T data;

  public RestResponse(T data) {
    super(true, LocalDateTime.now());
    this.data = data;
  }
}
