package com.zerobase.nomorebuy.global.kakaoLogin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoCode {

  private final String KAKAO_LOGIN_URL = "https://kauth.kakao.com/oauth/authorize?client_id=";
  @Value("${kakaoAPI.restApiKey}")
  private String restApiKey;
  @Value("${kakaoAPI.redirectUrl}")
  private String kakaoRedirectUrl;

  public String responseUrl() {
    String kakaoLoginUrl =
        KAKAO_LOGIN_URL + restApiKey + "&redirect_uri=" + kakaoRedirectUrl + "&response_type=code";
    return kakaoLoginUrl;
  }

}
