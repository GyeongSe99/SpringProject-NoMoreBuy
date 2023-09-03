package com.zerobase.nomorebuy.global.kakaoLogin;

import lombok.Data;

@Data
public class KakaoProfile {

  private String id;
  private KakaoAccount kakao_account;

  @Data
  public static class KakaoAccount {

    private String email;
  }
}
