package com.seoulmilk.auth.infrastructure.jwt;

import com.seoulmilk.auth.exception.AuthenticationErrorCode;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.value.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private JwtProperties.TokenConfig tokenConfig;

    @Mock
    private Emp emp;

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        lenient().when(jwtProperties.getSecretKey()).thenReturn("test-secret-key-1234567890-1234567890");
        lenient().when(jwtProperties.getIssuer()).thenReturn("seoulmilk");
        lenient().when(jwtProperties.getAccess()).thenReturn(tokenConfig);
        lenient().when(jwtProperties.getRefresh()).thenReturn(tokenConfig);
        lenient().when(tokenConfig.getExpiration()).thenReturn(3600L);
        lenient().when(jwtProperties.getAccessExpiration()).thenReturn(3600L);
        lenient().when(jwtProperties.getRefreshExpiration()).thenReturn(86400L);
        lenient().when(tokenConfig.getPrefix()).thenReturn("Bearer ");
        lenient().when(emp.getId()).thenReturn(1L);
        lenient().when(emp.getRole()).thenReturn(Role.ADMIN);
        jwtTokenProvider = new JwtTokenProvider(jwtProperties);
    }


    @Test
    @DisplayName("액세스 토큰 발급 성공")
    void provideAccessToken_success() {
        // When
        String accessToken = jwtTokenProvider.provideAccessToken(emp);
        Claims claims = jwtTokenProvider.getPayload(accessToken);

        // Then
        assertEquals("access", claims.get("type", String.class));
        assertEquals("seoulmilk", claims.getIssuer());
        assertEquals(Long.toString(1L), claims.get("sub", String.class));
    }

    @Test
    @DisplayName("만료된 토큰 검증 시 예외 발생")
    void  validate_expiredToken() {
        // Given
        when(jwtProperties.getAccess().getExpiration()).thenReturn(-1L);

        // When
        String expiredToken = jwtTokenProvider.provideAccessToken(emp);

        // Then
        assertThrows(
            AuthenticationErrorCode.EXPIRED_TOKEN.toException().getClass(),
            () -> jwtTokenProvider.getPayload(expiredToken)
        );
    }

    @Test
    @DisplayName("잘못된 서명 검증 시 예외 발생")
    void validateInvalidSignature() {
        // Given
        String invalidToken = Jwts.builder()
                .claims(Map.of("type", "access"))
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(Keys.hmacShaKeyFor("wrong-test-secret-key-1234567890-1234567890".getBytes()))
                .compact();

        // When & Then
        assertThrows(
                AuthenticationErrorCode.INVALID_SIGNATURE.toException().getClass(),
                () -> jwtTokenProvider.getPayload(invalidToken)
        );
    }

    @Test
    @DisplayName("잘못된 토큰 형식 검증 시 예외 발생")
    void validateMalformedToken() {
        // Given
        String malformedToken = "invalid.token.string";

        // When & Then
        assertThrows(
                AuthenticationErrorCode.NOT_MATCH_TOKEN_FORMAT.toException().getClass(),
                () -> jwtTokenProvider.getPayload(malformedToken)
        );
    }

    @Test
    @DisplayName("정의되지 않은 예외 발생 시 처리")
    void validateUndefinedError() {
        // Given
        String invalidToken = "invalid.token";

        // When & Then
        assertThrows(
                AuthenticationErrorCode.NOT_DEFINE_TOKEN.toException().getClass(),
                () -> jwtTokenProvider.getPayload(invalidToken)
        );
    }
}
