package com.pablo.digitalstore.digital_store_api.jwt;

import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isRefreshToken(String token);
    Instant extractIssuedAt(String token);
}
