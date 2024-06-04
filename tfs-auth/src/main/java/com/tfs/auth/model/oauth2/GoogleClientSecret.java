package com.tfs.auth.model.oauth2;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GoogleClientSecret implements ClientSecret{

    private String projectId;
    private String clientId;
    private String clientSecret;
    private List<String> redirectUris = new ArrayList<>();
    private String authUri;
    private String tokenUri;
    private String authProviderX509CertUrl;

    public GoogleClientSecret(String json) {
        JsonObject secret = JsonParser.parseString(json).getAsJsonObject();
        JsonObject web = secret.getAsJsonObject("web");

        this.projectId = web.get("project_id").getAsString();
        this.clientId = web.get("client_id").getAsString();
        this.clientSecret = web.get("client_secret").getAsString();
        for (JsonElement jsonElement : web.get("redirect_uris").getAsJsonArray()) {
            redirectUris.add(jsonElement.getAsString());
        }
        this.authUri = web.get("auth_uri").getAsString();
        this.tokenUri = web.get("token_uri").getAsString();
        this.authProviderX509CertUrl = web.get("auth_provider_x509_cert_url").getAsString();
    }
}
