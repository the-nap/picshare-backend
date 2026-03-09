package com.picshare;

import java.util.List;

import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;

public class PicshareUserStorageProviderFactory implements UserStorageProviderFactory<PicshareUserStorageProvider>{

  public static final String PROVIDER_NAME = "picshare-user-storage";
  
  static final String USER_API_BASE_URL = "USER_SERVICE_API";

  @Override
  public PicshareUserStorageProvider create(KeycloakSession session, ComponentModel model) {
    return new PicshareUserStorageProvider(session, model);
  }

  @Override
  public String getId() {
    return PROVIDER_NAME;
  }

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    return ProviderConfigurationBuilder.create()
      .property(USER_API_BASE_URL, "apiBaseUrl", "apiBaseUrlHelp", ProviderConfigProperty.STRING_TYPE, System.getenv(USER_API_BASE_URL), null)
      .build();
  }

  @Override
  public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) throws ComponentValidationException {
    if(config.getId() == null){
      config.setId(KeycloakModelUtils.generateShortId());
    }
  }
  
}
