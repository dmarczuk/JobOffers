package pl.joboffers.domain.loginandregister;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginAndRegisterFacadeTest {

    UserValidator userValidator = new UserValidator();
    LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(userValidator);

    @Test
    public void should_register_new_user () {
        //given
        User user = new User("FirstUser", "pass", "email@com");

        //when
        loginAndRegisterFacade.register(user);
        //then

    }

    @Test
    public void should_not_register_user_who_exist_in_database () {

    }

    @Test
    public void should_find_user () { //login user

    }

    @Test
    public void should_not_find_user () {

    }

}