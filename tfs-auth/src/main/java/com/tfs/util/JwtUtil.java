package com.tfs.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;

    private final SecureDigestAlgorithm<SecretKey, SecretKey> ALGORITHM = Jwts.SIG.HS256;

    public JwtUtil(@Value("${tfs.auth.jwt.key}") String key) {
        this.secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateToken(String subject) {
        log.debug("generating token... subject: {}", subject);

        Date issueAt = new Date();
        Date expiration = new Date(issueAt.getTime() + 1000 * 60 * 60 * 24);

        return Jwts.builder()
                .header().add("typ", "JWT").add("alg", "HS256")
                .and().issuedAt(issueAt).expiration(expiration)
                .subject(subject).signWith(secretKey, ALGORITHM).compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> jws = decodeToken(token);

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public Jws<Claims> decodeToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    }

    public static void main(String[] args) {

    }

}
