package com.zerobase.nomorebuy.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtTokenProvider jwtTokenProvider;

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic().disable()
        .csrf().disable()

        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()

        .authorizeRequests()
        .antMatchers("/").permitAll()

        .antMatchers("**exception**").permitAll()

        .anyRequest().hasRole("USER")

        .and()
        .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
        .and()
        .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())

        .and()
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class);

    http.authorizeRequests()
        .antMatchers(
            "/")
        .permitAll();

  }

  @Override
  public void configure(WebSecurity webSecurity) {
    webSecurity.ignoring().antMatchers("swagger");
  }
}

