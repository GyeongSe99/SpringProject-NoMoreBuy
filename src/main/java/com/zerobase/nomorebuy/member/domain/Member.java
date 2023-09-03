package com.zerobase.nomorebuy.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member implements UserDetails {

  @Id
  private String Id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String name;

  private String account;

  private LocalDateTime accountModifiedDate;

  private String openKaKaoTalkURL;

  private String kakaoId;

  private LocalDateTime signupDate;

  private LocalDateTime withdrawalDate;

  @ElementCollection
  @Builder.Default
  private List<String> roles = new ArrayList<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles
        .stream().map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public String getPassword() {
    return null;
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public String getUsername() {
    return this.Id;
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public boolean isEnabled() {
    return true;
  }
}
