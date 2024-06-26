package me.kimyeonsup.home.login.service;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.login.domain.entity.RefreshToken;
import me.kimyeonsup.home.login.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
