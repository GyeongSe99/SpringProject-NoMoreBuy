package com.zerobase.nomorebuy.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements
    org.springframework.security.web.AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    ObjectMapper objectMapper = new ObjectMapper();
    log.error("[CustomAuthenticationEntryPoint] commence : 인증 실패로 response.sendError 발생");

    EntryPointErrorResponse entryPointErrorResponse = new EntryPointErrorResponse();
    entryPointErrorResponse.setMsg("인증이 실패하였습니다.");

    response.setStatus(401);
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
    response.getWriter().write(objectMapper.writeValueAsString(entryPointErrorResponse));
  }
}
