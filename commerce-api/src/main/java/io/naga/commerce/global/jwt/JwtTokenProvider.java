package io.naga.commerce.global.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.naga.common.error.BusinessException;
import io.naga.common.error.ErrorCode;

@Component
public class JwtTokenProvider {

    private final String secret;
    private final long accessTokenExpirationMillis;

    public JwtTokenProvider(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.access-token-expiration-millis}") long accessTokenExpirationMillis
    ) {
        this.secret = secret;
        this.accessTokenExpirationMillis = accessTokenExpirationMillis;
    }

    public String createAccessToken(Integer userId) {
        SecretKey key = getSecretKey();
        return Jwts.builder()
            .signWith(key, Jwts.SIG.HS256)
            .header()
            .add("typ", "JWT")
            .add("alg", Jwts.SIG.HS256.getId())
            .and()
            .claim("id", userId)
            .expiration(Date.from(Instant.now().plusMillis(accessTokenExpirationMillis)))
            .compact();
    }

    public Integer getUserId(String accessToken) {
        try {
            return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .get("id", Integer.class);
        } catch (RuntimeException exception) {
            throw BusinessException.of(ErrorCode.UNAUTHORIZED, "invalid access token");
        }
    }

    private SecretKey getSecretKey() {
        return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }
}
