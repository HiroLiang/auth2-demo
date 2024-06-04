package com.tfs.auth.model.oauth2;

import java.util.List;

public interface ClientSecret {
    String getClientId();
    String getClientSecret();
    List<String> getRedirectUris();
}
