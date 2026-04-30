package com.picshare.post_service.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
          .requestMatchers("/post/feed/posts").permitAll()
          .requestMatchers("/post/feed/connection").permitAll()
          .anyRequest().authenticated())
      .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
      .build();
  }

  
}
