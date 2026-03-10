package com.picshare;

import java.util.function.Function;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

public class PicshareUserStorageProvider implements 
  UserStorageProvider,
  UserLookupProvider{

  private final KeycloakSession session;
  private final ComponentModel model;
  private final ApiClient apiClient;
  
  private final UserModelTransaction transaction = new UserModelTransaction();

  public PicshareUserStorageProvider(KeycloakSession session, ComponentModel model, ApiClient apiClient){
    this.session = session;
    this.model = model;
    this.apiClient = apiClient;
    session.getTransactionManager().enlist(transaction);
  }

  @Override
  public void close() {
  }

  @Override
  public UserModel getUserById(RealmModel realm, String id) {
    UserModel adapted = transaction.findUser(id);
    if (adapted == null){
      adapted = findUser(realm, id, apiClient::getUserById);
    }
    return adapted;
  }

  @Override
  public UserModel getUserByUsername(RealmModel realm, String username) {
    UserModel adapted = transaction.findUser(username);
    if (adapted == null){
      adapted = findUser(realm, username, apiClient::getUserByUsername);
    }
    return adapted;

  }

  @Override
  public UserModel getUserByEmail(RealmModel realm, String email) {
    UserModel adapted = transaction.findUser(email);
    if (adapted == null){
      adapted = findUser(realm, email, apiClient::getUserByEmail);
    }
    return adapted;
  }

  private UserModel findUser(RealmModel realm, String value, Function<String, PicshareUser> fnFindUser){
    UserModel adapted = null;
    PicshareUser user = fnFindUser.apply(value);
    if(user != null) {
      adapted = new PicshareUserAdapter(session, realm, model, user);
      transaction.addUser(adapted);
    }
    return adapted;
  }
  
}
