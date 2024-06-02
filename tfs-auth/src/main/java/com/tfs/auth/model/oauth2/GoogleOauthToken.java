package com.tfs.auth.model.oauth2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleOauthToken {

    private String access_token;
    private String expires_in;
    private String scope;
    private String token_type;
    private String id_token;

}
