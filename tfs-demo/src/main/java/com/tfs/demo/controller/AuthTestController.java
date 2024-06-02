package com.tfs.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfs.auth.model.TokenData;
import com.tfs.auth.model.exception.BaseException;
import com.tfs.auth.model.oauth2.GoogleOauthToken;
import com.tfs.auth.model.oauth2.GoogleUserData;
import com.tfs.auth.util.JwtUtil;
import com.tfs.auth.util.OAuth2Util;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/auth")
public class AuthTestController {

    private final OAuth2Util oAuth2Util;

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthTestController(OAuth2Util oAuth2Util, JwtUtil jwtUtil) {
        this.oAuth2Util = oAuth2Util;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/google/start")
    public ResponseEntity<String> googleOauthStart() {
        return ResponseEntity.ok(oAuth2Util.startGoogleOauth2());
    }

    @GetMapping("/google")
    public ResponseEntity<String> googleOauthGetToken(@RequestParam String code, @RequestParam String scope, HttpServletResponse response) {
        log.info("Send token to google with code : {} and scope : {}", code, scope);
        GoogleOauthToken token = oAuth2Util.getGoogleAuthToken(code);
        GoogleUserData userInfo = oAuth2Util.getGoogleUserInfo(token.getId_token());
        TokenData tokenData = new TokenData(userInfo.getEmail());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jwtToken = jwtUtil.generateToken(objectMapper.writeValueAsString(tokenData));
            response.addHeader("JwtToken", jwtToken);
            return ResponseEntity.ok("<script>window.location.href = 'http://localhost:5173/#/?JwtToken=" + jwtToken + "'</script>");
        } catch (JsonProcessingException e) {
            throw new BaseException("Generate token failed.");
        }

    }
}
