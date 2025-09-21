package com.agendasim.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    @InjectMocks
    private JwtTokenService jwtTokenService;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities("ROLE_USER")
                .build();
    }

    @Test
    void testGenerateToken() {
        // When
        String token = jwtTokenService.generateToken(userDetails);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void testGenerateRefreshToken() {
        // When
        String refreshToken = jwtTokenService.generateRefreshToken(userDetails);

        // Then
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
        assertTrue(refreshToken.contains("."));
    }

    @Test
    void testExtractUsername() {
        // Given
        String token = jwtTokenService.generateToken(userDetails);

        // When
        String username = jwtTokenService.extractUsername(token);

        // Then
        assertEquals("test@example.com", username);
    }

    @Test
    void testExtractClaim() {
        // Given
        String token = jwtTokenService.generateToken(userDetails);

        // When
        String subject = jwtTokenService.extractClaim(token, Claims::getSubject);
        Date issuedAt = jwtTokenService.extractClaim(token, Claims::getIssuedAt);
        Date expiration = jwtTokenService.extractClaim(token, Claims::getExpiration);

        // Then
        assertEquals("test@example.com", subject);
        assertNotNull(issuedAt);
        assertNotNull(expiration);
        assertTrue(expiration.after(issuedAt));
    }

    @Test
    void testIsTokenValid() {
        // Given
        String token = jwtTokenService.generateToken(userDetails);

        // When
        boolean isValid = jwtTokenService.isTokenValid(token, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testIsTokenValidWithWrongUser() {
        // Given
        String token = jwtTokenService.generateToken(userDetails);
        UserDetails wrongUser = User.builder()
                .username("wrong@example.com")
                .password("password")
                .authorities("ROLE_USER")
                .build();

        // When
        boolean isValid = jwtTokenService.isTokenValid(token, wrongUser);

        // Then
        assertFalse(isValid);
    }


    @Test
    void testGetExpirationTimeInSeconds() {
        // When
        long expirationTime = jwtTokenService.getExpirationTimeInSeconds();

        // Then
        assertEquals(36000, expirationTime); // 10 horas em segundos
    }

    @Test
    void testExtractUsernameFromInvalidToken() {
        // Given
        String invalidToken = "invalid.token.here";

        // When & Then
        assertThrows(MalformedJwtException.class, () -> {
            jwtTokenService.extractUsername(invalidToken);
        });
    }


    @Test
    void testExtractUsernameFromTokenWithWrongSignature() {
        // Given
        Key wrongKey = Keys.hmacShaKeyFor("wrong-key-with-different-length-32-chars".getBytes());
        String tokenWithWrongSignature = io.jsonwebtoken.Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(wrongKey, io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();

        // When & Then
        assertThrows(io.jsonwebtoken.security.SignatureException.class, () -> {
            jwtTokenService.extractUsername(tokenWithWrongSignature);
        });
    }

    @Test
    void testTokenExpirationTime() {
        // Given
        String token = jwtTokenService.generateToken(userDetails);
        long currentTime = System.currentTimeMillis();
        
        // When
        Date expiration = jwtTokenService.extractClaim(token, Claims::getExpiration);
        long expirationTime = expiration.getTime();
        long timeUntilExpiration = expirationTime - currentTime;

        // Then
        // O token deve expirar em aproximadamente 10 horas (36000000 ms)
        assertTrue(timeUntilExpiration > 35900000); // Mais de 9h59min
        assertTrue(timeUntilExpiration < 36100000); // Menos de 10h01min
    }

    @Test
    void testRefreshTokenExpirationTime() {
        // Given
        String refreshToken = jwtTokenService.generateRefreshToken(userDetails);
        long currentTime = System.currentTimeMillis();
        
        // When
        Date expiration = jwtTokenService.extractClaim(refreshToken, Claims::getExpiration);
        long expirationTime = expiration.getTime();
        long timeUntilExpiration = expirationTime - currentTime;

        // Then
        // O refresh token deve expirar em aproximadamente 7 dias
        long sevenDaysInMs = TimeUnit.DAYS.toMillis(7);
        assertTrue(timeUntilExpiration > sevenDaysInMs - 60000); // Mais de 6d23h59min
        assertTrue(timeUntilExpiration < sevenDaysInMs + 60000); // Menos de 7d00h01min
    }

    @Test
    void testTokenContainsCorrectClaims() {
        // Given
        String token = jwtTokenService.generateToken(userDetails);

        // When
        String subject = jwtTokenService.extractClaim(token, Claims::getSubject);
        Date issuedAt = jwtTokenService.extractClaim(token, Claims::getIssuedAt);
        Date expiration = jwtTokenService.extractClaim(token, Claims::getExpiration);

        // Then
        assertEquals("test@example.com", subject);
        assertNotNull(issuedAt);
        assertNotNull(expiration);
        assertTrue(expiration.after(issuedAt));
        assertTrue(issuedAt.before(new Date()));
    }
}
