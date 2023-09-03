package com.zerobase.nomorebuy.member.domain;

import com.zerobase.nomorebuy.global.kakaoLogin.RetKakaoOAuth;
import java.time.LocalDateTime;
import java.util.Collections;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpDto {

  @Data
  @Builder
  public static class Request {

    private RetKakaoOAuth kakaoOAuth;
    private String email;
    private String nickname;
    private String openKakaoURL;
    private String kakaoId;

    public Member toEntity() {
      return Member.builder()
          .email(email)
          .name(nickname)
          .account(null)
          .accountModifiedDate(null)
          .openKaKaoTalkURL(openKakaoURL)
          .kakaoId(kakaoId)
          .roles(Collections.singletonList("USER"))
          .signupDate(LocalDateTime.now())
          .withdrawalDate(null)
          .build();
    }
  }

  @Data
  @Builder
  public static class Response {

    private Member member;
  }
}
