package FITDAY.auth.sevice.impl;

import FITDAY.auth.client.KakaoOAuthClient;
import FITDAY.auth.dto.response.KakaoUserInfo;
import FITDAY.auth.dto.request.AuthRequest;
import FITDAY.auth.dto.response.AuthResponse;
import FITDAY.auth.dto.request.OAuth2AuthRequest;
import FITDAY.auth.jwt.JwtProvider;
import FITDAY.auth.sevice.AuthService;
import FITDAY.auth.sevice.OAuth2AuthService;
import FITDAY.user.AuthRole;
import FITDAY.user.dto.UserRequestDto;
import FITDAY.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service("kakao")
@RequiredArgsConstructor
public class KakaoAuthService implements AuthService, OAuth2AuthService {

    private final KakaoOAuthClient kakaoClient;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final RedisTemplate redisTemplate;

    @Override
    public AuthResponse authenticate(AuthRequest req) {

        return authenticate((OAuth2AuthRequest) req);
    }

    @Override
    public String getAuthorizationUri() {

        return kakaoClient.getRedirectUri();
    }

    @Override
    public AuthResponse authenticate(OAuth2AuthRequest req) {

        String oauthAccessToken = kakaoClient.getAccessToken(req.getCode());
        KakaoUserInfo userInfo = kakaoClient.getUserInfo(oauthAccessToken);

        List<String> roles = new ArrayList<>();
        roles.add(AuthRole.USER.toString());
        String accessToken = jwtProvider.createToken(userInfo.getEmail(), roles);

        userService.saveUser(new UserRequestDto(req.getProvider(), userInfo.getEmail(), userInfo.getName()));

        String redisKey = "token:" + userInfo.getEmail();

        long expirationMillis = jwtProvider.getExpiryDuration();
        redisTemplate.opsForValue()
                .set(redisKey, accessToken, Duration.ofMillis(expirationMillis));

        return new AuthResponse(accessToken);
    }
}
