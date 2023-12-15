package me.kimyeonsup.blog.service;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.blog.login.domain.entity.RefreshToken;
import me.kimyeonsup.blog.repository.RefreshTokenRepository;
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
