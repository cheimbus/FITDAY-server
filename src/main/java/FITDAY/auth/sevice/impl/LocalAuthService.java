package FITDAY.auth.sevice;

import FITDAY.auth.dto.AuthRequest;
import FITDAY.auth.dto.AuthResponse;
import FITDAY.auth.dto.UserRequest;
import FITDAY.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("local")
@RequiredArgsConstructor
public class LocalAuthService implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        UserRequest req = (UserRequest) request;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String token = jwtProvider.createToken(user.getUsername(), user.getAuthorities().stream()
                .map(granted -> granted.getAuthority()).toList());

        return new AuthResponse(token, null);
    }
}
