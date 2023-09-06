package com.zerobase.nomorebuy.member.service;

import com.zerobase.nomorebuy.global.exception.MemberErrorCode;
import com.zerobase.nomorebuy.global.exception.MemberException;
import com.zerobase.nomorebuy.member.domain.Member;
import com.zerobase.nomorebuy.member.domain.MemberDto;
import com.zerobase.nomorebuy.member.domain.SignUpDto.Request;
import com.zerobase.nomorebuy.member.repository.MemberJpaRepository;
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

  private final MemberJpaRepository memberRepository;

  @Override
  @Transactional
  public Member signUp(Request request) {

    log.info("[Sign Up]");
    if (memberRepository.existsByUserId(request.getUserId())) {
      throw new MemberException(MemberErrorCode.ALREADY_EXIST_USERNAME);
    }

    if (memberRepository.existsByKakaoId(request.getKakaoId())) {
      throw new MemberException(MemberErrorCode.ALREADY_EXIST_MEMBER);
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
    Member member = memberRepository.findByKakaoId(kakaoId)
        .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

    return member;
  }
}
