package com.hrms.jt_construction.service;

import com.hrms.jt_construction.jpa.RefreshToken;
import com.hrms.jt_construction.jpa.Users;
import com.hrms.jt_construction.jpa.repos.RefreshTokenRepository;
import com.hrms.jt_construction.jpa.repos.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${app.refresh-token-expiration-days}")
    private long refreshTokenDurationDays; // e.g., 30 days

    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 1. Check if user already has a token (optional: delete old token for security)
        refreshTokenRepository.deleteByUser_Id(user.getId());
        refreshTokenRepository.flush();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);

        // 2. Set expiration (e.g., 30 days from now)
        refreshToken.setExpiryDate(Instant.now().plus(refreshTokenDurationDays, ChronoUnit.DAYS));

        // 3. Generate a secure token string (e.g., UUID)
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
}
