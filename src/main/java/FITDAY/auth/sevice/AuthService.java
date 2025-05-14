package FITDAY.auth.sevice;

import FITDAY.auth.dto.request.AuthRequest;
import FITDAY.auth.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse authenticate(AuthRequest request);
}
