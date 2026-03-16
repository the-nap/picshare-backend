package com.picshare;

import java.util.ArrayList;
import java.util.List;

import org.keycloak.models.AbstractKeycloakTransaction;
import org.keycloak.models.UserModel;

import lombok.NonNull;

public class UserModelTransaction extends AbstractKeycloakTransaction{

  private final List<UserModel> loadedUsers = new ArrayList<>();

  public UserModel findUser(@NonNull String value){
    return loadedUsers.stream()
      .filter(user -> user.getId().equals(value) || user.getUsername().equals(value) || user.getEmail().equalsIgnoreCase(value))
      .findFirst().orElse(null);
  }

  @Override
  protected void commitImpl() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'commitImpl'");
  }

  @Override
  protected void rollbackImpl() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'rollbackImpl'");
  }

  public void addUser(UserModel adapted) {
    this.loadedUsers.add(adapted);
  }

  
}
