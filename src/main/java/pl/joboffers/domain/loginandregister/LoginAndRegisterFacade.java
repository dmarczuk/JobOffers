package pl.joboffers.domain.loginandregister;

import lombok.RequiredArgsConstructor;
import pl.joboffers.domain.loginandregister.dto.RegisterUserDto;
import pl.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import pl.joboffers.domain.loginandregister.dto.UserDto;
import pl.joboffers.domain.loginandregister.exception.UserNotFoundException;

@RequiredArgsConstructor
public class LoginAndRegisterFacade {

    private final UserValidator userValidator;
    private final UserRepository userRepository;

    public RegistrationResultDto register(RegisterUserDto registerUserDto) {
        final User user = User.builder()
                .username(registerUserDto.username())
                .password(registerUserDto.password())
                .build();
        User savedUser = userRepository.save(user);
        return new RegistrationResultDto(savedUser.id(), true, savedUser.username());
    }

    public String login(String username, String password) {
        UserDto userDto = findByUsername(username);
        if (userDto != null && userDto.password().equals(password)) {
            return "Successful login";
        } else {
            return "Invalid username or password";
        }
    }

    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserDto(user.id(), user.username(), user.password(), user.email()))
                .orElseThrow(() -> new UserNotFoundException("User not found in database"));
    }
}
