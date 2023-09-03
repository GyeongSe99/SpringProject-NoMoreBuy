package com.zerobase.nomorebuy.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberErrorCode {
  NOT_FOUND_MEMBER("해당 사용자를 찾을 수 없습니다."),
  ALREADY_EXIST_MEMBER("이미 존재하는 사용자 입니다.");


  private final String description;
}
