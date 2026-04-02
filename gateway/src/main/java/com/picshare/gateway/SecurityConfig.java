package com.picshare.gateway;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
    http
      .authorizeExchange((auth) -> auth
          .pathMatchers(HttpMethod.OPTIONS).permitAll()
          .pathMatchers("/").permitAll()
          .pathMatchers("/ui/**").permitAll()
          .pathMatchers("/api/user/create").hasAuthority("SCOPE_create:users")
          .anyExchange().authenticated()
          )
      .csrf((csrf) -> csrf.disable())
      .oauth2ResourceServer(oauth2 -> oauth2
          .jwt(withDefaults()))
      .securityContextRepository(NoOpServerSecurityContextRepository.getInstance());

    return http.build();
  }

}
