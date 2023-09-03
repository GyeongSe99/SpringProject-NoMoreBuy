package com.zerobase.nomorebuy.member.controller;

import com.zerobase.nomorebuy.global.kakaoLogin.KakaoCode;
import com.zerobase.nomorebuy.global.kakaoLogin.KakaoLoginService;
import com.zerobase.nomorebuy.global.kakaoLogin.KakaoProfile;
import com.zerobase.nomorebuy.global.kakaoLogin.RetKakaoOAuth;
import com.zerobase.nomorebuy.member.domain.Member;
import com.zerobase.nomorebuy.member.domain.SignInDto;
import com.zerobase.nomorebuy.member.domain.SignUpDto;
import com.zerobase.nomorebuy.member.domain.SignUpDto.Request;
import com.zerobase.nomorebuy.member.domain.SignUpDto.Response;
import com.zerobase.nomorebuy.member.service.MemberServiceImpl;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("member")
public class MemberController {

  private final KakaoCode kakaoCode;
  private final KakaoLoginService kakaoLoginService;
  private final MemberServiceImpl memberService;

  @GetMapping("/kakao/auth")
  public void getKaKaoAuthURL(HttpServletResponse response) throws IOException {
    log.info("KakaoAPI : 인증코드 받기");
    response.sendRedirect(kakaoCode.responseUrl());
  }

  @ResponseBody
  @GetMapping("/kakao")
  public RetKakaoOAuth getKaKaoAccessToken(@RequestParam String code) throws IOException {
    log.info("kakaoAPI : 액세스 토큰 받기");
    RetKakaoOAuth kakaoTokenInfo = kakaoLoginService.getKakaoTokenInfo(code);
    String token = kakaoTokenInfo.getAccess_token();
    log.info("Access token : " + token);
    return kakaoTokenInfo;
  }

  @PostMapping("/kakao/signup")
  public Response signUp(@RequestBody Request request) {
    String accessToken = request.getKakaoOAuth().getAccess_token();
    log.info("kakaoAPI : 유저 정보 가져오기");
    KakaoProfile kakaoProfile = kakaoLoginService.getKakaoProfile(accessToken);
    if (kakaoProfile == null) {
      throw new RuntimeException("유저 정보 가져오기 실패");
    }
    if (kakaoProfile.getKakao_account().getEmail() == null) {
      log.error("이메일 없음. 이메일 동의 여부 확인 필요.");
      kakaoLoginService.kakaoUnlink(accessToken);
      throw new RuntimeException();
    }

    log.info("회원가입 진행");
    Member signUp = memberService.signUp(request);
    if (signUp == null) {
      throw new RuntimeException("회원 가입이 실패하였습니다.");
    }

    log.info("회원가입에 성공하였습니다.");
    log.info("userID : " + signUp.getId());
    log.info("username : " + signUp.getName());

    return SignUpDto.Response.builder().member(signUp).build();
  }

  @GetMapping("/kakao/signIn/exception")
  public void exception() throws RuntimeException {
    throw new RuntimeException("접근이 금지되었습니다.");
  }

  @PostMapping("/kakao/signIn")
  public SignInDto.Response signIn(@RequestBody SignInDto.Request request) {
    log.info("[Sign in] 카카오 API로 로그인 진행 중");
    String accessToken = request.getKakaoOAuth().getAccess_token();
    KakaoProfile kakaoProfile = kakaoLoginService.getKakaoProfile(accessToken);
    if (kakaoProfile == null) {
      throw new RuntimeException("유저 정보 가져오기 실패");
    }

    return memberService.signIn(kakaoProfile.getId());
  }

}
