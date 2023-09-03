package com.zerobase.nomorebuy.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberException extends RuntimeException {

  private MemberErrorCode errorCode;
  private String errorMessage;

  public MemberException(MemberErrorCode errorCode) {
    this.errorCode = errorCode;
    this.errorMessage = errorCode.getDescription();
  }
}
