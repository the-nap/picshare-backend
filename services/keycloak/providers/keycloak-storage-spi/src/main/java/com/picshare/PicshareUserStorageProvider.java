package com.picshare;

import java.util.function.Function;
import java.util.stream.Stream;

import com.picshare.util.Credential;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

public class PicshareUserStorageProvider implements 
  UserStorageProvider,
  UserLookupProvider,
  CredentialInputValidator,
  CredentialInputUpdater{

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

  @Override
  public boolean supportsCredentialType(String credentialType) {
    return credentialType.equals(PasswordCredentialModel.TYPE);
  }

  @Override
  public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
    return supportsCredentialType(credentialType);
  }

  @Override
  public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
    if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel cred)){
      return false;
    }
    return apiClient.verifyCredentials(StorageId.externalId(user.getId()), new Credential("password", cred.getChallengeResponse()));
  }

  @Override
  public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
    if(!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel cred))
      return false;

    return apiClient.updateCredentials(user.getId(), new Credential("password", cred.getChallengeResponse()));
  }

  @Override
  public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
  }

  @Override
  public Stream<String> getDisableableCredentialTypesStream(RealmModel realm, UserModel user) {
    return Stream.empty();
  }
  
}
