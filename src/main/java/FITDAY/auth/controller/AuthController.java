package FITDAY.auth.controller;

import FITDAY.auth.dto.response.AuthResponse;
import FITDAY.auth.dto.request.OAuth2AuthRequest;
import FITDAY.auth.dto.request.UserRequest;
import FITDAY.auth.factoryMethod.AuthServiceFactory;
import FITDAY.auth.sevice.AuthService;
import FITDAY.auth.sevice.OAuth2AuthService;
import FITDAY.user.dto.UserRequestDto;
import FITDAY.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceFactory authServiceFactory;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserRequest request) {
        AuthService service = authServiceFactory.getService("local");
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/login/{provider}")
    public String oauth2Login(@PathVariable String provider) {

        OAuth2AuthService service = (OAuth2AuthService) authServiceFactory.getService(provider);
        return "redirect:" + service.getAuthorizationUri();
    }

    @GetMapping("/callback/{provider}")
    public ResponseEntity<AuthResponse> callback(
            @PathVariable String provider,
            @RequestParam("code") String code
    ) {
        OAuth2AuthRequest req = new OAuth2AuthRequest(provider, code);
        OAuth2AuthService service = (OAuth2AuthService) authServiceFactory.getService(provider);
        AuthResponse tokens = service.authenticate(req);
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/nameValidate")
    public boolean nameValidate(@RequestBody UserRequestDto request) {
        return userService.nameValidate(request);
    }
}
