package FITDAY.auth.sevice.impl;

import FITDAY.auth.client.KakaoOAuthClient;
import FITDAY.auth.dto.response.KakaoUserInfo;
import FITDAY.auth.dto.request.AuthRequest;
import FITDAY.auth.dto.response.AuthResponse;
import FITDAY.auth.dto.request.OAuth2AuthRequest;
import FITDAY.auth.jwt.JwtProvider;
import FITDAY.auth.sevice.OAuth2AuthService;
import FITDAY.common.service.RefreshTokenKeyCreate;
import FITDAY.global.AuthRole;
import FITDAY.global.OAuthProvider;
import FITDAY.redis.auth.RefreshCacheService;
import FITDAY.user.dto.UserRequestDto;
import FITDAY.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("kakao")
@RequiredArgsConstructor
public class KakaoAuthService implements OAuth2AuthService {

    private final KakaoOAuthClient kakaoClient;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final RefreshCacheService refreshCacheService;
    private final String PROVIDER = String.valueOf(OAuthProvider.KAKAO);
    private final RefreshTokenKeyCreate refreshTokenKeyCreate;

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
        String refreshToken = jwtProvider.createRefreshToken(userInfo.getEmail(), roles);
        String redisKey = refreshTokenKeyCreate.createKey(userInfo.getEmail(), PROVIDER);

        userService.saveUser(new UserRequestDto(req.getProvider(), userInfo.getEmail(), userInfo.getName()));
        refreshCacheService.saveRefreshToken(redisKey, refreshToken);

        return new AuthResponse(accessToken);
    }
}
