package shop.olcl.backend.user.service;

import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.olcl.backend.auth.dto.CognitoUserDto;
import shop.olcl.backend.user.entity.User;
import shop.olcl.backend.user.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void saveOrUpdateUser(CognitoUserDto cognitoUserDto) {
        userRepository.findById(UUID.fromString(cognitoUserDto.getCognitoUserId()))
                .ifPresentOrElse(user -> {
                    user.update(cognitoUserDto);
                }, () -> {
                    userRepository.save(new User(cognitoUserDto));
                });
    }
}
