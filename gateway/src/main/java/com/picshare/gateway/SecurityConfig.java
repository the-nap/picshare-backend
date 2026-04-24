package com.picshare.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

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
          .pathMatchers("/media/**").permitAll()
          .pathMatchers("/eureka/**").permitAll() //temporary
          .anyExchange().authenticated()
          )
      .csrf((csrf) -> csrf.disable())
      .oauth2ResourceServer(oauth2 -> oauth2
          .jwt(Customizer.withDefaults()));

    return http.build();
  }

}
