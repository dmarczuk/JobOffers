package pl.joboffers.infrastructure.loginandregister.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.domain.loginandregister.LoginAndRegisterFacade;
import pl.joboffers.domain.loginandregister.dto.RegisterUserDto;
import pl.joboffers.domain.loginandregister.dto.RegistrationResultDto;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class RegisterController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final PasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResultDto> register(@RequestBody @Valid RegisterUserDto registerUserDto) {
        String encodedPassword = bCryptPasswordEncoder.encode(registerUserDto.password());
        RegistrationResultDto registerResult = loginAndRegisterFacade.register(
                new RegisterUserDto(registerUserDto.username(), encodedPassword, registerUserDto.email()));
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResult);
    }
}
