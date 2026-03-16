package com.picshare;

import java.util.List;

import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.UserStorageProviderFactory;

public class PicshareUserStorageProviderFactory implements UserStorageProviderFactory<PicshareUserStorageProvider>{

  public static final String PROVIDER_NAME = "picshare-user-storage";
  
  static final String USER_API_BASE_URL = "USER_SERVICE_API";
  static final String EDIT_MODE = "editMode";

  @Override
  public PicshareUserStorageProvider create(KeycloakSession session, ComponentModel model) {
    ApiClient apiClient = new ApiClient(session, model);
    return new PicshareUserStorageProvider(session, model, apiClient);
  }

  @Override
  public String getId() {
    return PROVIDER_NAME;
  }

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    return ProviderConfigurationBuilder.create()
      .property(USER_API_BASE_URL, "apiBaseUrl", "apiBaseUrlHelp", ProviderConfigProperty.STRING_TYPE, System.getenv(USER_API_BASE_URL), null)
      .property(EDIT_MODE, "editMode", "editModeHelp", ProviderConfigProperty.LIST_TYPE, UserStorageProvider.EditMode.READ_ONLY, List.of(UserStorageProvider.EditMode.READ_ONLY.name(), UserStorageProvider.EditMode.WRITABLE.name()))
      .build();
  }

  @Override
  public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) throws ComponentValidationException {
    if(config.getId() == null){
      config.setId(KeycloakModelUtils.generateShortId());
    }
  }
  
}
