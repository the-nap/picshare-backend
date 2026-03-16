package com.picshare;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class PicshareUserAdapter extends AbstractUserAdapterFederatedStorage{

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
  public boolean isEnabled(){
    return true;
  }

  @Override
  public void setEnabled(boolean enabled){
    //TODO not yet requested
  }

  @Override
  public void setFirstName(String name){
    //TODO not yet requested
  }

  @Override
  public void setLastName(String name){
    //TODO not yet requested
  }

  @Override
  public SubjectCredentialManager credentialManager() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'credentialManager'");
  }

  @Override
  public void setAttribute(String name, List<String> values){
    log.info("setAttribute called with name: {}, values: {}", name, values);
    String value = values != null && !values.isEmpty() ? values.getFirst() : null;
    switch (name) {
      case UserModel.USERNAME -> setUsername(value);
      case UserModel.EMAIL -> setEmail(value);
      case UserModel.FIRST_NAME -> setFirstName(value);
      case UserModel.LAST_NAME -> setLastName(value);
      default -> super.setAttribute(name, values);
    }
  }

  @Override
  public void setSingleAttribute(String name, String value) {
    setAttribute(name, value == null ? null : List.of(value));
  }

  @Override
  public String getFirstAttribute(String name) {
    return getAttributeStream(name).findFirst().orElse(null);
  }

  @Override
  public Stream<String> getAttributeStream(String name) {
    List<String> values = getAttributes().get(name);
    return values != null && ! values.isEmpty() ? values.stream() : Stream.empty();
  }

  @Override
  public Map<String, List<String>> getAttributes(){
    MultivaluedHashMap<String, String> attributes = getFederatedStorage().getAttributes(realm, this.getId());
    if (attributes == null)
      attributes = new MultivaluedHashMap<>();
    attributes.add(UserModel.USERNAME, getUsername());
    attributes.add(UserModel.EMAIL, getEmail());
    return attributes;
  }

}
