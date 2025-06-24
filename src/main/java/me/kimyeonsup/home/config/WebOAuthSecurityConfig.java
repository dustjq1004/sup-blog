package me.kimyeonsup.home.config;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.config.jwt.TokenAuthenticationFilter;
import me.kimyeonsup.home.config.jwt.TokenProvider;
import me.kimyeonsup.home.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import me.kimyeonsup.home.config.oauth.OAuth2SuccessHandler;
import me.kimyeonsup.home.config.oauth.OAuth2UserCustomService;
import me.kimyeonsup.home.login.repository.RefreshTokenRepository;
import me.kimyeonsup.home.login.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@Profile({"dev", "test"})
public class WebOAuthSecurityConfig {
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final HttpSession httpSession;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable());

        http.sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
        );

//        http.addFilterAfter(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(authorization -> authorization
                .requestMatchers("/static/**").permitAll()
                .requestMatchers("/api/token").permitAll()
                .requestMatchers("/api/main/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/articles").permitAll()
                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/admin/**").authenticated()
                .requestMatchers("/blog/new-article").authenticated()
                .anyRequest().permitAll());

        http.oauth2Login(oAuth2Login -> oAuth2Login
                .loginPage("/login")
                .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                        .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                        .userService(oAuth2UserCustomService))
                .failureUrl("/login-error")
                .successHandler(oAuth2AuthorizationSuccessHandler()));

        // Logout Filter가 내부적으로 세션 만료를 해준다.
        http.logout(logout ->
                logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/blog"));

        http.exceptionHandling(exceptionHandler ->
                exceptionHandler.defaultAuthenticationEntryPointFor( // -> 특정 조건에 대해 기본 AuthenticationEntryPoint를 설정
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), // -> 응답 코드
                        new AntPathRequestMatcher("/api/**"))); //-> 요청 대상

        return http.build();
    }

    @Bean
    public OAuth2SuccessHandler oAuth2AuthorizationSuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userService);
    }

    //    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, httpSession);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
