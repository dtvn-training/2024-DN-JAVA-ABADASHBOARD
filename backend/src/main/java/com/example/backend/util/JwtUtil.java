package com.example.backend.util;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.backend.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {

    private final SecretKey secretKey;

    @Value("${jwt.valid-duration}")
    private long validDuration;

    @Value("${jwt.refreshable-duration}")
    private long refreshableDuration;

    public JwtUtil(@Value("${jwt.signerKey}") String signerKey) {
        this.secretKey = Keys.hmacShaKeyFor(signerKey.getBytes());
    }

    public String generateAccessToken(User user) {
        return generateToken(user, validDuration, "ACCESS_TOKEN");
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshableDuration, "REFRESH_TOKEN");
    }

    private String generateToken(User user, long duration, String tokenType) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issuer("dac-intern")
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(duration, ChronoUnit.SECONDS)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("type", tokenType)
                    .claim("scope", user.getRole() != null ? user.getRole().toString() : "")
                    .build();

            JWSObject jwsObject = new JWSObject(header, new Payload(claims.toJSONObject()));
            jwsObject.sign(new MACSigner(secretKey.getEncoded()));

            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error generating token", e);
            throw new JwtException("Error generating token", e);
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String refreshAccessToken(String refreshToken) {
        try {
            JWTClaimsSet claims = verifyToken(refreshToken, "REFRESH_TOKEN");
            String email = claims.getSubject();
            User user = new User();
            user.setEmail(email);
            return generateAccessToken(user);
        } catch (ParseException | JOSEException e) {
            log.error("Error refreshing access token", e);
            throw new JwtException("Error refreshing access token", e);
        }
    }

    public String adjustTokenExpiry(String oldToken, long newDuration) {
        try {
            JWSObject jwsObject = JWSObject.parse(oldToken);
            JWTClaimsSet oldClaims = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());

            String email = oldClaims.getSubject();
            String scope = oldClaims.getStringClaim("scope");
            String type = oldClaims.getStringClaim("type");

            JWTClaimsSet newClaims = new JWTClaimsSet.Builder()
                    .subject(email)
                    .issuer("dac-intern")
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(newDuration, ChronoUnit.SECONDS)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("type", type)
                    .claim("scope", scope)
                    .build();

            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            JWSObject newJwsObject = new JWSObject(header, new Payload(newClaims.toJSONObject()));
            newJwsObject.sign(new MACSigner(secretKey.getEncoded()));

            return newJwsObject.serialize();
        } catch (Exception e) {
            log.error("Error adjusting token expiry", e);
            throw new JwtException("Error adjusting token expiry", e);
        }
    }


    private JWTClaimsSet verifyToken(String token, String expectedType) throws JOSEException, ParseException {
        JWSObject jwsObject = JWSObject.parse(token);
        JWSVerifier verifier = new MACVerifier(secretKey.getEncoded());

        if (!jwsObject.verify(verifier)) {
            throw new JwtException("Invalid token signature");
        }

        JWTClaimsSet claims = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());

        if (claims.getExpirationTime().before(new Date())) {
            throw new JwtException("Token has expired");
        }

        String tokenType = (String) claims.getClaim("type");
        if (!expectedType.equals(tokenType)) {
            throw new JwtException("Invalid token type");
        }

        return claims;
    }

    public boolean validateToken(String token) {
        try {
            verifyToken(token, "ACCESS_TOKEN");
            return true;
        } catch (Exception e) {
            log.error("Token validation error", e);
            return false;
        }
    }

    public String getSubjectFromToken(String token) {
        try {
            JWTClaimsSet claims = verifyToken(token, "ACCESS_TOKEN");
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Error parsing token", e);
            throw new JwtException("Error parsing token", e);
        }
    }

    public Authentication getAuthentication(String token) {
        try {
            JWTClaimsSet claims = verifyToken(token, "ACCESS_TOKEN");

            String email = claims.getSubject();

            String scope = (String) claims.getClaim("scope");
            List<SimpleGrantedAuthority> authorities = Collections.emptyList();

            if (scope != null && !scope.isEmpty()) {
                authorities = List.of(scope.split(" "))
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }

            return new UsernamePasswordAuthenticationToken(email, null, authorities);
        } catch (Exception e) {
            throw new JwtException("Error getting authentication from token", e);
        }
    }
}