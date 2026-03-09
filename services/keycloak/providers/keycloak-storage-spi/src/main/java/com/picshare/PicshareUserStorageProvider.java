package com.picshare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

import lombok.RequiredArgsConstructor;

public class PicshareUserStorageProvider implements 
  UserStorageProvider,
  UserLookupProvider{

  private final KeycloakSession session;
  private final ComponentModel model;
  
  private final UserModelTransaction transaction = new UserModelTransaction();

  public PicshareUserStorageProvider(KeycloakSession session, ComponentModel model){
    this.session = session;
    this.model = model;
    session.getTransactionManager().enlist(transaction);
  }

  @Override
  public void close() {
  }

  private UserModel findUser(RealmModel realm, String value, Function<String, PicshareUser> fnFindUser){
    UserModel adapted = null;
    PicshareUser user = fnFindUser.apply(value);
    if(user != null) {

    }
    return adapted;
  }

  @Override
  public UserModel getUserById(RealmModel realm, String id) {
    UserModel adapted = transaction.findUser(id);
    if (adapted == null){
      // TODO look in api
    }
    return adapted;
  }

  @Override
  public UserModel getUserByUsername(RealmModel realm, String username) {
    UserModel adapted = transaction.findUser(username);
    if (adapted == null){
      // TODO look in api
    }
    return adapted;

  }

  @Override
  public UserModel getUserByEmail(RealmModel realm, String email) {
    UserModel adapted = transaction.findUser(email);
    if (adapted == null){
      // TODO look in api
    }
    return adapted;
  }

  
}
