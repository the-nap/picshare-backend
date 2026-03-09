package com.picshare;

import org.keycloak.component.ComponentModel;
import org.keycloak.http.simple.SimpleHttp;
import org.keycloak.models.KeycloakSession;

public class ApiClient {

  private final SimpleHttp simpleHttp;
  private final String baseUrl;

  public ApiClient(KeycloakSession session, ComponentModel model){
    this.simpleHttp = SimpleHttp.create(session);
    this.baseUrl = model.get(PicshareUserStorageProviderFactory.USER_API_BASE_URL);
  }
  
}
