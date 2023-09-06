package com.zerobase.nomorebuy.member.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String userId;

  private String account;

  private LocalDateTime accountModifiedDate;

  private String openKaKaoTalkURL;

  private String kakaoId;

  private LocalDateTime signupDateTime;

  private LocalDateTime withdrawalDate;


  @ElementCollection
  @Builder.Default
  private List<String> roles = new ArrayList<>();

}
