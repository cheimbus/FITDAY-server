package FITDAY.common.service;

import org.springframework.stereotype.Service;

@Service
public class RefreshTokenKeyCreate {

    public String createKey(String email, String provider) {

        return email + ":" + provider;
    }
}
