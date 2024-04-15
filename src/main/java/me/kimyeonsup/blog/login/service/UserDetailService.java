package me.kimyeonsup.blog.login.service;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.blog.config.oauth.PrincipalDetail;
import me.kimyeonsup.blog.login.domain.entity.User;
import me.kimyeonsup.blog.login.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException((email)));
        return new PrincipalDetail(user);
    }
}
