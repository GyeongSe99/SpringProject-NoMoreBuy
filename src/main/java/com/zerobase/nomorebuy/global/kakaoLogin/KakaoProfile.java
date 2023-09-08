package com.zerobase.nomorebuy.global.kakaoLogin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoProfile {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("kakao_account")
  private KakaoAccount kakaoAccount;

  @Data
  public static class KakaoAccount {

    private String email;
  }
}
