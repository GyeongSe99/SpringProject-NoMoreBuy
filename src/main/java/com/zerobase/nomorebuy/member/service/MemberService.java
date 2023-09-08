package com.zerobase.nomorebuy.member.service;

import com.zerobase.nomorebuy.member.domain.Member;
import com.zerobase.nomorebuy.member.domain.SignUpDto.Request;

public interface MemberService {

  Member signUp(Request request);

}
