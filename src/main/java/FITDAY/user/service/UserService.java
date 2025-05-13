package FITDAY.user.service;

import FITDAY.user.dto.UserRequestDto;

public interface UserService {

    void saveUser(UserRequestDto requestDto);
    boolean nameValidate(UserRequestDto request);
}
