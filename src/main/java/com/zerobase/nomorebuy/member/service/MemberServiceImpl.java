package com.zerobase.nomorebuy.member.service;

import com.zerobase.nomorebuy.global.ResponseStatus;
import com.zerobase.nomorebuy.global.exception.MemberErrorCode;
import com.zerobase.nomorebuy.global.exception.MemberException;
import com.zerobase.nomorebuy.global.security.JwtTokenProvider;
import com.zerobase.nomorebuy.member.domain.Member;
import com.zerobase.nomorebuy.member.domain.SignInDto;
import com.zerobase.nomorebuy.member.domain.SignUpDto.Request;
import com.zerobase.nomorebuy.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {

  private final MemberJpaRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public Member signUp(Request request) {
    log.info("[Sign Up]");

    if (memberRepository.findByKakaoId(request.getKakaoId())
        .isPresent()) {
      throw new MemberException(MemberErrorCode.ALREADY_EXIST_MEMBER);
    }

    return memberRepository.save(request.toEntity());
  }

  @Override
  public UserDetails loadUserByUsername(String memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));
  }

  public SignInDto.Response signIn(String kakaoId) {
    Member member = memberRepository.findByKakaoId(kakaoId)
        .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

    return SignInDto.Response.builder()
        .token(jwtTokenProvider.generateToken(member.getId(), member.getRoles()))
        .status(ResponseStatus.SUCCESS)
        .build();
  }
}
