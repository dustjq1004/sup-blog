package me.kimyeonsup.blog.login.repository;

import java.util.Optional;
import me.kimyeonsup.blog.login.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long userId);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
