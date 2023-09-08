package com.zerobase.nomorebuy.member.controller;

import com.zerobase.nomorebuy.global.ResponseStatus;
import com.zerobase.nomorebuy.global.exception.MemberException;
import com.zerobase.nomorebuy.global.kakaoLogin.KakaoLoginService;
import com.zerobase.nomorebuy.global.kakaoLogin.KakaoProfile;
import com.zerobase.nomorebuy.global.kakaoLogin.RetKakaoOAuth;
import com.zerobase.nomorebuy.global.security.JwtTokenProvider;
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
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("member")
public class MemberController {

  private final KakaoLoginService kakaoLoginService;
  private final MemberServiceImpl memberService;
  private final JwtTokenProvider jwtTokenProvider;

  @GetMapping("/kakao/auth")
  public void getKaKaoAuthURL(HttpServletResponse response) throws IOException {
    log.info("KakaoAPI : 인증코드 받기");
    response.sendRedirect(kakaoLoginService.responseUrl());
  }


  @ResponseBody
  @GetMapping("/kakao")
  public RetKakaoOAuth getKaKaoAccessToken(@RequestParam String code) throws IOException {
    log.info("kakaoAPI : 액세스 토큰 받기");
    RetKakaoOAuth kakaoTokenInfo = kakaoLoginService.getKakaoTokenInfo(code);
    String token = kakaoTokenInfo.getAccessToken();
    log.info("Access token : " + token);
    return kakaoTokenInfo;
  }

  @PostMapping("/kakao/signup")
  public Response signUp(@RequestBody Request request) {
    log.info("회원가입 진행");
    Member signUp = null;

    try {
      signUp = memberService.signUp(request);

    } catch (MemberException e) {
      // MemberException 발생 시 처리할 코드
      log.error("회원 가입 중 에러 발생: " + e.getMessage());
      // 여기서 예외 처리 로직을 추가하거나 필요에 따라 다른 예외를 던질 수 있습니다.
    }

    log.info("회원가입에 성공하였습니다.");

    return SignUpDto.Response.builder().member(signUp)
        .Id(signUp.getId())
        .status(ResponseStatus.SUCCESS).build();
  }

  @ApiIgnore
  @GetMapping("/kakao/signIn/exception")
  public void exception() throws RuntimeException {
    throw new RuntimeException("접근이 금지되었습니다.");
  }

  @PostMapping("/kakao/signIn")
  public SignInDto.Response signIn(@RequestBody SignInDto.Request request) {
    log.info("[Sign in] 카카오 API로 로그인 진행 중");
    String accessToken = request.getKakaoOAuth().getAccessToken();
    KakaoProfile kakaoProfile = kakaoLoginService.getKakaoProfile(accessToken);

    if (kakaoProfile == null) {
      throw new RuntimeException("유저 정보 가져오기 실패");
    }

    Member member = memberService.signIn(String.valueOf(kakaoProfile.getId()));

    return SignInDto.Response.builder()
        .token(jwtTokenProvider.generateToken(member.getUserId(), member.getRoles()))
        .status(ResponseStatus.SUCCESS)
        .build();
  }

}
