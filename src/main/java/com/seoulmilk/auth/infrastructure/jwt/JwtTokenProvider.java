package com.seoulmilk.auth.infrastructure.jwt;

import com.seoulmilk.auth.application.TokenProvider;
import com.seoulmilk.auth.exception.AuthenticationErrorCode;
import com.seoulmilk.emp.domain.entity.Emp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
@Log4j2
public class JwtTokenProvider implements TokenProvider {
    JwtProperties jwtProperties;
    private final SecretKey SECRET_KEY;

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        ACCESS("access"), REFRESH("refresh");
        private final String type;
    }

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.SECRET_KEY = Keys.hmacShaKeyFor(
                jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public String provideAccessToken(Emp emp) {
        return createToken(
                emp,
                Type.ACCESS,
                jwtProperties.getAccess().getExpiration()
        );
    }

    public String provideRefreshToken(Emp emp) {
        return createToken(
                emp,
                Type.REFRESH,
                jwtProperties.getRefresh().getExpiration()
        );
    }

    public Claims getPayload(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException e) {
            log.error("Invalid signature", e);
            throw AuthenticationErrorCode.INVALID_SIGNATURE.toException();
        } catch (ExpiredJwtException e) {
            log.error("Expired token", e);
            throw AuthenticationErrorCode.EXPIRED_TOKEN.toException();
        } catch (MalformedJwtException e) {
            log.error("Invalid token format", e);
            throw AuthenticationErrorCode.NOT_MATCH_TOKEN_FORMAT.toException();
        } catch (Exception e) {
            log.error("Failed to parse token", e);
            throw AuthenticationErrorCode.NOT_DEFINE_TOKEN.toException();
        }
    }

    private String createToken(Emp emp, Type type, long expiration) {
        Date expiryDate;
        Map<String, ?> claims;
        if (type.equals(Type.ACCESS)) {
            expiryDate = Date.from(Instant.now().plus(expiration, ChronoUnit.SECONDS));
            claims = Map.of(
                    "iss", jwtProperties.getIssuer(),
                    "sub", emp.getId().toString(),
                    "type", type.getType()
            );

        } else {
            expiryDate = Date.from(Instant.now().plus(expiration, ChronoUnit.DAYS));
            claims = Map.of(
                    "type", type.getType()
            );
        }

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }
}
