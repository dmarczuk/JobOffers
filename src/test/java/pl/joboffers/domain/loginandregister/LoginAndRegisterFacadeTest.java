package pl.joboffers.domain.loginandregister;

import net.bytebuddy.implementation.bytecode.Throw;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import pl.joboffers.domain.loginandregister.dto.RegisterUserDto;
import pl.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import pl.joboffers.domain.loginandregister.dto.UserDto;
import pl.joboffers.domain.loginandregister.exception.UserNotFoundException;
import pl.joboffers.domain.loginandregister.exception.UserRegistrationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class LoginAndRegisterFacadeTest {

    //UserValidator userValidator = new UserValidator();
    LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(
            new UserValidator(),
            new InMemoryUserRepositoryTestImpl()
    );

    @Test
    public void should_register_new_user () {
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto( "FirstUser", "pass", "email@com");

        //when
        RegistrationResultDto result = loginAndRegisterFacade.register(registerUserDto);

        //then
        assertThat(result.created()).isTrue();
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
        RegisterUserDto registerUserDto = new RegisterUserDto( "FirstUser", "pass", "email@com");

        //when
        Throwable thrown = catchThrowable(() -> loginAndRegisterFacade.findByUsername("FirstUser"));

        //then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");

    }

    @Test
    public void should_not_register_user_who_has_invalid_username () {
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto( "", "pass", "email@com");

        //when
        RegistrationResultDto result = loginAndRegisterFacade.register(registerUserDto);

        //then
        assertThat(result.created()).isFalse();

    }

    @Test
    public void should_not_register_user_who_has_invalid_password () {
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto( "FirstUser", "", "email@com");

        //when
        RegistrationResultDto result = loginAndRegisterFacade.register(registerUserDto);

        //then
        assertThat(result.created()).isFalse();

    }

    @Test
    public void should_not_register_user_who_has_invalid_email () {
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto( "FirstUser", "pass", "");

        //when
        RegistrationResultDto result = loginAndRegisterFacade.register(registerUserDto);

        //then
        assertThat(result.created()).isFalse();

    }


//    @Test
//    public void should_login_user_who_exist_in_database () {
//        //given
//        RegisterUserDto registerUserDto = new RegisterUserDto( "FirstUser", "pass", "email@com");
//        loginAndRegisterFacade.register(registerUserDto);
//
//        //when
//        String result = loginAndRegisterFacade.login("FirstUser", "pass");
//
//        //then
//        assertThat(result).isEqualTo("Successful login");
//
//    }
//
//    @Test
//    public void should_not_login_new_user_because_user_not_exist_in_database () {
//        //given
//        RegisterUserDto registerUserDto = new RegisterUserDto( "FirstUser", "pass", "email@com");
//
//        //when
//        String result = loginAndRegisterFacade.login("FirstUser", "pass");
//
//        //then
//        assertThat(result).isEqualTo("Invalid username or password");
//
//    }
//
//    @Test
//    public void should_not_login_user_because_invalid_password () {
//        //given
//        RegisterUserDto registerUserDto = new RegisterUserDto( "FirstUser", "pass", "email@com");
//        loginAndRegisterFacade.register(registerUserDto);
//
//        //when
//        String result = loginAndRegisterFacade.login("FirstUser", "invalidPassword");
//
//        //then
//        assertThat(result).isEqualTo("Invalid username or password");
//
//    }

}