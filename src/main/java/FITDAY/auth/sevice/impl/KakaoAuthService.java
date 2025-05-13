package FITDAY.auth.sevice;

import FITDAY.auth.client.KakaoOAuthClient;
import FITDAY.auth.dto.AuthRequest;
import FITDAY.auth.dto.AuthResponse;
import FITDAY.auth.dto.KakaoUserInfo;
import FITDAY.auth.dto.OAuth2AuthRequest;
import FITDAY.auth.jwt.JwtProvider;
import FITDAY.user.entity.User;
import FITDAY.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.RedirectView;

@Service("kakao")
@RequiredArgsConstructor
public class KakaoAuthService implements AuthService, OAuth2AuthService {

    private final KakaoOAuthClient kakaoClient;
    private final UserRepository userRepo;
    private final JwtProvider jwtProvider;

    @Override
    public AuthResponse authenticate(AuthRequest req) {

        RedirectView redirectUri = kakaoClient.getRedirectView();

//        String accessToken = kakaoClient.getAccessToken()
//        KakaoUserInfo userInfo = kakaoClient.getUserInfo(accessToken);

//        User user = userRepo.findByProviderAndProviderId("kakao", info.getId())
//                .orElseGet(() -> userRepo.save(User.ofKakao(info)));
//        boolean nameSet = StringUtils.hasText(user.getName());
//        String jwt = jwtProvider.generateToken(user.getUsername());
        return new AuthResponse("SDFSDF", "ddasd");
    }

    @Override
    public String getAuthorizationUri() {
        return "";
    }

    @Override
    public AuthResponse authenticate(OAuth2AuthRequest req) {
        return null;
    }
}
