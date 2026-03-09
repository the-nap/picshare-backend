package com.picshare;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.storage.adapter.AbstractUserAdapter;

public class PicshareUserAdapter extends AbstractUserAdapter{

  private final PicshareUser user;

  public PicshareUserAdapter(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel, PicshareUser user) {
    super(session, realm, storageProviderModel);
    this.user = user;
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public SubjectCredentialManager credentialManager() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'credentialManager'");
  }

  
}
