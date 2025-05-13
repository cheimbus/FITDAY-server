package FITDAY.auth.sevice;

import FITDAY.auth.dto.response.AuthResponse;
import FITDAY.auth.dto.request.OAuth2AuthRequest;

public interface OAuth2AuthService extends AuthService {

    String getAuthorizationUri();
    AuthResponse authenticate(OAuth2AuthRequest req);
}
