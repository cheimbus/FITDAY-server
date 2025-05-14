package FITDAY.user.service.impl;

import FITDAY.user.AuthRole;
import FITDAY.user.dto.UserRequestDto;
import FITDAY.user.entity.User;
import FITDAY.user.repository.UserRepository;
import FITDAY.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public boolean nameValidate(UserRequestDto request) {

        return false;
    }

    @Override
    @Transactional
    public void saveUser(UserRequestDto request) {

        userRepository
                .findByLoginTypeAndEmail(
                        request.getLoginType(),
                        request.getEmail()
                )
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setLoginType(request.getLoginType());
                    newUser.setEmail(request.getEmail());
                    newUser.setName(request.getName());
                    newUser.setRole(String.valueOf(AuthRole.USER));
                    return userRepository.save(newUser);
                });
    }
}
