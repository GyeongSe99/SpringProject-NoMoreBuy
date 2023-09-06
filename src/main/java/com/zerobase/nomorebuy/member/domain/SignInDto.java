package com.zerobase.nomorebuy.member.domain;

import com.zerobase.nomorebuy.global.ResponseStatus;
import com.zerobase.nomorebuy.global.kakaoLogin.RetKakaoOAuth;
import java.time.LocalDateTime;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class SignInDto {

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Request {

    private RetKakaoOAuth kakaoOAuth;
    private String email;
    private String userId;
    private String openKakaoURL;
    private String kakaoId;
    private String role;

    public Member toEntity() {
      return Member.builder()
          .email(email)
          .userId(userId)
          .account(null)
          .accountModifiedDate(null)
          .openKaKaoTalkURL(openKakaoURL)
          .kakaoId(kakaoId)
          .roles(Collections.singletonList("USER"))
          .signupDateTime(LocalDateTime.now())
          .withdrawalDate(null)
          .build();
    }
  }

  @Data
  @Builder
  public static class Response {

    private String token;
    private ResponseStatus status;

  }

}
