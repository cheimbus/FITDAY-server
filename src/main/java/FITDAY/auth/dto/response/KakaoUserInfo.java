package FITDAY.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class KakaoUserInfo {

    private String id;
    private String email;
    private String name;
}
