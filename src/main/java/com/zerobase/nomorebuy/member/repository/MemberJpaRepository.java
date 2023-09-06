package com.zerobase.nomorebuy.member.repository;

import com.zerobase.nomorebuy.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member, String> {

  Optional<Member> findByKakaoId(String kakaoId);

  Optional<Member> findByUserId(String userName);

  boolean existsByKakaoId(String s);

  boolean existsByUserId(String s);
}
