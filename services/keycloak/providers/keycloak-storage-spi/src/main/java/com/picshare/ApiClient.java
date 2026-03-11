package com.picshare;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.keycloak.component.ComponentModel;
import org.keycloak.http.simple.SimpleHttp;
import org.keycloak.http.simple.SimpleHttpRequest;
import org.keycloak.http.simple.SimpleHttpResponse;
import org.keycloak.models.KeycloakSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picshare.util.Credential;
import com.picshare.util.ThrowingFunction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiClient {

  private final SimpleHttp simpleHttp;
  private final String baseUrl;
  private final ObjectMapper objectMapper;

  public ApiClient(KeycloakSession session, ComponentModel model){
    this.simpleHttp = SimpleHttp.create(session);
    this.baseUrl = model.get(PicshareUserStorageProviderFactory.USER_API_BASE_URL);
    this.objectMapper = new ObjectMapper();
  }

  public PicshareUser searchUsers(String key, String toSearch){
    String url = String.format("%s/users/auth/", baseUrl);
    return searchUsersRequest(url, key, toSearch);
  }

  public PicshareUser getUserById(String id){
    return searchUsers("id", id);
  }

  public PicshareUser getUserByUsername(String username){
    return searchUsers("username", username);
  }
  public PicshareUser getUserByEmail(String email){
    return searchUsers("email", email);
  }

  public boolean verifyCredentials(String externalId, Credential credential) {
    String url = String.format("%s/users/auth/%s/credentials/verify", this.baseUrl, externalId);
    SimpleHttpRequest request = simpleHttp.doPost(url).json(credential);
    return handleRequestNoContentResponse(request);
  }

  public boolean updateCredentials(String externalId, Credential credential) {
    String url = String.format("%s/users/auth/%s/credentials/update", this.baseUrl, externalId);
    SimpleHttpRequest request = simpleHttp.doPost(url).json(credential);
    return handleRequestNoContentResponse(request);
  }

  public PicshareUser addUser(PicshareUser user){
    String url = String.format("%s/users/auth/create", this.baseUrl);
    SimpleHttpRequest request = simpleHttp.doPost(url).json(user);
    return handleRequest(request, response -> objectMapper.readValue(response.asString(), PicshareUser.class));
  }

  public boolean removeUser(String id){
    String url = String.format("%s/users/auth/remove", this.baseUrl);
    SimpleHttpRequest request = simpleHttp.doDelete(url).param("id", id);
    return handleRequestNoContentResponse(request);
  }

  private PicshareUser searchUsersRequest(String url, String key, String toSearch) {
    SimpleHttpRequest request = prepareGetRequest(url);
    if(key != null){
      request.param("key", key);
    }
    if (toSearch != null){
      request.param("value", toSearch);
    }
    return handleRequest(request, response -> objectMapper.readValue(response.asString(), PicshareUser.class));
  }

  private <T> T handleRequest(SimpleHttpRequest request, ThrowingFunction<SimpleHttpResponse, T, IOException> responseFunction){
    AtomicReference<T> result = new AtomicReference<>();
    try (SimpleHttpResponse response = request.asResponse()) {
      if (response.getStatus() >= 300) {
        log.error("Error response from server: {}, error: {}, url: {}", response.getStatus(), response.asString(), request.getUrl());
      } else {
        result.set(responseFunction.apply(response));
      }
    } catch (IOException e){
      log.error("Exception during calling url %s: %s".formatted(request.getUrl(), e.getMessage()));
    }
    return result.get();
  }

  private SimpleHttpRequest prepareGetRequest(String url) {
    return simpleHttp.doGet(url).acceptJson();
  }


  private boolean handleRequestNoContentResponse(SimpleHttpRequest request){
    return Optional.ofNullable(handleRequest(request, response -> response.getStatus() == 204))
      .orElse(false);
  }

}
