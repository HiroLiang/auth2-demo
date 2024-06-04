package com.tfs.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfs.auth.model.TokenData;
import com.tfs.auth.model.oauth2.GoogleOauthToken;
import com.tfs.auth.model.oauth2.GoogleUserData;
import com.tfs.auth.util.JwtUtil;
import com.tfs.auth.util.OAuth2Util;
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
    public ResponseEntity<String> googleOauthGetToken(@RequestParam String code, @RequestParam String scope) {
        log.info("User Login from google with code : {} and scope : {}", code, scope);
        try {
            // 以 Google redirect 帶的 code 取得權杖
            GoogleOauthToken token = oAuth2Util.getGoogleAuthToken(code);

            // 轉送權杖認證解碼為 user info
            GoogleUserData userInfo = oAuth2Util.getGoogleUserInfo(token.getId_token());

            // 製作自己的 Token Data ( 需要時可用 AOP 取得暫存的 User 資料 )
            TokenData tokenData = new TokenData(userInfo.getEmail());

            // 用 Jackson 將 Token Data 轉為字串用 js 轉導首頁
            ObjectMapper objectMapper = new ObjectMapper();
            String jwtToken = jwtUtil.generateToken(objectMapper.writeValueAsString(tokenData));

            return ResponseEntity.ok("<script>window.location.href = 'http://localhost:8080/tfs-demo/#/?JwtToken=" + jwtToken + "'</script>");
        } catch (JsonProcessingException e) {
            return ResponseEntity.ok("@Generate token failed.");
        } catch (Exception e) {
            return ResponseEntity.ok("@Google OAuth failed.");
        }

    }
}
