package com.picshare;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.keycloak.component.ComponentModel;
import org.keycloak.http.simple.SimpleHttp;
import org.keycloak.http.simple.SimpleHttpRequest;
import org.keycloak.http.simple.SimpleHttpResponse;
import org.keycloak.models.KeycloakSession;

import com.fasterxml.jackson.core.type.TypeReference;
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


  public PicshareUser getUserById(String id){
    List<PicshareUser> users = searchUsers("id", id, 0, 1, true);
    return users.isEmpty() ? null : users.getFirst();
  }

  public PicshareUser getUserByUsername(String username){
    List<PicshareUser> users = searchUsers("username", username, 0, 1, true);
    return users.isEmpty() ? null : users.getFirst();
  }

  public PicshareUser getUserByEmail(String email){
    List<PicshareUser> users = searchUsers("email", email, 0, 1, true);
    return users.isEmpty() ? null : users.getFirst();
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

  public List<PicshareUser> searchByUsername(String username, Integer first, Integer max){
    return searchUsers("username", username, first, max, false);
  }

  public List<PicshareUser> searchByEmail(String email, Integer first, Integer max){
    return searchUsers("email", email, first, max, false);
  }

  public List<PicshareUser> searchUsers(String key, String toSearch, Integer first, Integer max, boolean exactMatch){
    String url;
    if(exactMatch)
      url = String.format("%s/users/auth", baseUrl);
    else 
      url = String.format("%s/users/auth/search", baseUrl);
    return searchUsersRequest(url, key, toSearch, first, max);
  }

  private List<PicshareUser> searchUsersRequest(String url, String key, String toSearch, Integer first, Integer max) {
    SimpleHttpRequest request = prepareGetRequest(url);
    if(key != null)
      request.param("key", key);
    if (toSearch != null)
      request.param("value", toSearch);
    if (max != null && max > 1 && first != null){
      request.param("max", String.valueOf(max));
      request.param("first", String.valueOf(first));
    }
    return handleRequest(request, response -> response.asJson(new TypeReference<>() {}));
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
