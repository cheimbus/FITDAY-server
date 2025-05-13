package FITDAY.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    private String loginType;
    private String name;
    private String email;
    private String password;
    private String accessToken;

    public UserRequestDto(String loginType, String email, String name, String accessToken) {
        this.loginType = loginType;
        this.email = email;
        this.name = name;
        this.accessToken = accessToken;
    }
}
