package FITDAY.auth.client;

import FITDAY.auth.dto.response.KakaoUserInfo;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient {

    @Value("${oauth.kakao.auth-url}")
    private String authUrl;

    @Value("${oauth.kakao.api-key}")
    private String apiKey;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${oauth.kakao.token-url}")
    private String tokenUrl;

    @Value("${oauth.kakao.logout-url}")
    private String logoutUrl;

    @Value("${oauth.kakao.user-info}")
    private String userUrl;

    private final RestTemplate restTemplate;

    public String getRedirectUri() {

        return UriComponentsBuilder
                .fromUriString(authUrl)
                .queryParam("response_type", "code")
                .queryParam("client_id", apiKey)
                .queryParam("redirect_uri", redirectUri)
                .build()
                .toUriString();
    }

    public String getAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", apiKey);
        form.add("redirect_uri", redirectUri);
        form.add("code", code);

        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<>(form, headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                requestEntity,
                JsonNode.class
        );

        JsonNode tokenInfo = response.getBody();
        return tokenInfo.get("access_token").asText();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String fullUserUrl = UriComponentsBuilder
                .fromUriString(userUrl)
                .build()
                .toUriString();

        JsonNode userInfo = restTemplate.exchange(fullUserUrl, HttpMethod.GET, requestEntity, JsonNode.class).getBody();

        String id = userInfo.get("id").asText();
        JsonNode account = userInfo.get("kakao_account");
        JsonNode profile = account.get("profile");
        String email = account.has("email") ? account.get("email").asText() : null;
        String name = profile.has("nickname") ? profile.get("nickname").asText() : null;

        return new KakaoUserInfo(id, email, name);
    }

    public String logout() {

        String fullLogoutUrl = UriComponentsBuilder
                .fromUriString(logoutUrl)
                .queryParam("client_id", apiKey)
                .queryParam("redirect_uri", redirectUri)
                .build()
                .toUriString();

        JsonNode logoutInfo = restTemplate.getForObject(fullLogoutUrl, JsonNode.class);

        return logoutInfo.get("state").asText();
    }
}
