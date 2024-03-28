package pl.joboffers.domain.loginandregister;

import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import pl.joboffers.domain.loginandregister.dto.RegisterUserDto;
import pl.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import pl.joboffers.domain.loginandregister.dto.UserDto;
import pl.joboffers.domain.loginandregister.exception.UserAlreadyExistException;

@AllArgsConstructor
@Component
public class LoginAndRegisterFacade {

    private final UserRepository userRepository;

    public RegistrationResultDto register(RegisterUserDto registerUserDto) {
        final User user = User.builder()
                .username(registerUserDto.username())
                .password(registerUserDto.password())
                .email(registerUserDto.email())
                .build();
        try {
            User savedUser = userRepository.save(user);
            return new RegistrationResultDto(savedUser.id(), true, savedUser.username());
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistException("User already exist");
        }
    }

    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserDto(user.id(), user.username(), user.password(), user.email()))
                .orElseThrow(() -> new BadCredentialsException("User not found"));
    }
}
