package pl.joboffers.domain.loginandregister;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LoginAndRegisterFacadeTest {

    //UserValidator userValidator = new UserValidator();
    LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(
            new UserValidator(),
            new InMemoryUserRepositoryTestImpl()
    );

    @Test
    public void should_register_new_user () {
        //given
        User user = new User("FirstUser", "pass", "email@com");

        //when
        String result = loginAndRegisterFacade.register(user);

        //then
        assertThat(result).isEqualTo("success");
    }

    @Test
    public void should_not_register_user_who_exist_in_database () {
        //given
        User userInDatabase = new User("FirstUser", "pass", "email@com");
        User userToRegister = new User("FirstUser", "pass", "email@com");

        //when
        loginAndRegisterFacade.register(userInDatabase);
        String result = loginAndRegisterFacade.register(userToRegister);

        //then
        assertThat(result).isEqualTo("User exist in database");

    }

    @Test
    public void should_not_register_user_who_has_invalid_username () {
        //given
        User userToRegister = new User("", "pass", "email@com");

        //when
        String result = loginAndRegisterFacade.register(userToRegister);

        //then
        assertThat(result).isEqualTo("invalid arguments");

    }

    @Test
    public void should_not_register_user_who_has_invalid_password () {
        //given
        User userToRegister = new User("FirstUser", "", "email@com");

        //when
        String result = loginAndRegisterFacade.register(userToRegister);

        //then
        assertThat(result).isEqualTo("invalid arguments");

    }

    @Test
    public void should_not_register_user_who_has_invalid_email () {
        //given
        User userToRegister = new User("FirstUser", "pass", "");

        //when
        String result = loginAndRegisterFacade.register(userToRegister);

        //then
        assertThat(result).isEqualTo("invalid arguments");

    }

    @Test
    public void should_find_user_by_user_name () {
        //given
        User user = new User("FirstUser", "pass", "email@com");

        //when
        loginAndRegisterFacade.register(user);
        User result = loginAndRegisterFacade.findByUsername("FirstUser");

        //then
        assertThat(result).isEqualTo(user);

    }

    @Test
    public void should_not_find_user_by_user_name_who_not_exist_in_database () {
        //given
        User user = new User("FirstUser", "pass", "email@com");

        //when
        User result = loginAndRegisterFacade.findByUsername("FirstUser");

        //then
        assertThat(result).isNull();

    }

    @Test
    public void should_login_user_who_exist_in_database () {
        //given
        User userInDatabase = new User("FirstUser", "pass", "email@com");
        loginAndRegisterFacade.register(userInDatabase);

        //when
        String result = loginAndRegisterFacade.login("FirstUser", "pass");

        //then
        assertThat(result).isEqualTo("Successful login");

    }

    @Test
    public void should_not_login_new_user_because_user_not_exist_in_database () {
        //given
        User user = new User("FirstUser", "pass", "email@com");

        //when
        String result = loginAndRegisterFacade.login("FirstUser", "pass");

        //then
        assertThat(result).isEqualTo("Invalid username or password");

    }

    @Test
    public void should_not_login_user_because_invalid_password () {
        //given
        User user = new User("FirstUser", "pass", "email@com");
        loginAndRegisterFacade.register(user);

        //when
        String result = loginAndRegisterFacade.login("FirstUser", "invalidPassword");

        //then
        assertThat(result).isEqualTo("Invalid username or password");

    }

    @Test
    public void should_throw_exception_when_user_not_found() {

    }

}