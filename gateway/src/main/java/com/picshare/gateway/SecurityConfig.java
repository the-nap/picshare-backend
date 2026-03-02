package com.picshare.gateway;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class SecurityConfig {

  @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
  String jwkSetUri;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
    http
      .authorizeExchange((auth) -> auth
          .pathMatchers("/api/user/create").hasAuthority("SCOPE_create:users")
          .anyExchange().authenticated()
          )
      .oauth2ResourceServer(oauth2 -> oauth2
          .jwt(withDefaults()))
      .securityContextRepository(NoOpServerSecurityContextRepository.getInstance());

    return http.build();
  }

}
