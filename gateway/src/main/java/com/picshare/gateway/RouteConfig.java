package com.picshare.gateway;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ServiceConfiguration.class)
public class RouteConfig {

  private final ServiceConfiguration serviceConfig;
  private final RouteLocatorBuilder builder;

  @Bean
  public RouteLocator dynamicRoutes() {
    RouteLocatorBuilder.Builder routes = builder.routes();

    serviceConfig.getServices().forEach((key,value) -> {
      routes
        .route(key, route -> route
            .path(value.getPath())
            .filters(filter -> filter
              .stripPrefix(1)
              .circuitBreaker(config -> config
                .setName(key)
                .setFallbackUri(value.getFallback())))
            .uri(value.getUri()));
    });
    return routes.build();
  }
}
