package com.picshare;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
public class PicshareUserAdapter extends AbstractUserAdapter{
@Slf4j

  private final PicshareUser user;

  private boolean dirty;

  public PicshareUserAdapter(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel, PicshareUser user) {
    super(session, realm, storageProviderModel);
    this.storageId = new StorageId(storageProviderModel.getId(), user.getId());
    this.user = user;
    this.dirty = false;
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public void setUsername(String username){
    dirty = dirty || user.getUsername().equals(username);
    user.setUsername(username);
  }

  @Override
  public String getEmail(){
    return user.getEmail();
  }

  @Override
  public void setEmail(String email){
    dirty = dirty || user.getEmail().equals(email);
    user.setUsername(email);
  }

  @Override
  public SubjectCredentialManager credentialManager() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'credentialManager'");
  }

  
}
