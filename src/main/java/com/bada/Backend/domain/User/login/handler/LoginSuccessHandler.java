package com.bada.Backend.domain.User.login.handler;

import com.bada.Backend.domain.User.repository.UserRepository;
import com.bada.Backend.domain.User.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String email = extractUsername(authentication); // 인증 정보에서 Username(email) 추출
        String accessToken = "Bearer " + jwtService.createAccessToken(email); // JwtService의 createAccessToken을 사용하여 AccessToken 발급
        String refreshToken = "Bearer " + jwtService.createRefreshToken(); // JwtService의 createRefreshToken을 사용하여 RefreshToken 발급

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken); // 응답 헤더에 AccessToken, RefreshToken 실어서 응답
        AtomicReference<Long> userPK = new AtomicReference<>();
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    userPK.set(user.getId());
                    user.updateRefreshToken(refreshToken);
                    userRepository.saveAndFlush(user);
                });
        log.info("로그인에 성공하였습니다. 이메일 : {}", email);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(userPK.get().toString());
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}