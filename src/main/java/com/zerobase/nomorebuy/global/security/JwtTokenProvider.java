package com.zerobase.nomorebuy.global.security;

import com.zerobase.nomorebuy.member.service.MemberServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

  private static final long TOKEN_VALID_MILLISECOND = 60 * 60 * 1000L; // 한 시간

  private final MemberServiceImpl memberService;

  @Value("${spring.jwt.secret}")
  private String secretKey;

  public String generateToken(String memberId, List<String> roles) {
    Claims claims = Jwts.claims().setSubject(memberId);
    claims.put("roles", roles);

    Date now = new Date();
    var expiredDate = new Date(now.getTime() + TOKEN_VALID_MILLISECOND);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now) // 토큰 생성 시간
        .setExpiration(expiredDate) // 토큰 만료 시간
        .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 암호화 알고리즘, 비밀키
        .compact();
  }

  // Jwt로 인증정보 조회
  public Authentication getAuthentication(String token) {
    log.info("[getAuthentication] 토큰 인증정보 조회");
    UserDetails userDetails = this.memberService.loadUserByUsername(this.getUserId(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUserId(String token) {
    return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest request) {
    return request.getHeader("X-AUTH-TOKEN");
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token);
      return !claimsJws.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }

  }


}
