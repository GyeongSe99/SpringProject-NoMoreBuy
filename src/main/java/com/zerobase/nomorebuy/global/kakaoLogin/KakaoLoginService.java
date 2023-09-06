package com.zerobase.nomorebuy.global.kakaoLogin;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoLoginService {

  private final String KAKAO_TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
  private final String KAKAO_USERINFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";
  private final String KAKAO_UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";

  private final RestTemplate restTemplate;

  @Value("${kakaoAPI.restApiKey}")
  private String restApiKey;
  @Value("${kakaoAPI.redirectUrl}")
  private String kakaoRedirectUrl;

  public RetKakaoOAuth getKakaoTokenInfo(String code) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "authorization_code");
    params.add("client_id", restApiKey);
    params.add("redirect_uri", kakaoRedirectUrl);
    params.add("code", code);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
    ResponseEntity<RetKakaoOAuth> response = restTemplate.postForEntity(KAKAO_TOKEN_REQUEST_URL, request,
        RetKakaoOAuth.class);

    if (response.getStatusCode() == HttpStatus.OK) {
      return response.getBody();
    }

    throw new RuntimeException("getToken status not ok");
  }

  public KakaoProfile getKakaoProfile(String kakaoAccessToken) {
    log.info("[KakaoLoginService : getKakaoProfile]");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.set("Authorization", "Bearer " + kakaoAccessToken);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
    try {

      ResponseEntity<KakaoProfile> response = restTemplate.postForEntity(KAKAO_USERINFO_REQUEST_URL,
          request, KakaoProfile.class);

      if (response.getStatusCode() == HttpStatus.OK) {
        log.info("body : " + response.getBody());
        return response.getBody();
      }

    } catch (Exception e) {
      log.error(e.toString());
      throw new RuntimeException();
    }
    throw new RuntimeException();
  }

  public void kakaoUnlink(String kakaoAccessToken) {
    log.info("[KakaoLoginService : kakaoUnlink]");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.set("Authorization", "Bearer " + kakaoAccessToken);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);

    ResponseEntity<String> response = restTemplate.postForEntity(KAKAO_UNLINK_URL,
        request, String.class);

    if (response.getStatusCode() == HttpStatus.OK) {
      log.info("unlink " + response.getBody());
      return;
    }

    throw new RuntimeException();
  }
}
