package com.zerobase.nomorebuy.member.service;

import com.zerobase.nomorebuy.global.exception.MemberErrorCode;
import com.zerobase.nomorebuy.global.exception.MemberException;
import com.zerobase.nomorebuy.global.kakaoLogin.KakaoLoginService;
import com.zerobase.nomorebuy.global.kakaoLogin.KakaoProfile;
import com.zerobase.nomorebuy.member.domain.Member;
import com.zerobase.nomorebuy.member.domain.MemberDto;
import com.zerobase.nomorebuy.member.domain.SignUpDto.Request;
import com.zerobase.nomorebuy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {

  private final MemberRepository memberRepository;
  private final KakaoLoginService kakaoLoginService;

  @Override
  @Transactional
  public Member signUp(Request request) {
    log.info("[Sign Up] kakaoAPI : 유저 정보 가져오기");
    KakaoProfile kakaoProfile = kakaoLoginService.getKakaoProfile(request.getKakaoOAuth().getAccessToken());
    if (kakaoProfile == null) {
      throw new RuntimeException("유저 정보 가져오기 실패");
    }

    if (kakaoProfile.getKakaoAccount().getEmail() == null) {
      log.error("이메일 없음. 이메일 동의 여부 확인 필요.");
      kakaoLoginService.kakaoUnlink(request.getKakaoOAuth().getAccessToken());
      throw new RuntimeException("토큰 연결 해제");
    }

    request.setKakaoId(String.valueOf(kakaoProfile.getId()));


    if (memberRepository.existsByUserId(request.getUserId())) {
      throw new MemberException(MemberErrorCode.ALREADY_EXIST_USERNAME);
    }

    if (memberRepository.existsByKakaoId(request.getKakaoId())) {
      throw new MemberException(MemberErrorCode.ALREADY_EXIST_MEMBER);
    }

    if (memberRepository.existsByEmail(request.getEmail())) {
      throw new MemberException(MemberErrorCode.ALREADY_REGISTERED_EMAIL);
    }

    return memberRepository.save(request.toEntity());
  }

  @Override
  public UserDetails loadUserByUsername(String memberId) {
    Member member = memberRepository.findByUserId(memberId)
        .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

    return MemberDto.builder()
        .Id(member.getId())
        .email(member.getEmail())
        .userId(member.getUserId())
        .account(member.getAccount())
        .accountModifiedDate(member.getAccountModifiedDate())
        .openKaKaoTalkURL(member.getOpenKaKaoTalkURL())
        .kakaoId(member.getKakaoId())
        .signupDateTime(member.getSignupDateTime())
        .build();
  }

  public Member signIn(String kakaoId) {
    return memberRepository.findByKakaoId(kakaoId)
        .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));
  }
}
