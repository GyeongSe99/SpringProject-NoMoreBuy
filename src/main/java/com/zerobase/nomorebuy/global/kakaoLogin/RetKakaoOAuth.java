package com.zerobase.nomorebuy.global.kakaoLogin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RetKakaoOAuth {

  @JsonProperty("token_type")
  private String tokenType;

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("expires_in")
  private Integer expiresIn;

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("refresh_token_expires_in")
  private String refreshTokenExpiresIn;

  @JsonProperty("scope")
  private String scope;

}
