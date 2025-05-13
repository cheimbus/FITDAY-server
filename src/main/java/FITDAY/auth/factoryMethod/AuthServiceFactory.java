package FITDAY.auth.factoryMethod;

import FITDAY.auth.sevice.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class AuthServiceFactory {

    private final Map<String, AuthService> authServices;

    public AuthService getService(String provider) {
        return authServices.get(provider);
    }
}
