package me.kimyeonsup.blog.repository;

import java.util.Optional;
import me.kimyeonsup.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
