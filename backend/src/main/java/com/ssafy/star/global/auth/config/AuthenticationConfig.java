package com.ssafy.star.global.auth.config;

import com.ssafy.star.common.config.properties.AppProperties;
import com.ssafy.star.common.config.properties.CorsProperties;
import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.global.auth.config.filter.TokenAuthenticationFilter;
import com.ssafy.star.global.auth.dto.BoardPrincipal;
import com.ssafy.star.global.auth.util.AuthTokenProvider;
import com.ssafy.star.global.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.ssafy.star.global.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.ssafy.star.global.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.ssafy.star.user.application.UserService;
import com.ssafy.star.user.repository.UserCacheRepository;
import com.ssafy.star.user.repository.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final AuthTokenProvider tokenProvider;
    private final CorsProperties corsProperties;
    private final AppProperties appProperties;
    private final UserCacheRepository userCacheRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 토큰 필터 설정
     */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    /**
     * 쿠키 기반 인가 Repository
     * 인가응답을 연계하고 검증할 때 사용
     */
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /**
     * OAuth 인증 성공 핸들러
     *
     * @return
     */
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                tokenProvider,
                appProperties,
                userRefreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    /**
     * OAuth 인증 실패 핸들러
     */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    /**
     * Cors 설정
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfig.addAllowedOriginPattern("*");
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(corsConfig.getMaxAge());

        corsConfigurationSource.registerCorsConfiguration("/**", corsConfig);
        return corsConfigurationSource;

    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return email -> userService
                .loadUserByEmail(email)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("email : %s not founded")));

    }
}
