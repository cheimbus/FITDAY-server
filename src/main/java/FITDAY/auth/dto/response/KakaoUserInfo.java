package FITDAY.auth.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class KakaoUserInfo {

    private String id;
    private String email;
    private String name;
}
