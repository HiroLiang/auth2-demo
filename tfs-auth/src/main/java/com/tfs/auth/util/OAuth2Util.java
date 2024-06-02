package com.tfs.auth.util;

import com.tfs.auth.model.oauth2.ClientSecret;
import com.tfs.auth.model.oauth2.GoogleClientSecret;
import com.tfs.auth.model.oauth2.GoogleOauthToken;
import com.tfs.auth.model.oauth2.GoogleUserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class OAuth2Util {

    private final Map<String, ClientSecret> clientSecrets;

    private final RestUtil restUtil;

    @Autowired
    public OAuth2Util(@Value("${oauth2.config.paths}") String configPaths, RestUtil restUtil) {
        this.restUtil = restUtil;
        this.clientSecrets = new HashMap<>();
        String[] paths = configPaths.split(",");

        // 遍歷 path ，若有新增要來這邊註冊新的對象
        for (String path : paths) {
            File file = FileUtil.getResourcesFile(path);
            String json = FileUtil.readAsString(file);

            // google oauth2
            if(path.contains("google")) {
                assert false;
                clientSecrets.put("google", new GoogleClientSecret(json));
            }

        }
        log.info("Initialed secrets : {}", clientSecrets);
    }

    public String startGoogleOauth2() {
        GoogleClientSecret clientSecret = (GoogleClientSecret) clientSecrets.get("google");
        String uuid = UUID.randomUUID().toString();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(clientSecret.getAuthUri())
                .queryParam("client_id", clientSecret.getClientId())
                .queryParam("nonce", uuid)
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", clientSecret.getRedirectUris().getFirst())
                .queryParam("scope", "https://www.googleapis.com/auth/userinfo.email");

        return builder.build().toUriString();
    }

    public GoogleOauthToken getGoogleAuthToken(String code) {
        GoogleClientSecret clientSecret = (GoogleClientSecret) clientSecrets.get("google");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("client_id", clientSecret.getClientId());
        map.add("client_secret", clientSecret.getClientSecret());
        map.add("redirect_uri", clientSecret.getRedirectUris().getFirst());
        map.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        return restUtil.post(clientSecret.getTokenUri(), entity, GoogleOauthToken.class);
    }

    public GoogleUserData getGoogleUserInfo(String idToken) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/tokeninfo")
                .queryParam("id_token", idToken);

        String url = builder.build().toUriString();

        return restUtil.get(url, GoogleUserData.class);
    }

}
