package com.picshare;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.keycloak.models.AbstractKeycloakTransaction;
import org.keycloak.models.UserModel;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserModelTransaction extends AbstractKeycloakTransaction{

  private final List<UserModel> loadedUsers = new ArrayList<>();
  
  private final Consumer<UserModel> userConsumer;

  public UserModel findUser(@NonNull String value){
    return loadedUsers.stream()
      .filter(user -> user.getId().equals(value) || user.getUsername().equals(value) || user.getEmail().equalsIgnoreCase(value))
      .findFirst().orElse(null);
  }

  @Override
  protected void commitImpl() {
    loadedUsers.forEach(userConsumer);
  }

  @Override
  protected void rollbackImpl() {
    loadedUsers.clear();
  }

  public void addUser(UserModel adapted) {
    this.loadedUsers.add(adapted);
  }

  
}
