package pl.joboffers.domain.loginandregister;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import pl.joboffers.domain.loginandregister.dto.RegisterUserDto;
import pl.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import pl.joboffers.domain.loginandregister.dto.UserDto;
import pl.joboffers.domain.loginandregister.exception.UserRegistrationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

class LoginAndRegisterFacadeTest {

    LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(
            new InMemoryUserRepositoryTestImpl()
    );

    @Test
    public void should_register_new_user () {
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto( "FirstUser", "pass", "email@com");

        //when
        RegistrationResultDto result = loginAndRegisterFacade.register(registerUserDto);

        //then
        assertAll(
                () -> assertThat(result.created()).isTrue(),
                () -> assertThat(result.username()).isEqualTo("FirstUser")
        );
    }

    @Test
    public void should_throw_exception_when_user_cannot_register () {
        //given
        RegisterUserDto userInDatabase = new RegisterUserDto( "FirstUser", "pass", "email@com");
        RegisterUserDto userToRegister = new RegisterUserDto( "FirstUser", "pass", "email@com");

        //when
        loginAndRegisterFacade.register(userInDatabase);
        Throwable thrown = catchThrowable(() -> loginAndRegisterFacade.register(userToRegister));

        //then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(UserRegistrationException.class)
                .hasMessage("User cannot register");
    }

    @Test
    public void should_find_user_by_user_name () {
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto( "FirstUser", "pass", "email@com");

        //when
        RegistrationResultDto register = loginAndRegisterFacade.register(registerUserDto);
        UserDto result = loginAndRegisterFacade.findByUsername("FirstUser");

        //then
        assertThat(result).isEqualTo(new UserDto(register.id(), "FirstUser", "pass", "email@com"));
    }

    @Test
    public void should_throw_exception_when_user_not_found() {
        //given
        RegisterUserDto userNotRegistered = new RegisterUserDto( "FirstUser", "pass", "email@com");

        //when
        Throwable thrown = catchThrowable(() -> loginAndRegisterFacade.findByUsername("FirstUser"));

        //then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("User not found");

    }

}