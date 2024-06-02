package com.tfs.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    public JwtUtil jwtUtil = new JwtUtil("1qaz2wsx1q2w3edc2w3e4rfv3e4r5tgb");

    @Test
    void generateToken() {
        String token = jwtUtil.generateToken("token unit test");
        assertNotNull(token);
    }

    @Test
    void decodeToken() {
        Jws<Claims> jws = jwtUtil.decodeToken(jwtUtil.generateToken("token unit test"));
        assertEquals("token unit test", jws.getPayload().getSubject());
        System.out.println(jws.getPayload().getIssuedAt());
        System.out.println(jws.getPayload().getExpiration());

    }

}